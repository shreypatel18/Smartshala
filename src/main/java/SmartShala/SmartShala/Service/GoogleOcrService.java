package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.CustomException.GoogleDriveException;
import SmartShala.SmartShala.CustomException.GoogleOcrException;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.SocketHandler;

public class GoogleOcrService {

    public static Map<String, String> detectDocumentText(List<byte[]> imageByteArrays) {
        // List to hold OCR requests
        List<AnnotateImageRequest> requests = new java.util.ArrayList<>();

        // Add each byte array as an image for OCR processing
        for (byte[] imgBytes : imageByteArrays) {
            Image img = Image.newBuilder().setContent(ByteString.copyFrom(imgBytes)).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);
        }

        Map<String, String> answers = new HashMap<>();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            // Process each response
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    continue;
                }
                TextAnnotation annotation = res.getFullTextAnnotation();
                String[] lines = annotation.getText().split("\n");

                if (lines.length > 0) {
                    String answerKey = lines[0].trim(); // The first line is assumed to be 'Ans-1', 'Ans-2', etc.
                    StringBuilder value = new StringBuilder();

                    // Collect all the following lines as content for the answer
                    for (int i = 1; i < lines.length; i++) {
                        value.append(lines[i].trim()).append(" ");
                    }

                    answers.put(answerKey, value.toString().trim());
                }
            }
        } catch (Exception e) {
            throw new GoogleOcrException("issue is detecting text from photos, photos uploaded successfully to google drive");
        }
        return answers;
    }

}


