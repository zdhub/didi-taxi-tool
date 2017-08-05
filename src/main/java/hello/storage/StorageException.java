package hello.storage;

/**
 * @Description:
 * @vertion:
 * @author:yizhendong
 * @date:2017/8/2 14:03
 */
public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
