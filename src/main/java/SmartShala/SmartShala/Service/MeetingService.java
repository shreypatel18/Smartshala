package SmartShala.SmartShala.Service;


import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class MeetingService {

    @Autowired
    MailService mailService;


    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10); // Number of threads in the pool
        scheduler.setThreadNamePrefix("TaskScheduler-");
        return scheduler;
    }

    public String notifyMeetingService(String meetingId) {
        mailService.sendMail("", "", "", "http://127.0.0.1:5500/test/test.html?roomID=" + meetingId);
        return "success";
    }



    public void scheduleTask(LocalDateTime scheduledTime) {
        Date date = Date.from(scheduledTime.atZone(ZoneId.systemDefault()).toInstant());
        taskScheduler().schedule(() -> mailService.sendMail("","","","meeting scheduled"), date);
    }


}