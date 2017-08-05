package hello;

import hello.storage.StorageProperties;
import hello.storage.StorageService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Description:
 * @vertion:
 * @author:yizhendong
 * @date:2017/8/1 17:00
 */
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Example {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class,args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }


}
