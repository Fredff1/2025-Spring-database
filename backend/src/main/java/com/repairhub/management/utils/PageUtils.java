package com.repairhub.management.utils;

import java.util.Collections;
import java.util.List;

public class PageUtils {

    /**
     * 对给定的 List 进行分页
     *
     * @param <T>   列表元素类型
     * @param list  原始列表（已按需要的顺序排序）
     * @param page  当前页（从 1 开始）
     * @param limit 每页大小
     * @return 对应页的子列表，如果 page 超出范围则返回空列表
     */
    public static <T> List<T> paginate(List<T> list, int page, int limit) {
        if (page < 1 || limit < 1 || list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        int total = list.size();
        int start = (page - 1) * limit;
        if (start >= total) {
            return Collections.emptyList();
        }
        int end = Math.min(start + limit, total);
        return list.subList(start, end);
    }

    public static long calculateOffset(int pageNum,int pageSize){
        long offset = (pageNum - 1) * pageSize;
        return offset;
    }
}
