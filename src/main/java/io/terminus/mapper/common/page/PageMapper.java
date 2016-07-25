package io.terminus.mapper.common.page;

import io.terminus.mapper.entity.Example;
import io.terminus.mapper.interceptor.PageBean;
import io.terminus.mapper.provider.PageProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * Created by xgs on 16/7/5.
 * 该类是提供分页方法,凡在该接口下的方法, 都会被拦截器拦截, 进行分页操作
 */
public interface PageMapper<T> {

    /**
     * 根据example对象进行分页查询
     * @param example
     * @param pageNo 若为null, 设置为1
     * @param pageSize 若为null, 设置为20
     * @return
     */
    @SelectProvider(type = PageProvider.class, method = "dynamicSQL")
    PageBean<T> pagingByExample(@Param("example") Example example, @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    /**
     * 根据实体类封装的数据进行分页查询
     * @param entity
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SelectProvider(type = PageProvider.class, method = "dynamicSQL")
    PageBean<T> pagingByEntity(@Param("entity")Object entity, @Param("pageNo")Integer pageNo, @Param("pageSize") Integer pageSize);

}
