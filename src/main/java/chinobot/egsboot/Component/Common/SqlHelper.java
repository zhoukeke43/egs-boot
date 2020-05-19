package chinobot.egsboot.Component.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 数据库访问接口类
 * 用jdbc templadte
 */
@Component
public class SqlHelper {

    public class PagedData
    {
        int pagesize=15;
        int pageindex=1;
        int recordcount=0;
        int pagecount=1;
        List<Map<String,Object>> data;
    }


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    Configure configure;



    //执行sql查询,返回通用list<map>类型
    public  PagedData QueryPagedData(String sql,Map<String,String[]> paras) throws Exception{

        PagedData pagedData=new PagedData();
        if(paras.containsKey("pageindex")){
            pagedData.pageindex= Integer.parseInt(paras.get("pageindex")[0]);
        }
        if(paras.containsKey("pagesize")) {
            pagedData.pagesize= Integer.parseInt(paras.get("pagesize")[0]);
        }

        //参数处理，从请求端讲，所有类型，都是string
        //根据请求参数，自动识别为通用数据库类型，uuid,int,varchar,date,decimal(18,2) 5种
        String sqlbase="";
        sqlbase=sql;
        //给sql加参数
        String value;
        for(String key:paras.keySet()){
            //egs_开头的是自动添加的参数
            if(key.startsWith("egs_")) {

                //可选参数与必选参数，mybatis里，是否有值在动态添加条件
                //必选参数 sql里用{{}}标识，
                //可选参数 sql里用{}标识，与mybatis一致
                value = paras.get(key)[0];
                if(value !=null && !value.isEmpty()) {
                    sqlbase.replace("{{" + key.substring(4) + "}}", "'" + value + "'");
                    sqlbase.replace("{" + key.substring(4) + "}", "'" + value + "'");
                }
            }
        }

        //如果必选参数不全，则直接报错
        if(sqlbase.contains("{{") || sqlbase.contains("}}")){
            String paraname=sqlbase.substring(sqlbase.indexOf("{{"));
            paraname=paraname.substring(0,paraname.indexOf("}}"));
            throw new Exception("sql 要求的参数没有提供或提供的值为空");
        }
        //可选参数处理，如果没有传这个参数或为空
        //规则，如果还{aa} 存在，则把{aa}所在的行全部删除掉，可选参数必须以为行为单位
        String[] sqlbases=sqlbase.split("\n");
        sqlbase="";
        for(String sqlitem:sqlbases){
            //防止把必选参数替换掉，安全起见。也保留在sql中，sql报错，也比越权查询好
            if(!(sqlitem.contains("{") && sqlitem.contains("}")) || sqlitem.contains("{{") || sqlitem.contains("}}")){
                sqlbase +=" "+sqlitem+" \n";
            }
        }


        String sqlcount=" select count(1) total  from  ( \n "+sqlbase+" \n ) tablecount ";
        int recordcount=jdbcTemplate.queryForObject(sqlcount,Integer.class);
        pagedData.recordcount=recordcount;
        pagedData.pagecount=recordcount==0?0:(recordcount-1)/pagedData.pagesize+1;

        //开始位置
        int offset = (pagedData.pageindex -1)*pagedData.pagesize;
        String sqldata="";
        if(configure.dbType.equals("mysql")){
            sqldata=sqlbase +" \n "+ " limit " +offset+" , "+(offset+pagedData.pagesize-1) ;
        }
        pagedData.data=jdbcTemplate.queryForList(sqldata);

        //所有字段名转小写。  a_b_c是推荐标准。oracel大小，mysql小写。但小写可读性更好
        //暂未实现，需要页面绑定时注意大小写

        return pagedData;
    }



}
