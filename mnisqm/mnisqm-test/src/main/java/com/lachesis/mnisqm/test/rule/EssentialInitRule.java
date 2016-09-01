package com.lachesis.mnisqm.test.rule;

import java.util.TimeZone;

import org.joda.time.DateTimeZone;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.lachesis.mnisqm.configuration.SiteConfiguration;

/**
 * @author Paul Xu
 * @since 1.0.0
 */
public class EssentialInitRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        SiteConfiguration.loadConfiguration();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        DateTimeZone.setDefault(DateTimeZone.UTC);
        return base;
    }
}
