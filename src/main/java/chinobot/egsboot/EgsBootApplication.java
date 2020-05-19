package chinobot.egsboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller("/")
public class EgsBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(EgsBootApplication.class, args);
    }

    @ResponseBody
    @RequestMapping("/")
    public String test(){


        return "hello 服务正常";
    }

}


