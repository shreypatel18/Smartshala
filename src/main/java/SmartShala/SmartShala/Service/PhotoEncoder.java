package SmartShala.SmartShala.Service;
import SmartShala.SmartShala.Entities.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;


public class PhotoEncoder {
    public static Photo encodePhotoToBase64(MultipartFile photo) throws IOException {
        if (photo == null) {
            return null; // Handle null case if needed
        }

        Photo tempPhoto = new Photo();
        tempPhoto.setBase64EncodedString(Base64.getEncoder().encodeToString(photo.getBytes()));
        tempPhoto.setFileType(photo.getContentType());
        return tempPhoto;
    }


}
