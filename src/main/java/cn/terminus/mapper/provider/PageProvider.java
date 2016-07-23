package cn.terminus.mapper.provider;

import cn.terminus.mapper.mapperhelper.MapperTemplate;
import org.apache.ibatis.mapping.MappedStatement;
import cn.terminus.mapper.mapperhelper.MapperHelper;
import cn.terminus.mapper.mapperhelper.SqlHelper;

/**
 * Created by xuguosheng on 16/7/5.
 */
public class PageProvider extends MapperTemplate {
    public PageProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 分页查询
     * @param ms
     * @return
     */
    public String pagingByExample(MappedStatement ms){
        Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append("<if test=\"example.distinct\">distinct</if>");
        //支持查询指定列
        sql.append(SqlHelper.examplePagingSelectColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.examplePagingWhereClause());
        sql.append(SqlHelper.examplePagingOrderBy(entityClass));
        return sql.toString();
    }


    public String pagingByEntity(MappedStatement ms){
        Class<?> entityClass = getEntityClass(ms);
        //修改返回值类型为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.whereAllIfColumns(entityClass, isNotEmpty()));
        sql.append(SqlHelper.pagingOrderByDefault(entityClass));
        return sql.toString();
    }

    public String dynamicSQL(MappedStatement ms){
        return "dynamicSQL";
    }


}
