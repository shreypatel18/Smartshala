//package SmartShala.SmartShala.Service;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//public class FaceNet {
//
//    private static final String MODEL_PATH = "C:/20180408-102900/20180408-102900.pb"; // Path to your .pb model file
//
//    // Load the model
//    private static Graph loadModel(String modelPath) throws IOException {
//        byte[] graphDef = Files.readAllBytes(Paths.get(modelPath));
//        Graph graph = new Graph();
//        graph.importGraphDef(graphDef);
//        return graph;
//    }
//
//    // Compare the input image against a list of images and return the index of the matching image
//    public static int compareFaces(byte[] inputImage, List<byte[]> imageList) throws Exception {
//        // Load the FaceNet model
//        Graph graph = loadModel(MODEL_PATH);
//        try (Session session = new Session(graph)) {
//            // Preprocess the input image
//            Tensor inputTensor = preprocessImage(inputImage);
//
//            // Get embeddings for the input image
//            float[] inputEmbeddings = getEmbeddings(session, inputTensor);
//
//            // Compare with images in the list
//            for (int i = 0; i < imageList.size(); i++) {
//                byte[] comparisonImage = imageList.get(i);
//
//                // Preprocess and get embeddings for the comparison image
//                Tensor comparisonTensor = preprocessImage(comparisonImage);
//                float[] comparisonEmbeddings = getEmbeddings(session, comparisonTensor);
//
//                // Calculate similarity (e.g., using cosine similarity)
//                float similarity = calculateCosineSimilarity(inputEmbeddings, comparisonEmbeddings);
//
//                // Return index if similarity exceeds a threshold
//                if (similarity > 0.9) { // Threshold can be adjusted
//                    return i;
//                }
//            }
//        }
//        return -1; // No match found
//    }
//
//    // Preprocess the image: resize, normalize, etc.
//    private static Tensor preprocessImage(byte[] imageBytes) {
//        // Assuming image is resized to 160x160 and normalized.
//        // This step would use a library like OpenCV or any other image processing tool.
//        // For simplicity, we will create a dummy tensor here.
//
//        // In real implementation, you'd need to preprocess the image (resize, normalize, etc.)
//        // For the sake of example, returning a dummy tensor.
//        float[] dummyImageData = new float[160 * 160 * 3]; // 160x160x3 (RGB)
//        return Tensor.create(dummyImageData);
//    }
//
//    // Get embeddings for the image
//    private static float[] getEmbeddings(Session session, Tensor imageTensor) {
//        // The name of the output layer that produces the embeddings
//        String outputLayerName = "embeddings"; // This depends on the specific FaceNet model you're using
//
//        Tensor outputTensor = session.runner()
//                .feed("input_image", imageTensor)
//                .fetch(outputLayerName)
//                .run().get(0);
//
//        // Convert the tensor into a float array (embedding)
//        FloatNdArray embeddings = outputTensor.expect(FloatNdArray.class);
//        return embeddings.toFloatArray();
//    }
//
//    // Calculate cosine similarity between two embeddings
//    private static float calculateCosineSimilarity(float[] embedding1, float[] embedding2) {
//        float dotProduct = 0.0f;
//        float norm1 = 0.0f;
//        float norm2 = 0.0f;
//
//        for (int i = 0; i < embedding1.length; i++) {
//            dotProduct += embedding1[i] * embedding2[i];
//            norm1 += embedding1[i] * embedding1[i];
//            norm2 += embedding2[i] * embedding2[i];
//        }
//
//        norm1 = (float) Math.sqrt(norm1);
//        norm2 = (float) Math.sqrt(norm2);
//
//        return dotProduct / (norm1 * norm2); // Cosine similarity
//    }
//
//    // Main method for testing the FaceNet class
//    public static void main(String[] args) throws Exception {
//        // Example usage:
//        List<byte[]> imageList = new ArrayList<>();
//
//        // Load some images (you need to load actual images here as byte arrays)
//        // For the sake of example, using dummy byte arrays.
//        byte[] image1 = new byte[160 * 160 * 3];  // Dummy image
//        byte[] image2 = new byte[160 * 160 * 3];  // Dummy image
//        imageList.add(image1);
//        imageList.add(image2);
//
//        // Test image (input image)
//        byte[] testImage = new byte[160 * 160 * 3];  // Dummy test image
//
//        // Compare test image with the list of images
//        int matchIndex = compareFaces(testImage, imageList);
//
//        System.out.println("Match found at index: " + matchIndex);
//    }
//}
