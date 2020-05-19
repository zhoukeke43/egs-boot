package chinobot.egsboot.Component.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Configure {

    @Autowired
    private Environment env;
     //获取yam文件的配置
     String getProperty(String key){
         return env.getProperty(key);
     }

    //常用固化配置

     //数据库类型，暂支持两种 mysql,oracle
    @Value("${egs.dbtype}")
    public String dbType="mysql";




}
