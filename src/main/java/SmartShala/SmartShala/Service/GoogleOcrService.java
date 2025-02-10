package SmartShala.SmartShala.Service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SocketHandler;

public class GoogleOcrService {

    public static Map<String, String> detectDocumentText(List<byte[]> imageByteArrays) throws IOException {
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
                    System.out.format("Error: %s%n", res.getError().getMessage());
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
        }
        return answers;
    }



    public static int findMatchingFace(byte[] targetImage, List<byte[]> imageList) throws Exception {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            // Convert target image to ByteString
            FaceAnnotation targetFace = getFaceAnnotation(vision, targetImage);
            if (targetFace == null) return -1;

            // Convert all images in the list to ByteStrings
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Feature feature = Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build();

            for (byte[] imgBytes : imageList) {
                Image img = Image.newBuilder().setContent(ByteString.copyFrom(imgBytes)).build();
                AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                        .addFeatures(feature)
                        .setImage(img)
                        .build();
                requests.add(request);
            }

            // Send batch request
            BatchAnnotateImagesRequest batchRequest = BatchAnnotateImagesRequest.newBuilder()
                    .addAllRequests(requests)
                    .build();
            BatchAnnotateImagesResponse batchResponse = vision.batchAnnotateImages(batchRequest);

            // Compare with each image in the array
            List<AnnotateImageResponse> responses = batchResponse.getResponsesList();
            for (int i = 0; i < responses.size(); i++) {
                List<FaceAnnotation> faces = responses.get(i).getFaceAnnotationsList();

                for (FaceAnnotation face : faces) { // Compare with all detected faces
                    if (compareFaceLandmarks(targetFace, face)) {
                        return i; // Return the index of the first match
                    }
                }
            }
        }
        return -1; // No match found
    }

    private static FaceAnnotation getFaceAnnotation(ImageAnnotatorClient vision, byte[] imageBytes) throws Exception {
        ByteString imgBytes = ByteString.copyFrom(imageBytes);
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feature = Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feature)
                .setImage(img)
                .build();

        AnnotateImageResponse response = vision.batchAnnotateImages(List.of(request)).getResponses(0);
        return response.getFaceAnnotationsList().isEmpty() ? null : response.getFaceAnnotationsList().get(0);
    }

    private static boolean compareFaceLandmarks(FaceAnnotation face1, FaceAnnotation face2) {
        // Compare key landmarks (eyes, nose, mouth)
        float threshold = 10.0f; // Tolerance for small variations

        return isClose(face1.getLandmarksList(), face2.getLandmarksList(), threshold);
    }

    private static boolean isClose(List<FaceAnnotation.Landmark> landmarks1, List<FaceAnnotation.Landmark> landmarks2, float threshold) {
        if (landmarks1.size() != landmarks2.size()) return false;

        for (int i = 0; i < landmarks1.size(); i++) {
            FaceAnnotation.Landmark lm1 = landmarks1.get(i);
            FaceAnnotation.Landmark lm2 = landmarks2.get(i);

            float dx = Math.abs(lm1.getPosition().getX() - lm2.getPosition().getX());
            float dy = Math.abs(lm1.getPosition().getY() - lm2.getPosition().getY());
            float dz = Math.abs(lm1.getPosition().getZ() - lm2.getPosition().getZ());

            if (dx > threshold || dy > threshold || dz > threshold) {
                return false;
            }
        }
        return true;
    }
    }


