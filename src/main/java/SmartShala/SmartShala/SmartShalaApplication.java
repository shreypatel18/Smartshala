package SmartShala.SmartShala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartShalaApplication {

    public static void main(String[] args) {
        System.out.println(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
        SpringApplication.run(SmartShalaApplication.class, args);
    }


}
