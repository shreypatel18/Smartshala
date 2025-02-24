package SmartShala.SmartShala.Service;


import SmartShala.SmartShala.CustomException.EntityNotFoundException;
import SmartShala.SmartShala.Entities.Photo;
import SmartShala.SmartShala.Repository.PhotoRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PhotoService {


    @Autowired
    PhotoRepository photoRepository;


    public void uploadPhoto(MultipartFile multipartFile, int id) {
        if (multipartFile == null) return;
        Photo photo = PhotoEncoder.encodePhotoToBase64(multipartFile);
        photo.setId(id);
        photoRepository.save(photo);
    }

    public Photo getPhoto(int id) {
        return photoRepository.findById(id).orElse(null);
    }

}
