package com.alibaba.excel.read.listener;

import java.util.List;
import java.util.function.Consumer;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.util.ListUtils;

import org.apache.commons.collections4.CollectionUtils;

/**
 * page read listener
 *
 * @author Jiaju Zhuang
 */
public class PageReadListener<T> implements ReadListener<T> {
    /**
     * Single handle the amount of data
     */
    public static int BATCH_COUNT = 100;
    /**
     * Temporary storage of data
     */
    private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * consumer
     */
    private final Consumer<List<T>> consumer;

    private final int pageSize;

    public PageReadListener(Consumer<List<T>> consumer) {
        this(consumer, BATCH_COUNT);
    }

    public PageReadListener(Consumer<List<T>> consumer,int pageSize) {
        this.consumer = consumer;
        this.pageSize = pageSize;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        cachedDataList.add(data);
        if (cachedDataList.size() >= pageSize) {
            consumer.accept(cachedDataList);
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtils.isNotEmpty(cachedDataList)) {
            consumer.accept(cachedDataList);
        }
    }

}
