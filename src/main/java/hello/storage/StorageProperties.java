package hello.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @vertion:
 * @author:yizhendong
 * @date:2017/8/2 13:54
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * 用来保存文件的文件夹位置
     */
    private  String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
