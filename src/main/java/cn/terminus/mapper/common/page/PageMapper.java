package cn.terminus.mapper.common.page;

import cn.terminus.mapper.entity.Example;
import cn.terminus.mapper.interceptor.PageBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import cn.terminus.mapper.provider.PageProvider;

/**
 * Created by xuguosheng on 16/7/5.
 */
public interface PageMapper<T> {

    @SelectProvider(type = PageProvider.class, method = "dynamicSQL")
    PageBean<T> pagingByExample(@Param("example") Example example, @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);
    @SelectProvider(type = PageProvider.class, method = "dynamicSQL")
    PageBean<T> pagingByEntity(@Param("entity")Object entity, @Param("pageNo")Integer pageNo, @Param("pageSize") Integer pageSize);

}
