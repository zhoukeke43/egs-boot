package chinobot.egsboot.Service;

import chinobot.egsboot.Component.Common.SqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * form请求通用类
 */

@Service
public class FormRequest {

    @Autowired
    SqlHelper sqlHelper;

    public class FormRequestResult{
        int code;//执行返回代码 0成功，其它各种值未定义 -99通用错误
        String message;//执行结果的说明文字
        Map<String,String> validateinfo;//验证结果，key="summary"表示整体验证，否则是某个字段的验证
        List<SqlHelper.PagedData> datas;//返回数据,可能有多个结果集，为什么统一，插入操作也用这个结果集

    }



    public FormRequestResult doRequest(String opcode, Map<String,String[]> paras) throws Exception{

        FormRequestResult result=new FormRequestResult();

        String opcode2="form.list";
        String sql=" select * from egs_form ";
        result.code=0;
        result.message="成功";
        result.datas=new ArrayList<SqlHelper.PagedData>();
        result.datas.add(sqlHelper.QueryPagedData(sql,paras));

        return result;

    }



}
