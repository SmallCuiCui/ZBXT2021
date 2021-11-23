package org.lxzx.boot.dto;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {
    private long total;//数据总数
    private int page;//总共多少页
    private int current;//当前页数
    private int size;// 每页多少条数据
    private List<T> result;//数据

    public void setData(PageInfo pageInfo) {
        this.setTotal(pageInfo.getTotal());
        this.setPage(pageInfo.getPrePage());
        this.setCurrent(pageInfo.getPageNum());
        this.setSize(pageInfo.getPageSize());
    }
}
