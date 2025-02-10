package SmartShala.SmartShala.Test;


import SmartShala.SmartShala.Entities.Photo;
import SmartShala.SmartShala.Service.GoogleDriveService;
import SmartShala.SmartShala.Service.GoogleOcrService;
import SmartShala.SmartShala.Service.MailService;
import SmartShala.SmartShala.Service.PhotoEncoder;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static java.nio.file.Paths.get;

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

//    @GetMapping("getText")
//    Map<String , String> sendImage() throws IOException {
//       return GoogleOcrService.detectDocumentText("C:\\Users\\shreykumar\\OneDrive\\Desktop\\January\\demo resources\\id4.jpg");
//    }

//    @GetMapping("/driveTest")
//    Map<String, String> drive() throws IOException {
////        List<byte[]> images = GoogleDriveService.get("class-12","220130107101","maths","test1");
////        for(byte[] m:images){
////            System.out.println(m);
////        }
//       return GoogleOcrService.detectDocumentText(;
//    }

//    @GetMapping("viewAnswerSheetPhotos")
//    List<Photo> viewAnswerSheetPhotos(@RequestParam int studentId, @RequestParam int testId) throws IOException {
//        List<Photo> photos = ;
//        return photos;
//    }

    @GetMapping("getText")
    Map<String,String> getOcrDone(@RequestParam MultipartFile[] images) throws IOException {
        List<byte[]> temp = new ArrayList<>();
        for(MultipartFile file : images) {
          temp.add(file.getBytes());
        }
        return GoogleOcrService.detectDocumentText(temp);
    }

    @GetMapping("face")
    int getImages(@RequestParam MultipartFile[] images, @RequestParam MultipartFile image) throws Exception {
        List<byte[]> imgs = new ArrayList<>();
       for(int i=0; i<images.length; i++){
           imgs.add(images[i].getBytes());
       }
       return 1;
    }
}
