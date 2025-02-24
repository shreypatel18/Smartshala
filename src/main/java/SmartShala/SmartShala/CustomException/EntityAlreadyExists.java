package SmartShala.SmartShala.CustomException;

public class EntityAlreadyExists extends RuntimeException {

    public EntityAlreadyExists(String message) {
        super(message);
    }
}
