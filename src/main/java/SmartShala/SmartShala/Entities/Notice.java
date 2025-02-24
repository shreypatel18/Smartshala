package SmartShala.SmartShala.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;


public class Notice {

    @NotBlank(message = "notice subject is empty")
    String subject;

    @NotBlank(message = "notice body is empty")
    String body;

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
}
