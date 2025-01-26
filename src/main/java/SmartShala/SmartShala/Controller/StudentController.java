package SmartShala.SmartShala.Controller;


import SmartShala.SmartShala.Entities.Answer;
import SmartShala.SmartShala.Entities.Student;
import SmartShala.SmartShala.Entities.Test;
import SmartShala.SmartShala.Service.StudentService;
import SmartShala.SmartShala.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    TestService testService;
    @Autowired
    StudentService studentService;

    @GetMapping("getTest")
    Test getTest(@RequestParam int tesTid){
        Test test = testService.getTest(tesTid);
        test.setAnswerKey(null);
        test.setAnswers(null);
        return test;
    }

    @GetMapping("submit")
   Answer submitAnswer(@RequestParam int testId, @RequestBody Answer answer){
       return testService.submitAnswer(testId, answer);
    }

}
