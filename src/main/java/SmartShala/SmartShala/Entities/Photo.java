package SmartShala.SmartShala.Entities;

import jakarta.persistence.*;

@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for PostgreSQL auto-increment
    private int id;

    private String fileType;

    @Lob// Use TEXT instead of CLOB for PostgreSQL
    private String base64EncodedPhoto;

    public int getId() {
        return id;
    }

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
