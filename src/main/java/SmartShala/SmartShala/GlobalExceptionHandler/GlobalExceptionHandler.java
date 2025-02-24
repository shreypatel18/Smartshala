package SmartShala.SmartShala.GlobalExceptionHandler;

import SmartShala.SmartShala.CustomException.*;
import SmartShala.SmartShala.Entities.Classroom;
import org.apache.poi.util.DocumentFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExists.class)
    public ResponseEntity<String> entityAlreadyExists(EntityAlreadyExists entityAlreadyExists) {
        return ResponseEntity.internalServerError().body(entityAlreadyExists.getMessage());
    }

    @ExceptionHandler(DocumentFormatException.class)
    public ResponseEntity<String> DocumentFormatException(DocumentFormatException documentFormatException) {
        return ResponseEntity.internalServerError().body(documentFormatException.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> IllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.internalServerError().body(illegalArgumentException.getMessage());

    }

    @ExceptionHandler(InvalidExcelDataException.class)
    public ResponseEntity<String> InvalidExcelDataException(InvalidExcelDataException invalidExcelDataException) {
        return ResponseEntity.internalServerError().body(invalidExcelDataException.getMessage() + " no records are inserted");
    }

    @ExceptionHandler(ClassRoomNotFoundException.class)
    public ResponseEntity<String> ClassroomNotFoundException(ClassRoomNotFoundException classRoomNotFoundException) {
        return ResponseEntity.internalServerError().body(classRoomNotFoundException.getMessage());
    }

    @ExceptionHandler(ClassroomAlreadyRegistered.class)
    public ResponseEntity<String> ClassroomAlreadyRegisteredException(ClassroomAlreadyRegistered classroomAlreadyRegistered) {
        return ResponseEntity.internalServerError().body(classroomAlreadyRegistered.getMessage());
    }

    @ExceptionHandler(GoogleDriveException.class)
    public ResponseEntity<String> googleDriveException(GoogleDriveException googleDriveException) {
        return ResponseEntity.internalServerError().body(googleDriveException.getMessage());
    }

    @ExceptionHandler(SubjectAlreadyAdded.class)
    public ResponseEntity<String> subjectAlreadyAddedException(SubjectAlreadyAdded subjectAlreadyAdded) {
        return ResponseEntity.internalServerError().body(subjectAlreadyAdded.getMessage());
    }

    @ExceptionHandler(SubjectNotAssigned.class)
    public ResponseEntity<String> SubjectNotAssigned(SubjectNotAssigned subjectNotAssigned) {
        return ResponseEntity.internalServerError().body(subjectNotAssigned.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));  // Join all error messages with commas
        return ResponseEntity.internalServerError().body(errorMessage);
    }

    @ExceptionHandler(TestException.class)
    public ResponseEntity<String> testException(TestException testException) {
        return ResponseEntity.internalServerError().body(testException.getMessage());
    }

    @ExceptionHandler(GoogleOcrException.class)
    public ResponseEntity<String> googleOcrException(GoogleOcrException googleOcrException) {
        return ResponseEntity.internalServerError().body(googleOcrException.getMessage());
    }

    @ExceptionHandler(AuthorisationException.class)
    public ResponseEntity<String> authorisationException(AuthorisationException authorisationException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authorisationException.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return ResponseEntity.internalServerError().body(entityNotFoundException.getMessage());
    }

}
