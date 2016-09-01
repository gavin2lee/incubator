package com.lachesis.mnisqm.core;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Paul Xu
 * @since 1.0.0
 */
public class NotificationBroadcaster implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8183722683426793950L;

	static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface BroadcastListener {
        void broadcastNotification(AbstractNotification notification);

        void removeNotification(AbstractNotification notification);
    }

    private static Collection<AbstractNotification> globalNotifications = new ArrayList<>();

    private static LinkedList<BroadcastListener> listeners = new LinkedList<>();

    public static synchronized void register(BroadcastListener listener) {
        listeners.add(listener);
        for (AbstractNotification notification : globalNotifications) {
            listener.broadcastNotification(notification);
        }
    }

    public static void removeGlobalNotification(Class<?> notificationCls) {
        Iterator<AbstractNotification> iter = globalNotifications.iterator();
        while (iter.hasNext()) {
            final AbstractNotification notification = iter.next();
            if (notification.getClass() == notificationCls) {
                iter.remove();
                for (final BroadcastListener listener : listeners)
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            listener.removeNotification(notification);
                        }
                    });
            }
        }
    }

    public static synchronized void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }

    public static synchronized void broadcast(final AbstractNotification notification) {
        if (notification.isGlobalScope() && !globalNotifications.contains(notification)) {
            globalNotifications.add(notification);
        }
        for (final BroadcastListener listener : listeners)
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    listener.broadcastNotification(notification);
                }
            });
    }
}
