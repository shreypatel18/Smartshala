package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.Entities.Photo;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;

public class GoogleDriveService {
    private static final String SERVICE_ACCOUNT_KEY_PATH = "C:\\Users\\shreykumar\\Downloads\\smart-class-management-5bc18fcce30f.json";
    private static final String SMARTSHALA_FOLDER_ID = "14dUpmB6TjMoJyudqqCKY1_Qel-gGLktN";

    public static void uploadImages(MultipartFile[] images, String testId, String classroom, String subject, String enrollmentNo) throws IOException {
        Drive service = getDriveService();
        String subjectFolderId = findFolderIdByName(service, SMARTSHALA_FOLDER_ID, classroom, enrollmentNo, subject);
        if (subjectFolderId == null) {
            System.out.println("⚠️ Subject folder not found!");
            return;
        }
        String testFolderId = createFolder(service, testId, subjectFolderId);
        for (MultipartFile image : images) {
            uploadFile(service, image, testFolderId);
        }
        System.out.println("✅ Images uploaded successfully to folder: " + testId);
    }


    public static void saveImagesToGoogleDrive(MultipartFile[] images, String className, String subjectName, String testName, int studentId) throws IOException {
        Drive service = getDriveService();

        // Step 1: Find existing Class folder inside "SmartShala"
        String classFolderId = findFolderIdByName(service, SMARTSHALA_FOLDER_ID, className);
        if (classFolderId == null) {
            throw new IOException("⚠️ Class folder not found: " + className);
        }

        // Step 2: Find existing Subject folder inside the Class folder
        String subjectFolderId = findFolderIdByName(service, classFolderId, subjectName);
        if (subjectFolderId == null) {
            throw new IOException("⚠️ Subject folder not found: " + subjectName);
        }

        // Step 3: Find existing Test folder inside the Subject folder
        String testFolderId = findFolderIdByName(service, subjectFolderId, testName);
        if (testFolderId == null) {
            throw new IOException("⚠️ Test folder not found: " + testName);
        }

        // Step 4: Create a new folder for studentId inside the Test folder
        String studentFolderId = createFolder(service, String.valueOf(studentId), testFolderId);

        // Step 5: Upload each file inside the Student folder
        for (MultipartFile image : images) {
            uploadFile(service, image, studentFolderId);
        }

        System.out.println("✅ Images uploaded successfully to: SmartShala/" + className + "/" + subjectName + "/" + testName + "/" + studentId);
    }






    private static Drive getDriveService() throws IOException {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(credential.getTransport(), credential.getJsonFactory(), (HttpRequestInitializer) credential)
                .setApplicationName("GoogleDriveService")
                .build();
    }

    private static String findFolderIdByName(Drive service, String parentFolderId, String... folderNames) throws IOException {
        String currentParentId = parentFolderId;
        for (String folderName : folderNames) {
            String query = String.format("'%s' in parents and mimeType='application/vnd.google-apps.folder' and name='%s'", currentParentId, folderName);
            FileList result = service.files().list().setQ(query).setFields("files(id, name)").execute();
            List<File> files = result.getFiles();
            if (files.isEmpty()) return null;
            currentParentId = files.get(0).getId();
        }
        return currentParentId;
    }

    private static String createFolder(Drive service, String folderName, String parentFolderId) throws IOException {
        File newFolder = new File();
        newFolder.setName(folderName);
        newFolder.setMimeType("application/vnd.google-apps.folder");
        newFolder.setParents(Collections.singletonList(parentFolderId));
        return service.files().create(newFolder).setFields("id").execute().getId();
    }

    private static void uploadFile(Drive service, MultipartFile file, String parentFolderId) throws IOException {
        File driveFile = new File();
        driveFile.setName(file.getOriginalFilename());
        driveFile.setParents(Collections.singletonList(parentFolderId));
        AbstractInputStreamContent content = new InputStreamContent(file.getContentType(), file.getInputStream());
        service.files().create(driveFile, content).setFields("id, name").execute();
    }






    public static String createClassFolder(String className) throws IOException {
        Drive service = getDriveService();
        System.out.println("class folder");
        return createFolder(service, className, SMARTSHALA_FOLDER_ID);
    }

    public static String createSubjectFolder(String className, String subjectName) throws IOException {
        Drive service = getDriveService();
        String classFolderId = findFolderIdByName(service, SMARTSHALA_FOLDER_ID, className);
        if (classFolderId == null) {
            System.out.println("⚠️ Class folder not found! Creating now...");
            classFolderId = createFolder(service, className, SMARTSHALA_FOLDER_ID);
        }
        return createFolder(service, subjectName, classFolderId);
    }




    public static String createTestFolder(String className, String subjectName, String testName) throws IOException {
        Drive service = getDriveService();

        // Step 1: Find Class folder (must exist)
        String classFolderId = findFolderIdByName(service, SMARTSHALA_FOLDER_ID, className);
        if (classFolderId == null) {
            throw new IOException("⚠️ Class folder not found: " + className);
        }

        // Step 2: Find Subject folder (must exist)
        String subjectFolderId = findFolderIdByName(service, classFolderId, subjectName);
        if (subjectFolderId == null) {
            throw new IOException("⚠️ Subject folder not found: " + subjectName);
        }

        // Step 3: Create Test folder inside the Subject folder with name = testName
        String testFolderId = createFolder(service, testName, subjectFolderId);

        System.out.println("✅ Test folder created: SmartShala/" + className + "/" + subjectName + "/" + testName);
        return testFolderId; // Returns the ID of the created folder
    }

    private static Photo downloadFile(Drive service, String fileId) throws IOException {
        File fileMetadata = service.files().get(fileId).setFields("mimeType").execute();
        String mimeType = fileMetadata.getMimeType();
        Photo photo = new Photo();
        photo.setFileType(mimeType);
        try (InputStream inputStream = service.files().get(fileId).executeMediaAsInputStream()) {
            photo.setBase64EncodedString(Base64.getEncoder().encodeToString(inputStream.readAllBytes()));
            return photo;
        } catch (GoogleJsonResponseException e) {
            System.out.println("⚠️ Error downloading file: " + e.getDetails());
            return null;
        }
    }

    private static List<Photo> fetchJpgFilesAsByteArray(Drive service, String folderId) throws IOException {
        String query = String.format("'%s' in parents and mimeType='image/jpeg'", folderId);
        FileList result = service.files().list().setQ(query).setFields("files(id, name)").execute();
        List<Photo> imagesArrayList = new ArrayList<>();
        for (File file : result.getFiles()) {
            Photo photo = downloadFile(service, file.getId());
            if (photo != null) imagesArrayList.add(photo);
        }
        return imagesArrayList;
    }

    public static List<Photo> get(String className, String subjectName, String testName, int studentId) throws IOException {
        Drive service = getDriveService();
        String classFolderId = findFolderIdByName(service, SMARTSHALA_FOLDER_ID, className);
        if (classFolderId == null) return new ArrayList<>();

        String subjectFolderId = findFolderIdByName(service, classFolderId, subjectName);
        if (subjectFolderId == null) return new ArrayList<>();

        String testFolderId = findFolderIdByName(service, subjectFolderId, testName);
        if (testFolderId == null) return new ArrayList<>();

        String studentFolderId = findFolderIdByName(service, testFolderId, String.valueOf(studentId));
        if (studentFolderId == null) return new ArrayList<>();

        return fetchJpgFilesAsByteArray(service, studentFolderId);
    }



}