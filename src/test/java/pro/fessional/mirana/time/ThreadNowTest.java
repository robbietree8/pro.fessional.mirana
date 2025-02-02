package pro.fessional.mirana.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static pro.fessional.mirana.time.ThreadNow.TweakClock;

/**
 * @author trydofor
 * @since 2022-10-10
 */
class ThreadNowTest {

    @Test
    void test1() {
        final Instant n = Instant.now();
        final ZoneId CN = ZoneId.of("Asia/Shanghai");
        final ZoneId JP = ZoneId.of("Asia/Tokyo");
        try {
            TweakClock.tweakThread(Clock.fixed(n, CN));
            // 2022-10-10T04:33:39.180Z
            Assertions.assertEquals(n, ThreadNow.instant());
            // 2022-10-10T13:33:39.180+09:00[Asia/Tokyo]
            Assertions.assertEquals(n.atZone(JP), ThreadNow.zonedDateTime(JP));
        }
        finally {
            TweakClock.resetThread();
        }
    }
}
