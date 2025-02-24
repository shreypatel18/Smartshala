package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.CustomException.SubjectNotAssigned;
import SmartShala.SmartShala.CustomException.TestException;
import SmartShala.SmartShala.Entities.*;
import SmartShala.SmartShala.Repository.AnswerRepository;
import SmartShala.SmartShala.Repository.ResultRepository;
import SmartShala.SmartShala.Repository.SubjectRepository;
import SmartShala.SmartShala.Repository.TestRepository;
import org.apache.poi.util.DocumentFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.tensorflow.op.math.Sub;

import javax.annotation.Nullable;
import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static SmartShala.SmartShala.Service.GoogleOcrService.detectDocumentText;

@Service
public class TestService {

    @Autowired
    TestRepository testRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    AiService aiService;

    @Autowired
    SubjectRepository subjectRepository;

    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    public TestService() {
        taskScheduler.initialize();
    }

    public ScheduledFuture<?> scheduleTestActivation(int testId, String activationTime) {
        return taskScheduler.schedule(() -> changeStatusActive(testId), TimeUtil.convertToInstant(activationTime));
    }

    public ScheduledFuture<?> scheduleTestCompletion(int testId, String completionTime) {
        return taskScheduler.schedule(() -> {
            changeStatusComplete(testId);
        }, TimeUtil.convertToInstant(completionTime));
    }

    public String generateTest(QuetionPaper quetionPaper, Answer answerKey, int subCode, String name) throws IOException {
        Test test = new Test();
        if (testRepository.findByName(name).isPresent()) throw new TestException("test with this name already exists");
        if (!subjectRepository.existsById(subCode)) throw new TestException("Subject does not exists");
        test.setName(name);
        Subject subject = subjectRepository.findById(subCode).get();
        test.setSubject(subject);
        test.setQuetionPaper(quetionPaper);
        test.setAnswerKey(answerKey);
        test.setStatus("upcoming");
        if (TimeUtil.isAfterCurrentTime(test.getQuetionPaper().getStartTime())) {
            GoogleDriveService.createTestFolder(subject.getClassroom().getName(), String.valueOf(subject.getSubCode()), name);
            Test temp = testRepository.save(test);
            scheduleTestActivation(temp.getId(), temp.getQuetionPaper().getStartTime());
            scheduleTestCompletion(temp.getId(), TimeUtil.addMinutesToDateTime(temp.getQuetionPaper().getStartTime(), temp.getQuetionPaper().getDuration() + 1));
            return String.valueOf(temp.getId());
        }
        throw new TestException("something went wrong");
    }


    public List<Test> getTestsBySubjectId(int id) {
        return testRepository.findBySubject(subjectRepository.findById(id).orElseThrow(() -> new SubjectNotAssigned("subject for id: " + id + " does not exist")));
    }

    public Test getTest(int testId) {
        return testRepository.findById(testId).orElseThrow(() -> new TestException("test with id " + testId + " does not exists"));
    }

    public Answer submitAnswer(int testId, Answer answer) {

        Test test = testRepository.findById(testId).orElseThrow(() -> new TestException("cannot submit test with id " + testId + " doesnt exists"));

        if (test.getStatus().equals("active")) {
            if (answerRepository.findByStudentIdAndTest_Id(answer.getStudentId(), testId).isEmpty()) {
                answer.setTest(test);
                return answerRepository.save(answer);
            }
        }
        return null;
    }

    public List<Result> evaluate(int testId) {
        List<Result> results = new ArrayList<>();
        Test currTest = testRepository.findById(testId).get();
        List<Answer> answers = currTest.getAnswers();
        Answer answerKey = currTest.getAnswerKey();

        for (Answer answer : answers) {
            Result result = new Result();
            result.setStudentId(answer.getStudentId());
            result.setStatus(false);
            result.setTest(currTest);
            result.setMcqMarks(mcqEvaluation(answer, answerKey));
            results.add(result);
        }
        currTest.setResults(results);
        testRepository.save(currTest);
        return results;
    }

    public void changeStatusActive(int id) {
        Test test = testRepository.findById(id).get();
        test.setStatus("active");
        testRepository.save(test);
    }

    public void changeStatusComplete(int id) {
        Test test = testRepository.findById(id).get();
        test.setStatus("complete");
        evaluate(id);
        testRepository.save(test);
    }

    public int mcqEvaluation(Answer answer, Answer answerKey) {
        List<Character> mcqAnswerKey = answerKey.getMcqAnswers();
        int marks = 0;
        List<Character> mcqAnswers = answer.getMcqAnswers();
        for (int i = 0; i < mcqAnswerKey.size(); i++) {
            if (i > mcqAnswers.size() - 1) {
                break;
            }
            if (Character.toLowerCase(mcqAnswerKey.get(i)) == Character.toLowerCase(mcqAnswers.get(i))) {
                marks++;
            }
        }
        return marks;
    }

    public Result enterTheoryMarks(int studentId, int testId, int marks) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new TestException("marks not submitted test with id " + testId + " not found"));
        if (test.getStatus().equals("complete")) {
            Result result = resultRepository.findByStudentIdAndTest(studentId, test).orElseThrow(() -> new TestException("student did not gave the test"));
            if (test.getQuetionPaper().getTheoryMarks() < marks) {
                throw new TestException("entered marks: " + marks + " must be smaller than or equal: " + test.getQuetionPaper().getTheoryMarks());
            }
            result.setTheoryMarks(marks);
            result.setStatus(true);
            return resultRepository.save(result);
        }
        throw new TestException("test is not in complete state it might be in upcoming active or checked");
    }

    public String getAiOverview(String answer, int testId, int quetionIndex) {
        List<String> answers = testRepository.findById(testId).orElseThrow(() -> new TestException("test does not exist for test id: " + testId)).getAnswerKey().getTheoryAnswers();
        if (quetionIndex > answers.size() - 1) {
            throw new TestException("quetion index is not proper");
        }
        String answerKey = answers.get(quetionIndex);
        return aiService.generateReply(answer, answerKey);
    }

    public Answer viewAnswerSheet(int studentId, int testId) {
        return answerRepository.findByStudentIdAndTest_Id(studentId, testId).get();
    }

    public Answer addScannedTheoryAnswers(int studentId, int testId, MultipartFile[] images) {
        Answer answer = answerRepository.findByStudentIdAndTest_Id(studentId, testId).orElseThrow(() -> new TestException("answer sheet not found student did not attempted test"));

        List<byte[]> answers = new ArrayList<>();

        List<String> theoryAnswers = answer.getTheoryAnswers();

        try {
            for (MultipartFile multipartFile : images) {
                answers.add(multipartFile.getBytes());
            }
        } catch (Exception e) {
            throw new DocumentFormatException("issue is processing images, there may be some error in file");
        }

        Map<String, String> OcrAnswers = GoogleOcrService.detectDocumentText(answers);
        OcrAnswers.keySet().stream()
                .filter(key -> key != null && key.matches("\\d+")) // Avoid null & non-numeric keys
                .sorted((key1, key2) -> Integer.compare(Integer.parseInt(key1), Integer.parseInt(key2)))
                .forEach(key -> {
                    String value = OcrAnswers.get(key);
                    if (value != null) {
                        theoryAnswers.add(value);
                    }
                });
        return answerRepository.save(answer);
    }


    public List<TestDto> getTestBySubjectId(int subCode) {
        return DTOtraforms.getTestDtos(testRepository.findBySubject(subjectRepository.findById(subCode).get()));
    }

    public Map<String, List<TestDto>> getTestBySubjectIdMap(int subCode) {
        return DTOtraforms.getMapTestDto(testRepository.findBySubject(subjectRepository.findById(subCode).orElseThrow(() -> new SubjectNotAssigned("subject with code: " + subCode + " does not exist"))));
    }


    public void uploadImages(int testId, MultipartFile[] images, int studentId) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new TestException("test with id " + testId + " not found"));
        if (test.getStatus().equals("active") || test.getStatus().equals("upcoming")) {
            throw new TestException("test is still not completed");
        }
        if (!checkIfStudentAttemptedTest(studentId, test)) {
            throw new TestException("student did not attempted test cant upload images");
        }
        String subjectCode = String.valueOf(test.getSubject().getSubCode());
        String classname = test.getSubject().getClassroom().getName();
        GoogleDriveService.saveImagesToGoogleDrive(images, classname, subjectCode, test.getName(), studentId);
    }

    public List<Photo> getAnswerSheetImages(int studentId, int testId) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new TestException("test not found for testId: " + testId));
        String className = test.getSubject().getClassroom().getName();
        String subjectCode = String.valueOf(test.getSubject().getSubCode());
        String testName = test.getName();
        if (!checkIfStudentAttemptedTest(studentId, test))
            throw new TestException("student did not attempted test cant upload papers");
        return GoogleDriveService.get(className, subjectCode, testName, studentId);
    }


    public Result getResult(int testId, int studentId) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new TestException("unable to get result, test with id " + testId + " does not exists"));
        if (test.getStatus().equals("checked")) {
            return resultRepository.findByStudentIdAndTest(studentId, test).orElseThrow(() -> new TestException("student did not appeared for the test"));
        }
        return null;
    }


    public AnswerSheetDto getAnswerSheetDto(int studentId, int testId) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new TestException("test does not exist for id: " + testId));
        if (!test.getStatus().equals("checked")) return null;
        Answer answer = answerRepository.findByStudentIdAndTest_Id(studentId, testId).orElseThrow(() -> new TestException("something went wrong, student did not attempted test or testId is wrong"));
        QuetionPaper quetionPaper = testRepository.findById(testId).get().getQuetionPaper();
        AnswerSheetDto answerSheetDto = new AnswerSheetDto();
        answerSheetDto.setAnswer(answer);
        answerSheetDto.setQuetionPaper(quetionPaper);
        return answerSheetDto;
    }

    public String publish(int testId) {
        Test test = testRepository.findById(testId).orElseThrow(() -> new TestException("cannot publish results because test with id " + testId + " does not exists"));
        if (test.getStatus().equals("complete")) {
            for (Result result : test.getResults()) {
                if (result.getStatus() == false) {
                    return "checking pending";
                }
            }
            test.setStatus("checked");
            testRepository.save(test);
            return "published";
        }
        return "test is not in complete state, it might be active or upcoming cannot publish results";
    }


    public Boolean checkIfStudentAttemptedTest(int studentId, Test test) {
        for (Answer answer : test.getAnswers()) {
            if (answer.getStudentId() == studentId) {
                return true;
            }
        }
        return false;
    }
}
