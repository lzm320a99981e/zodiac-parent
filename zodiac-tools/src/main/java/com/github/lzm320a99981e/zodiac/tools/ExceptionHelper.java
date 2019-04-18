package com.github.lzm320a99981e.zodiac.tools;

/**
 * 异常辅助类
 */
public class ExceptionHelper {
    /**
     * 包装运行时异常
     *
     * @param e
     * @return
     */
    public static RuntimeException wrappedRuntimeException(Exception e) {
        if (RuntimeException.class.isAssignableFrom(e.getClass())) {
            return (RuntimeException) e;
        }
        return new RuntimeException(e);
    }

    /**
     * 重新抛出运行时异常
     *
     * @param e
     */
    public static void rethrowRuntimeException(Exception e) {
        throw wrappedRuntimeException(e);
    }
}
