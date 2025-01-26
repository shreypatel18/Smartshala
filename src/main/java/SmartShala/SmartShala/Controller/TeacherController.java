package SmartShala.SmartShala.Controller;


import SmartShala.SmartShala.Entities.Answer;
import SmartShala.SmartShala.Entities.Teacher;
import SmartShala.SmartShala.Entities.Test;
import SmartShala.SmartShala.Service.TeacherService;
import SmartShala.SmartShala.Service.TestService;
import org.hibernate.annotations.Struct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;
    @Autowired
    TestService testService;

    @PostMapping("/generateTest")
    String generateTest(@RequestBody Test test){
        return  testService.generateTest(test.getMcqs(), test.getTheoryQuetions(), test.getAnswerKey());
    }

    @GetMapping("/getTestData")
    Test getAllAnswerSheets(@RequestParam int testId){
        return  testService.getTest(testId);
    }







}
