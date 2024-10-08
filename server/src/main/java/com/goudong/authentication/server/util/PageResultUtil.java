package com.goudong.authentication.server.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.zhxu.bs.SearchResult;
import com.goudong.authentication.server.lang.PageResultConverter;
import com.goudong.authentication.server.rest.req.search.BasePage;
import com.goudong.authentication.server.lang.PageResult;

public class PageResultUtil {

    private static SearchResult2PageResultConverter<?> converter;

    private PageResultUtil() {

    }

    public static SearchResult2PageResultConverter<?> getConverter() {
        if (converter != null) {
            return converter;
        }
        synchronized (PageResultUtil.class) {
            if (converter != null) {
                return converter;
            }
            converter = new SearchResult2PageResultConverter<Object>();
            return converter;
        }
    }

    /**
     * 转分页结果
     * @param source
     * @param basePage
     * @return
     */
    public static PageResult<BasePage> convert(SearchResult<?> source, BasePage basePage) {
        PageResult<BasePage> pageResult = (PageResult<BasePage>) getConverter().basicConvert(source, null);
        pageResult.setPage(basePage.getPage().longValue() + 1);
        pageResult.setSize(basePage.getSize().longValue());
        // 总页数
        Long totalPage = pageResult.getTotal() == 0 ? 0L : (long)Math.floor(pageResult.getTotal() / basePage.getSize()) + 1;
        pageResult.setTotalPage(totalPage);
        return pageResult;
    }

    /**
     * 转分页结果
     * @param source
     * @param basePage
     * @return
     */
    public static PageResult convert(SearchResult<?> source, BasePage basePage, Class resultClazz) {
        PageResult<?> pageResult = getConverter().basicConvert(source, resultClazz);
        pageResult.setPage(basePage.getPage().longValue() + 1);
        pageResult.setSize(basePage.getSize().longValue());
        // 总页数
        Long totalPage = pageResult.getTotal() == 0 ? 0L : (long)Math.floor(pageResult.getTotal() / basePage.getSize()) + 1;
        pageResult.setTotalPage(totalPage);
        return pageResult;
    }

    public static class SearchResult2PageResultConverter<E> implements PageResultConverter<SearchResult<?>, PageResult<E>, E> {
        @Override
        public PageResult<E> basicConvert(SearchResult<?> source, Class<E> tClazz) {

            return new PageResult<E>(source.getTotalCount().longValue(),
                    0L,
                    0L,
                    0L,
                    BeanUtil.copyToList(source.getDataList(), tClazz, CopyOptions.create()));
        }

        @Override
        public PageResult<E> convert(Object source, Class<E> tClazz) {
            return null;
        }
    }
}
