package hello.storage;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @Description:
 * @vertion:
 * @author:yizhendong
 * @date:2017/8/2 11:53
 */

public interface StorageService {

    void init();

    void store(MultipartFile file);

    Stream<java.nio.file.Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();





}
