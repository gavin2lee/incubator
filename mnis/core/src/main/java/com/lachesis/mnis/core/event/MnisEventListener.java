package com.lachesis.mnis.core.event;

import org.springframework.context.ApplicationListener;

public interface MnisEventListener<T extends MnisEvent> extends ApplicationListener<T> {

}
