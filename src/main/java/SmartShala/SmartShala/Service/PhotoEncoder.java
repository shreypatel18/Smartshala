package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.CustomException.InvalidDocumentFormat;
import SmartShala.SmartShala.Entities.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;


public class PhotoEncoder {
    public static Photo encodePhotoToBase64(MultipartFile photo) {
        Photo tempPhoto = new Photo();
        try {
            tempPhoto.setBase64EncodedString(Base64.getEncoder().encodeToString(photo.getBytes()));
            tempPhoto.setFileType(photo.getContentType());
            return tempPhoto;
        } catch (Exception e) {
            throw new InvalidDocumentFormat("error processing photo, photo not uploaded");
        }
    }


}
