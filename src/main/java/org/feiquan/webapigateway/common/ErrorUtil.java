package org.feiquan.webapigateway.common;

import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 常用错误处理工具
 * @author junwei.jjw
 * @date 2019-07-02
 */
public class ErrorUtil {
    /**
     * 输出异常信息
     * @param e 异常
     * @param message 消息，支持替占位符
     * @param args 结果变量
     */
    public static void log(Logger logger, Throwable e, String message, Object...args) {
        log(logger, e, isLowLevelError(e), message, args);
    }

    /**
     * 判断异常是否是低级错误
     * @param e
     * @return
     */
    public static boolean isLowLevelError(Throwable e) {
        return e instanceof NullPointerException
            || e instanceof IndexOutOfBoundsException
            || e instanceof ClassCastException;
    }

    /**
     * 输出异常错误
     * @param logger
     * @param e
     * @param logStackByWarn
     * @param message
     * @param args
     */
    public static void log(Logger logger, Throwable e, boolean logStackByWarn, String message, Object...args) {
        logger.warn(appendError(message, e), args);
        if (logStackByWarn) {
            logger.warn("error stack: ", e);
        } else {
            logger.debug("error stack: ", e);
        }
    }

    private static String appendError(String message, Throwable e) {
        return message + " error: " + e.getClass().getSimpleName()
            + " msg: " + e.getMessage();
    }

    /**
     * 从异常中获取错误消息
     * @param error
     * @return
     */
    public static String getMessage(Throwable error) {
        Throwable cause = error;
        String message;
        do {
            message = cause.getMessage();
            cause = cause.getCause();
        } while (message == null && cause != null);
        if (message == null) {
            message = "Unknown error. error: " + error.getClass().getName();
        }
        return message;
    }

    /**
     * 连接错误消息
     * @param message
     * @param e
     * @return
     */
    public static String joinMessage(String message, Throwable e) {
        return message + " 错误：" + getMessage(e);
    }

    /**
     * 将错误消息进行完整的构建
     * @param e
     * @return
     */
    public static String getStackTrace(Throwable e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
