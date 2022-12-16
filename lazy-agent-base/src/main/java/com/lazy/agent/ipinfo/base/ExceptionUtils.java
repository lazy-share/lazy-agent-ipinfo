package com.lazy.agent.ipinfo.base;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>
 *     异常工具
 * </p>
 *
 * @author laizhiyuan
 * @since 2018/12/27.
 */
public class ExceptionUtils {

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

}
