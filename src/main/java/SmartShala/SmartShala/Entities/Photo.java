package SmartShala.SmartShala.Entities;


import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
public class Photo {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String fileType;

    @Lob
    String base64EncodedPhoto;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getBase64EncodedString() {
        return base64EncodedPhoto;
    }

    public void setBase64EncodedString(String base64EncodedPhoto) {
        this.base64EncodedPhoto = base64EncodedPhoto;
    }
}
