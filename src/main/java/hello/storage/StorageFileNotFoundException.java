package hello.storage;

import hello.storage.StorageException;

/**
 * @Description:
 * @vertion:
 * @author:yizhendong
 * @date:2017/8/2 12:20
 */
public class StorageFileNotFoundException extends StorageException {
    public StorageFileNotFoundException(String message){
        super(message);
    }
    public StorageFileNotFoundException(String message, Throwable cause){
        super(message,cause);
    }
}
