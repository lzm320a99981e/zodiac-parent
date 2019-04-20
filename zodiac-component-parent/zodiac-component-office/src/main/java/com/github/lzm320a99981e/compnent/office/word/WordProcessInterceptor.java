package com.github.lzm320a99981e.compnent.office.word;

/**
 * Word模板处理拦截器
 */
public interface WordProcessInterceptor {
    /**
     * 开始处理文档
     *
     * @return
     */
    default boolean startProcessDocument() {
        return true;
    }

    /**
     * 处理变量前
     *
     * @return
     */
    default boolean beforeProcessVariable() {
        return true;
    }

    /**
     * 处理变量后
     */
    void afterProcessVariable();

    /**
     * 文档处理结束
     */
    void endProcessDocument();

    /**
     * 发生错误
     */
    void failure();
}
