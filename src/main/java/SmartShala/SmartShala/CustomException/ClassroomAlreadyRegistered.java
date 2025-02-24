package SmartShala.SmartShala.CustomException;

public class ClassroomAlreadyRegistered extends RuntimeException {

    public ClassroomAlreadyRegistered(String message) {
        super(message);
    }
}
