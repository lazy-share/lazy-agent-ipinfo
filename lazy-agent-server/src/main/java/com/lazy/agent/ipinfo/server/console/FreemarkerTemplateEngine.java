package com.lazy.agent.ipinfo.server.console;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.Map;

/**
 * <p>
 * freemarker模板引擎
 * </p>
 *
 * @author laizhiyuan
 * @since 2019/9/10.
 */
public class FreemarkerTemplateEngine {

    private static Configuration cfg;

    private static String templateRootPath = "/templates/";

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_28);
        //指定模板文件根路径
        cfg.setClassForTemplateLoading(FreemarkerTemplateEngine.class, templateRootPath);
        cfg.setDefaultEncoding("UTF-8");
        //设置错误的显示方式
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        //不要在FreeMarker中记录它会抛出的异常
        cfg.setLogTemplateExceptions(false);
        //将模板处理期间抛出的未经检查的异常包装到TemplateException -s中
        cfg.setWrapUncheckedExceptions(true);
    }

    /**
     * 生成方法
     *
     * @param templateRelativePath 模板文件路径
     * @param templateData         模板数据
     * @return 输出流
     * @throws Exception
     */
    public static byte[] doGenerator(String templateRelativePath, Map<String, Object> templateData) {

        Writer out = null;
        try {
            Template template = cfg.getTemplate(templateRelativePath);
            out = new StringWriter();
            template.process(templateData, out);
            out.flush();
            String str = out.toString();
            return str.getBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception ignored) {

            }
        }
    }
}
