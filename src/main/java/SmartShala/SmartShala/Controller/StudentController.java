package SmartShala.SmartShala.Controller;


import SmartShala.SmartShala.Entities.*;
import SmartShala.SmartShala.Service.GoogleDriveService;
import SmartShala.SmartShala.Service.StudentService;
import SmartShala.SmartShala.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
@CrossOrigin
public class StudentController {

    @Autowired
    TestService testService;
    @Autowired
    StudentService studentService;

    @GetMapping("getTest")
    QuetionPaper getTest(@RequestParam int tesTid){
        Test test = testService.getTest(tesTid);
        if(test.getStatus().equals("active")) {
            QuetionPaper quetionPaper = test.getQuetionPaper();
            quetionPaper.setTestId(tesTid);

            return quetionPaper;
        }
        return null;
    }

    @GetMapping("submit")
   Answer submitAnswer(@RequestParam int testId, @RequestBody Answer answer){
       return testService.submitAnswer(testId, answer);
    }

    @GetMapping("getAiOverview")
    String getAiOverview(@RequestParam int testId,@RequestParam int quetionIndex,@RequestParam String answer){
        return testService.getAiOverview(answer, testId, 0);
    }

    @GetMapping("viewAnswers")
    AnswerSheetDto viewAnswerSheet(@RequestParam int studentId, @RequestParam int testId){
        return testService.getAnswerSheetDto(studentId, testId);
    }

//    @GetMapping("viewAnswerSheetPhotos")
//    List<Photo> viewAnswerSheetPhotos(@RequestParam String studentId, @RequestParam String testId, @RequestParam String className, @RequestParam String subject) throws IOException {
//        List<Photo> photos =  GoogleDriveService.get(className, studentId, subject, testId);
//        return photos;
//    }


    @GetMapping("viewAnswerSheetPhotos")
    List<Photo> viewAnswerSheetPhotos(@RequestParam int studentId, @RequestParam int testId) throws IOException {
        List<Photo> photos = testService.getAnswerSheetImages(studentId,testId);
        return photos;
    }

    @GetMapping("home")
    StudentDto getStudentData(){
        return studentService.getStudentDto(1);
    }


    @GetMapping("viewTests")
    Map<String,List<TestDto>> getTestDtos(@RequestParam int subCode){
        return testService.getTestBySubjectIdMap(subCode);
    }



    @GetMapping("getResult")
    Result getResult(@RequestParam int studentId,@RequestParam int testId){
        return testService.getResult(testId,studentId);
    }


}
