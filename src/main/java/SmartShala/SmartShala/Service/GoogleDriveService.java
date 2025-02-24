package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.CustomException.GoogleDriveException;
import SmartShala.SmartShala.CustomException.TestException;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleDriveService {
    private static final String SERVICE_ACCOUNT_KEY_PATH = "C:\\Users\\shreykumar\\Downloads\\smart-class-management-5bc18fcce30f.json";
    private static final String SMARTSHALA_FOLDER_ID = "14dUpmB6TjMoJyudqqCKY1_Qel-gGLktN";

    private static final Logger LOGGER = Logger.getLogger(GoogleDriveService.class.getName());

    public static void saveImagesToGoogleDrive(MultipartFile[] images, String className, String subjectCode, String testName, int studentId) {

        Drive service = getDriveService();

        // Step 1: Find existing Class folder inside "SmartShala"
        String classFolderId = findFolderIdByName(service, SMARTSHALA_FOLDER_ID, className);
        if (classFolderId == null) {
            throw new GoogleDriveException("class folder does not exist for classname " + className);
        }

        // Step 2: Find existing Subject folder inside the Class folder
        String subjectFolderId = findFolderIdByName(service, classFolderId, subjectCode);
        if (subjectFolderId == null) {
            throw new GoogleDriveException("subject folder does not exist for subject for code " + subjectCode);
        }

        // Step 3: Find existing Test folder inside the Subject folder
        String testFolderId = findFolderIdByName(service, subjectFolderId, testName);
        if (testFolderId == null) {
            throw new GoogleDriveException("test folder does not exist for test " + testName);
        }

        // Step 4: Create a new folder for studentId inside the Test folder
        String studentFolderId = createFolder(service, String.valueOf(studentId), testFolderId);

        // Step 5: Upload each file inside the Student folder
        for (MultipartFile image : images) {
            uploadFile(service, image, studentFolderId);
        }

    }


    //returns the drive services
    private static Drive getDriveService() {


        //handles exception if google api key file is not loaded
        try {
            //Fetches Credentials and define scope like DRIVE scope means full access to the drive
            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_PATH))
                    .createScoped(Collections.singleton(DriveScopes.DRIVE));
            //getTransport to communicate with google servers , json factory to parse json responses from google , and credentials to validate each request
            return new Drive.Builder(credential.getTransport(), credential.getJsonFactory(), (HttpRequestInitializer) credential)
                    .setApplicationName("GoogleDriveService")
                    .build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error accessing Google Drive: unable to fetch google api credentials");
            throw new GoogleDriveException("Unable to process data google drive is not accessible");
        }

    }

    private static String findFolderIdByName(Drive service, String parentFolderId, String... folderNames) {
        String currentParentId = parentFolderId;

        try {
            for (String folderName : folderNames) {
                String query = String.format("'%s' in parents and mimeType='application/vnd.google-apps.folder' and name='%s'", currentParentId, folderName);
                FileList result = service.files().list().setQ(query).setFields("files(id, name)").execute();
                List<File> files = result.getFiles();
                if (files.isEmpty()) return null;
                currentParentId = files.get(0).getId();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "unable to find the folder by name in google drive exception occured as: " + e.getMessage());
            throw new GoogleDriveException("issue in finding the folder in google drive");
        }
        return currentParentId;

    }

    private static String createFolder(Drive service, String folderName, String parentFolderId) {
        File newFolder = new File();
        newFolder.setName(folderName);
        newFolder.setMimeType("application/vnd.google-apps.folder");
        newFolder.setParents(Collections.singletonList(parentFolderId));

        //handles if any exception in creating folder
        try {
            return service.files().create(newFolder).setFields("id").execute().getId();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to create folder: " + folderName + " exception occured with message " + e.getMessage());
            throw new GoogleDriveException("Something went wrong Unable to create Google Drive");
        }
    }

    private static void uploadFile(Drive service, MultipartFile file, String parentFolderId) {
        File driveFile = new File();
        driveFile.setName(file.getOriginalFilename());
        driveFile.setParents(Collections.singletonList(parentFolderId));
        try {
            AbstractInputStreamContent content = new InputStreamContent(file.getContentType(), file.getInputStream());
            service.files().create(driveFile, content).setFields("id, name").execute();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "something went wrong while uploading images message :" + e.getMessage());
            throw new GoogleDriveException("unable to upload file to google drive something went wrong on server side");
        }
    }


    public static void createClassFolder(String className) {
        Drive service = getDriveService();
        createFolder(service, className, SMARTSHALA_FOLDER_ID);
    }

    public static void createSubjectFolder(String className, String subjectName) throws IOException {
        Drive service = getDriveService();
        String classFolderId = findFolderIdByName(service, SMARTSHALA_FOLDER_ID, className);
        if (classFolderId != null) {
            createFolder(service, subjectName, classFolderId);
            return;
        }
        throw new GoogleDriveException("class folder for class " + className + " not found");
    }


    public static void createTestFolder(String className, String subjectCode, String testName) {
        Drive service = getDriveService();
        String classFolderId = findFolderIdByName(service, SMARTSHALA_FOLDER_ID, className);
        if (classFolderId == null) {
            throw new GoogleDriveException("some issue in google drive class folder to store test data not found for class " + className);
        }
        String subjectFolderId = findFolderIdByName(service, classFolderId, subjectCode);
        if (subjectFolderId == null) {
            throw new GoogleDriveException("some issue in google drive Subject folder not found for subject " + subjectCode);
        }
        createFolder(service, testName, subjectFolderId);
    }

    private static Photo downloadFile(Drive service, String fileId) throws Exception {
        File fileMetadata = service.files().get(fileId).setFields("mimeType").execute();
        String mimeType = fileMetadata.getMimeType();
        Photo photo = new Photo();
        photo.setFileType(mimeType);
        InputStream inputStream = service.files().get(fileId).executeMediaAsInputStream();
        photo.setBase64EncodedString(Base64.getEncoder().encodeToString(inputStream.readAllBytes()));
        return photo;
    }

    private static List<Photo> fetchJpgFilesAsByteArray(Drive service, String folderId) throws Exception {
        String query = String.format("'%s' in parents and mimeType='image/jpeg'", folderId);
        FileList result = service.files().list().setQ(query).setFields("files(id, name)").execute();
        List<Photo> imagesArrayList = new ArrayList<>();
        for (File file : result.getFiles()) {
            Photo photo = downloadFile(service, file.getId());
            if (photo != null) imagesArrayList.add(photo);
        }
        return imagesArrayList;
    }

    public static List<Photo> get(String className, String subjectCode, String testName, int studentId) {
        Drive service = getDriveService();
        String classFolderId = findFolderIdByName(service, SMARTSHALA_FOLDER_ID, className);
        if (classFolderId == null) {
            new GoogleDriveException("unable to find class folder for class: " + className);
        }
        String subjectFolderId = findFolderIdByName(service, classFolderId, subjectCode);

        if (subjectFolderId == null) {
            new GoogleDriveException("unable to find subject folder for subject: " + subjectCode);
        }

        String testFolderId = findFolderIdByName(service, subjectFolderId, testName);
        if (testFolderId == null) {
            new GoogleDriveException("unable to find test folder for test: " + testName);
        }

        String studentFolderId = findFolderIdByName(service, testFolderId, String.valueOf(studentId));
        if (studentFolderId == null) {
            new GoogleDriveException("unable to find student folder for student: " + studentId);
        }

        try {
            return fetchJpgFilesAsByteArray(service, studentFolderId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "something went wrong while fetching photos message: " + e.getMessage());
            throw new GoogleDriveException("issue in fetching photos");
        }
    }


}