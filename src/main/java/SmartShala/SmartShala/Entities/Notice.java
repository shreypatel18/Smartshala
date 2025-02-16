package SmartShala.SmartShala.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;


public class Notice {


    String subject;
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
