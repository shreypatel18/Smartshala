package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.Entities.*;
import SmartShala.SmartShala.Repository.AnswerRepository;
import SmartShala.SmartShala.Repository.ResultRepository;
import SmartShala.SmartShala.Repository.SubjectRepository;
import SmartShala.SmartShala.Repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.tensorflow.op.math.Sub;

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
        System.out.println(activationTime+"set active");
        return taskScheduler.schedule(() -> changeStatusActive(testId), TimeUtil.convertToInstant(activationTime));
    }

    public ScheduledFuture<?> scheduleTestCompletion(int testId, String completionTime) {
        System.out.println("set complete"+completionTime);
        return taskScheduler.schedule(() -> {
            try {
                changeStatusComplete(testId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, TimeUtil.convertToInstant(completionTime));
    }
    public String generateTest(QuetionPaper quetionPaper, Answer answerKey, int subCode,String name) throws IOException {
        Test test = new Test();
        System.out.println(quetionPaper.getMcqTypeQuetionList().get(0).getOptionA());
        test.setName(name);
        Subject subject = subjectRepository.findById(subCode).get();
        test.setSubject(subject);
        test.setQuetionPaper(quetionPaper);
        test.setAnswerKey(answerKey);
        test.setStatus("upcoming");
        if(TimeUtil.isAfterCurrentTime(test.getQuetionPaper().getStartTime())){
            Test temp = testRepository.save(test);
            this.scheduleTestActivation(temp.getId(), temp.getQuetionPaper().getStartTime());
            this.scheduleTestCompletion(temp.getId(), TimeUtil.addMinutesToDateTime(temp.getQuetionPaper().getStartTime(), temp.getQuetionPaper().getDuration() + 1));
            GoogleDriveService.createTestFolder(subject.getClassroom().getName(), subject.getName(), name);
            return "student/getTest?tesTid=" + temp.getId();
        }
        return "something went wrong";
    }


    public List<Test> getByTestsSubjectId(int id){
        return testRepository.findBySubject(subjectRepository.findById(id).get());
    }

    public Test getTest(int testId){
        return  testRepository.findById(testId).get();
    }

    public Answer submitAnswer(int testId,Answer answer){

        Test test = testRepository.findById(testId).get();

        if(test.getStatus().equals("active")) {
            if (answerRepository.findByStudentIdAndTest_Id(answer.getStudentId(), testId).isEmpty()) {
                answer.setTest(test);
                return answerRepository.save(answer);
            }
        }
        return null;
    }

    public List<Result> evaluate(int testId) throws Exception {
        List<Result> results = new ArrayList<>();
        Test currTest = testRepository.findById(testId).get();
        List<Answer> answers = currTest.getAnswers();
        Answer answerKey = currTest.getAnswerKey();

        for(Answer answer : answers){
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

    public void changeStatusActive(int id){
        System.out.println("in change status");
        Test test  = testRepository.findById(id).get();
        test.setStatus("active");
        testRepository.save(test);
    }

    public void changeStatusComplete(int id) throws Exception {
        System.out.println("in change status complete");
        Test test = testRepository.findById(id).get();
        test.setStatus("complete");
        evaluate(id);
        testRepository.save(test);
    }

    public int mcqEvaluation(Answer answer, Answer answerKey) throws Exception{

        List<Character> mcqAnswerKey = answerKey.getMcqAnswers();

        int marks = 0;

         List<Character> mcqAnswers = answer.getMcqAnswers();

         if(mcqAnswers.size() != mcqAnswerKey.size()){
             throw new Exception("difference in answer key sizes mismatch");
         }

         for(int i = 0; i< mcqAnswerKey.size(); i++){
             if(Character.toLowerCase(mcqAnswerKey.get(i))==Character.toLowerCase(mcqAnswers.get(i))){
                 marks++;
             }
         }

         return  marks;



    }

    public Result enterTheoryMarks(int studentId, int testId, int marks){

        Test test = testRepository.findById(testId).get();
        if(test.getStatus().equals("complete")) {
            Result result = resultRepository.findByStudentIdAndTest(studentId, test).get();
            result.setTheoryMarks(marks);
            result.setStatus(true);
            return resultRepository.save(result);
        }
        return null;
    }

    public String getAiOverview(String answer,int testId, int quetionIndex){
    String answerKey = testRepository.findById(testId).get().getAnswerKey().getTheoryAnswers().get(quetionIndex);
    return  aiService.generateReply(answer, answerKey);
    }

    public Answer viewAnswerSheet(int studentId, int testId){
        return answerRepository.findByStudentIdAndTest_Id(studentId,testId).get();
    }

    public Answer addScannedTheoryAnswers(int studentId, int testId, MultipartFile[] images) throws IOException {
        Answer answer = answerRepository.findByStudentIdAndTest_Id(studentId, testId).get();
        List<byte[]> answers= new ArrayList<>();
        List<String> theoryAnswers = answer.getTheoryAnswers();

        for(MultipartFile multipartFile : images){
            answers.add(multipartFile.getBytes());
        }

        Map<String, String> OcrAnswers =  GoogleOcrService.detectDocumentText(answers);
        System.out.println(OcrAnswers);

        OcrAnswers.keySet().stream()
                .sorted((key1, key2) -> Integer.compare(Integer.parseInt(key1), Integer.parseInt(key2)))  // Sort keys as numbers
                .forEach(key -> {
                    theoryAnswers.add(OcrAnswers.get(key));  // Add the corresponding value to the list in sorted order
                });
       return answerRepository.save(answer);
    }


    public List<TestDto> getTestBySubjectId(int subCode){
        return DTOtraforms.getTestDtos(testRepository.findBySubject(subjectRepository.findById(subCode).get()));
    }

    public Map<String, List<TestDto>> getTestBySubjectIdMap(int subCode){
        return DTOtraforms.getMapTestDto(testRepository.findBySubject(subjectRepository.findById(subCode).get()));
    }


    public void uploadImages(int testId,MultipartFile[] images, int studentId) throws IOException {
        Test test = testRepository.findById(testId).get();
        String subjectName = test.getSubject().getName();
        String classname = test.getSubject().getClassroom().getName();
        GoogleDriveService.saveImagesToGoogleDrive(images,classname,subjectName, test.getName(),studentId);
    }

    public List<Photo> getAnswerSheetImages(int studentId, int testId) throws IOException {
      Test test =  this.getTest(testId);
      String className = test.getSubject().getClassroom().getName();
      String subjectName = test.getSubject().getName();
      String testName = test.getName();

        System.out.println(className);
        System.out.println(subjectName);
        System.out.println(testName);
      return GoogleDriveService.get(className,subjectName,testName, studentId);
    }


    public Result getResult(int testId, int studentId){
        Test test  = testRepository.findById(testId).get();
        if(test.getStatus().equals("checked")) {
            return resultRepository.findByStudentIdAndTest(studentId, test).get();
        }
        return null;
    }


    public AnswerSheetDto getAnswerSheetDto(int studentId, int testId){
         Answer answer =answerRepository.findByStudentIdAndTest_Id(studentId,testId).get();
         QuetionPaper quetionPaper = testRepository.findById(testId).get().getQuetionPaper();
         AnswerSheetDto answerSheetDto = new AnswerSheetDto();
         answerSheetDto.setAnswer(answer);
         answerSheetDto.setQuetionPaper(quetionPaper);
         return answerSheetDto;
    }

    public String publish(int testId){
        Test test = testRepository.findById(testId).get();
        if(test.getStatus().equals("complete")){
            for(Result result: test.getResults()){
                if(result.getStatus()==false){
                    return "checking pending";
                }
            }
            test.setStatus("checked");
            testRepository.save(test);
            return "published";
        }
        return "not in  complete state";

    }

}
