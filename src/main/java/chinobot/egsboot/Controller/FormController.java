package chinobot.egsboot.Controller;

import chinobot.egsboot.Service.FormRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/form")
public class FormController {

    @Autowired
    FormRequest formRequest;


    @ResponseBody
    @RequestMapping("/request")
    public  Object doRequest(HttpServletRequest request) throws Exception{



        return formRequest.doRequest("form.list",request.getParameterMap());

    }

}
