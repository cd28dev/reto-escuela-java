package cd.dev.compositionorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class CompositionOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompositionOrderApplication.class, args);
    }

}
