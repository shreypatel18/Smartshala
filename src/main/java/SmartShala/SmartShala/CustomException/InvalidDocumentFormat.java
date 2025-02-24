package SmartShala.SmartShala.CustomException;


public class InvalidDocumentFormat extends RuntimeException {
    public InvalidDocumentFormat(String message) {
        super(message);
    }
}
