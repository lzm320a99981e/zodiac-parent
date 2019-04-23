package com.github.lzm320a99981e.component.office.word;

/**
 * Word模板处理拦截器
 */
public interface WordProcessInterceptor {
    /**
     * 开始处理文档
     *
     * @return
     */
    default boolean startProcessDocument(WordProcessContext context) {
        return true;
    }

    /**
     * 处理变量前
     *
     * @return
     */
    default boolean beforeProcessVariable(WordProcessContext context) {
        return true;
    }

    /**
     * 处理变量后
     */
    default void afterProcessVariable(WordProcessContext context) {
    }

    /**
     * 文档处理结束
     */
    default void endProcessDocument(WordProcessContext context) {
    }

    /**
     * 发生错误
     */
    default void onFailure(WordProcessContext context) {
    }
}
