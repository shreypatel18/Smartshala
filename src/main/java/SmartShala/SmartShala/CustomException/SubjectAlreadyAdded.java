package SmartShala.SmartShala.CustomException;

public class SubjectAlreadyAdded extends RuntimeException {
    public SubjectAlreadyAdded(String message) {
        super(message);
    }
}
