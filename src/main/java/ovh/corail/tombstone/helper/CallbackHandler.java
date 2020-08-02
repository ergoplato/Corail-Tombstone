package ovh.corail.tombstone.helper;

import com.google.common.collect.Queues;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CallbackHandler {
    private static final Queue<DelayedCallback> DELAYED_CALLBACKS = Queues.newConcurrentLinkedQueue();

    private static Queue<DelayedCallback> getDelayedQueue() {
        synchronized (DELAYED_CALLBACKS) {
            return DELAYED_CALLBACKS;
        }
    }

    public static void addCallback(int delay, Runnable callback) {
        getDelayedQueue().add(new DelayedCallback(delay, callback));
    }

    public static void clear() {
        getDelayedQueue().clear();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
            final Queue<DelayedCallback> callbacks = getDelayedQueue();
            if (callbacks.isEmpty()) {
                return;
            }
            final Set<DelayedCallback> toRemove = new HashSet<>();
            callbacks.forEach(callback -> {
                if (callback.delay.getAndDecrement() <= 0) {
                    callback.task.run();
                    toRemove.add(callback);
                }
            });
            callbacks.removeAll(toRemove);
        }
    }

    public static class DelayedCallback {
        final AtomicInteger delay;
        final Runnable task;

        DelayedCallback(int delay, Runnable task) {
            this.delay = new AtomicInteger(delay);
            this.task = task;
        }
    }
}
