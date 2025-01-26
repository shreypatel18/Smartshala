package SmartShala.SmartShala.Test;


import SmartShala.SmartShala.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    MailService mailService;

    @GetMapping("sendMail")
    String sendMail(){
        mailService.sendMail("sk","skm","snk","si");
        return "success1";
    }
}
