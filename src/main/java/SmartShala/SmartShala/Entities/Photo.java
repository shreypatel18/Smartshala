package SmartShala.SmartShala.Entities;

import jakarta.persistence.*;

@Entity
public class Photo {

    @Id
    private int id;

    private String fileType;

    @Lob
    private String base64EncodedPhoto;

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
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getBase64EncodedPhoto() {
        return base64EncodedPhoto;
    }
    public void setBase64EncodedPhoto(String base64EncodedPhoto) {
        this.base64EncodedPhoto = base64EncodedPhoto;
    }
}
