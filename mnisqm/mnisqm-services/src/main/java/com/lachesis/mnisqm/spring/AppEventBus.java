package com.lachesis.mnisqm.spring;

import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;

/**
 * @author Paul Xu
 * @since 1.0.0
 */
//@Configuration
public class AppEventBus {
    //@Bean
    public AsyncEventBus asyncEventBus() {
        return new AsyncEventBus(Executors.newCachedThreadPool());
    }
}
