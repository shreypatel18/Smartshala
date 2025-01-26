package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.Entities.Answer;
import SmartShala.SmartShala.Entities.McqTypeQuetion;
import SmartShala.SmartShala.Entities.Test;
import SmartShala.SmartShala.Entities.TheoryQuetion;
import SmartShala.SmartShala.Repository.AnswerRepository;
import SmartShala.SmartShala.Repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    TestRepository testRepository;

    @Autowired
    AnswerRepository answerRepository;
    public String generateTest(List<McqTypeQuetion> mcqTypeQuetionList, List<TheoryQuetion> theoryQuetionList, Answer answerKey){
        Test test = new Test();
        test.setTheoryQuetions(theoryQuetionList);
        test.setMcqs(mcqTypeQuetionList);
        test.setAnswerKey(answerKey);
        Test temp = testRepository.save(test);
        return "student/getTest?tesTid="+temp.getId();
    }

    public Test getTest(int testId){
        return  testRepository.findById(testId).get();
    }

    public Answer submitAnswer(int testId,Answer answer){
        Test test = testRepository.findById(testId).get();
        answer.setTest(test);
        return answerRepository.save(answer);
    }

}
