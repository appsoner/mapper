package cn.terminus.mapper.interceptor;

import io.terminus.common.model.Paging;

import java.util.*;

/**
 * Created by xuguosheng on 16/7/21.
 */
public class PageBean<T> extends ArrayList{
    private static final long serialVersionUID = 755183539178076901L;

    private Long total;

    private List<T> data;

    private Paging<T> paging = new Paging<T>();

    public PageBean() {
    }

    public PageBean(Long total, List<T> data) {
        this.data = data;
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
        this.paging.setData(data);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
        this.paging.setTotal(total);
    }

    public boolean isEmpty() {
        return Objects.equals(0L, total) || data == null || data.isEmpty();
    }

    @SuppressWarnings("all")
    public static <T> Paging<T> empty(Class<T> clazz) {
        List<T> emptyList = Collections.emptyList();
        return new Paging<T>(0L, emptyList);
    }

    public static <T> Paging<T> empty() {
        return new Paging<T>(0L, Collections.<T>emptyList());
    }

    public Paging<T> getPaging(){
        return this.paging;
    }


}
