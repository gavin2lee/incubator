package net.jeeshop.core.freemarker;

import com.google.common.collect.ImmutableMap;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by dylan on 15-5-10.
 */
public class FreemarkerTest {
    @Test
    public void test() throws Exception{
        Configuration configuration = new Configuration();
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate("string", "hello, ${bean.name}");
        configuration.setTemplateLoader(templateLoader);
        Template template = configuration.getTemplate("string");
        template.process(ImmutableMap.of("bean", new Bean()), new PrintWriter(System.out));
    }
}
