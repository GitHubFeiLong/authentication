package com.goudong.authentication.server.lang;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 * 本项目的同一分页结果封装
 * @author cfl
 * @version 1.0
 */
@Data
public class PageResult<T> {

    /**
     * 数据总条数
     */
    private Long total;

    /**
     * 数据总页数
     */
    private Long totalPage;

    /**
     * 当前页码
     */
    private Long page;

    /**
     * 每页显示条目数
     */
    private Long size;

    /**
     * 分页结果
     */
    private List<T> content;

    /**
     * 构造空的分页结果
     * @return  分页结果
     */
    public static <T> PageResult<T> ofEmpty() {
        return new PageResult<>(0L, 0L, 1L, 10L, new ArrayList<>(0));
    }

    /**
     * 构造空的分页结果
     * @return  分页结果
     */
    public static <T> PageResult<T> ofEmpty(Long page, Long size) {
        return new PageResult<>(0L, 0L, page, size, new ArrayList<>(0));
    }

    public PageResult(List<T> content) {
        this.content = content;
    }

    public PageResult(Number total, Number totalPage, Number page, Number size, List<T> content) {
        this.total = total.longValue();
        this.totalPage = totalPage.longValue();
        this.page = page.longValue();
        this.size = size.longValue();
        this.content = content;
    }

    public PageResult(Long total, Long totalPage, Long page, Long size, List<T> content) {
        this.total = total;
        this.totalPage = totalPage;
        this.page = page;
        this.size = size;
        this.content = content;
    }
}
