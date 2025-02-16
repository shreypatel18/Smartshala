package SmartShala.SmartShala.Controller;


import SmartShala.SmartShala.Entities.*;
import SmartShala.SmartShala.Service.GoogleDriveService;
import SmartShala.SmartShala.Service.MeetingService;
import SmartShala.SmartShala.Service.TeacherService;
import SmartShala.SmartShala.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@RestController
@RequestMapping("/teacher")
@CrossOrigin
public class TeacherController {

    @Autowired
    TeacherService teacherService;
    @Autowired
    TestService testService;

    @Autowired
    MeetingService meetingService;

    @PostMapping("/generateTest")
    String generateTest(@RequestBody Test test, @RequestParam int subCode) throws IOException {
//        System.out.println(test.getName());
//        System.out.println(subCode);
//        System.out.println(test.getQuetionPaper().getTheoryQuetions());
//        System.out.println(test.getAnswerKey());
        System.out.println("hello");
        System.out.println(test.getQuetionPaper().getMcqTypeQuetionList().get(0).getQuetion());
        System.out.println(test.getQuetionPaper().getMcqTypeQuetionList().get(0).getOptionA());
        return testService.generateTest(test.getQuetionPaper(), test.getAnswerKey(), subCode, test.getName());

    }

    @GetMapping("/getTestData")
    Test getAllAnswerSheets(@RequestParam int testId) {
        return testService.getTest(testId);
    }

    @GetMapping("/endTest")
    List<Result> endTest(@RequestParam int testId) throws Exception {
        return testService.evaluate(testId);
    }

    @GetMapping("publishResult")
    String publish(@RequestParam int testId){
     return testService.publish(testId);
    }

    @GetMapping("/addTheoryMarks")
    Result enterTheoryMarks(@RequestParam int studentId, @RequestParam int testId, @RequestParam int marks) {
        return testService.enterTheoryMarks(studentId, testId, marks);
    }


    @GetMapping("/notifyMeeting")
    String notifyMeeting(@RequestParam String meetingId) {
        return meetingService.notifyMeetingService(meetingId);
    }

    @GetMapping("/schedule")
    public String scheduleMeeting(@RequestParam String datetime) {

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("d MMM h:mm a") // Your input pattern
                .parseDefaulting(ChronoField.YEAR, LocalDateTime.now().getYear()) // Default year to current year
                .toFormatter();

        // Parse the string into LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(datetime, formatter);
        meetingService.scheduleTask(dateTime);
        return "success";
    }

    @GetMapping("viewAnswerSheetPhotos")
    List<Photo> viewAnswerSheetPhotos(@RequestParam int studentId, @RequestParam int testId) throws IOException {
        List<Photo> photos = testService.getAnswerSheetImages(studentId,testId);
        return photos;
    }



    @GetMapping("home")
    Teacher Home(){
    Teacher teacher = teacherService.getTeacher(1);
    teacher.setPhoto(null);
    return teacher;
    }


    @GetMapping("/getTests")
    List<Test> getTestBySubject(@RequestParam  int subCode){
        return testService.getByTestsSubjectId(subCode);
    }

}




