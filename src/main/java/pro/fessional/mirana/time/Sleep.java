package pro.fessional.mirana.time;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * Thead.sleep
 *
 * @author trydofor
 * @since 2023-01-05
 */
public class Sleep {

    /**
     * ignore InterruptedException and wake up
     */
    public static void ignoreInterrupt(@NotNull Duration time) {
        ignoreInterrupt(time.toMillis());
    }

    /**
     * ignore InterruptedException and wake up
     */
    public static void ignoreInterrupt(long ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            // ignore
        }
    }

    /**
     * ignore InterruptedException and continue to sleep
     */
    public static void snoozeInterrupt(@NotNull Duration time) {
        snoozeInterrupt(time.toMillis());
    }

    /**
     * ignore InterruptedException and continue to sleep
     */
    public static void snoozeInterrupt(long ms) {
        final long wake = System.currentTimeMillis() + ms;
        do {
            try {
                Thread.sleep(ms);
                break;
            }
            catch (InterruptedException e) {
                final long now = System.currentTimeMillis();
                if (now >= wake) {
                    break;
                }
                else {
                    ms = wake - now;
                }
            }
        } while (true);
    }

    /**
     * throw IllegalStateException, and set whether to keep interrupt status
     */
    public static void throwsInterrupt(@NotNull Duration time, boolean keep) {
        throwsInterrupt(time.toMillis(), keep);
    }

    /**
     * throw IllegalStateException, and set whether to keep interrupt status
     */
    public static void throwsInterrupt(long ms, boolean keep) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            if (keep) {
                Thread.currentThread().interrupt();
            }
            throw new IllegalStateException(e);
        }
    }
}
