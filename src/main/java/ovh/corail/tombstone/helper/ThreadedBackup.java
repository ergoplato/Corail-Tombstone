package ovh.corail.tombstone.helper;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadedBackup implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ThreadedBackup INSTANCE = new ThreadedBackup();
    private final List<IThreadedBackup> threadedBackupQueue = Collections.synchronizedList(Lists.newArrayList());
    private volatile AtomicLong queuedCounter = new AtomicLong();
    private volatile AtomicLong counter = new AtomicLong();
    private volatile boolean isWaiting;

    private ThreadedBackup() {
        Thread thread = new Thread(this, "Thread Backup");
        thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandlerWithName(LOGGER));
        thread.setPriority(1);
        thread.start();
    }

    public void run() {
        while (true) {
            processQueue();
        }
    }

    private void processQueue() {
        for (int i = 0; i < this.threadedBackupQueue.size(); ++i) {
            IThreadedBackup backup;
            boolean success;
            synchronized ((backup = this.threadedBackupQueue.get(i))) {
                success = backup.writeNextBackup();
            }
            if (!success) {
                this.threadedBackupQueue.remove(i--);
                this.counter.incrementAndGet();
            }
            try {
                Thread.sleep(this.isWaiting ? 0L : 10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.threadedBackupQueue.isEmpty()) {
            try {
                Thread.sleep(25L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void queueBackup(IThreadedBackup backup) {
        if (!this.threadedBackupQueue.contains(backup)) {
            this.queuedCounter.incrementAndGet();
            this.threadedBackupQueue.add(backup);
        }
    }

    public interface IThreadedBackup {
        boolean writeNextBackup();
    }

    public class DefaultUncaughtExceptionHandlerWithName implements Thread.UncaughtExceptionHandler {
        private final Logger logger;

        DefaultUncaughtExceptionHandlerWithName(Logger logger) {
            this.logger = logger;
        }

        public void uncaughtException(Thread thread, Throwable throwable) {
            this.logger.error("Caught previously unhandled exception :");
            this.logger.error(thread.getName(), throwable);
        }
    }
}
