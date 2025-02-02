package pro.fessional.mirana.time;

import org.junit.jupiter.api.Test;
import pro.fessional.mirana.SystemOut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pro.fessional.mirana.time.DateFormatter.FMT_DATE_PSE;
import static pro.fessional.mirana.time.DateFormatter.FMT_TIME_PSE;

/**
 * @author trydofor
 * @since 2019-06-28
 */
public class DateFormatterTest {

    @Test
    public void printDateTime() {
        SystemOut.println("------");
        SystemOut.println(DateFormatter.fixFull19("2017-04-26"));
        SystemOut.println(DateFormatter.fixFull19("2017-04-26 12"));
        SystemOut.println(DateFormatter.fixFull19("2017-04-26 12:34"));
        SystemOut.println(DateFormatter.fixFull19("2017-04-26 12:34:56"));
        SystemOut.println(DateFormatter.fixFull19("2017-04-26 1"));
        SystemOut.println(DateFormatter.fixFull19("2017-04-26 12:3"));
        SystemOut.println(DateFormatter.fixFull19("2017-04-26 12:34:5"));
    }

    @Test
    public void printFull23() {
        SystemOut.println("------");

        Date d = new Date();

        SystemOut.println(DateFormatter.full23().format(d));
        SystemOut.println(DateFormatter.full23(d));

        LocalDateTime n = LocalDateTime.now();
        SystemOut.println(DateFormatter.FMT_FULL_23.format(n));
        SystemOut.println(DateFormatter.full23(n));
    }

    /**
     * V       time-zone ID                zone-id           America/Los_Angeles; Z; -08:30
     * O       localized zone-offset       offset-O          GMT+8; GMT+08:00; UTC-08:00;
     * X       zone-offset 'Z' for zero    offset-X          Z; -08; -0830; -08:30; -083015; -08:30:15;
     * x       zone-offset                 offset-x          +0000; -08; -0830; -08:30; -083015; -08:30:15;
     * Z       zone-offset                 offset-Z          +0000; -0800; -08:00;
     */
    @Test
    public void printZone() {
        // 2021-05-06 20:27:04.633 Asia/Shanghai
        DateTimeFormatter d1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS VV");
        // 2021-05-06 20:28:26.883 +0800 Asia/Shanghai
        DateTimeFormatter d2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS ZZ VV");
        final ZonedDateTime now = ZonedDateTime.now();
        SystemOut.println(now.format(d1));
        SystemOut.println(now.format(d2));

        for (String zid : ZoneId.getAvailableZoneIds()) {
            SystemOut.println(zid);
        }
        SystemOut.println(DateFormatter.fullTz(now));
        SystemOut.println(DateFormatter.fullTz(now.toOffsetDateTime()));
    }

    @Test
    public void testPnt() {
        assertEquals(LocalTime.of(1, 2, 3), LocalTime.parse("1:2:3", FMT_TIME_PSE));
        assertEquals(LocalTime.of(1, 2, 0), LocalTime.parse("1:2", FMT_TIME_PSE));
        assertEquals(LocalTime.of(1, 0, 0), LocalTime.parse("1", FMT_TIME_PSE));

        assertEquals(LocalDate.of(2021, 1, 2), FMT_DATE_PSE.parse("21-1-2", DateParser.QueryDate));
        assertEquals(LocalDate.of(2021, 1, 2), FMT_DATE_PSE.parse("2021/1/2", DateParser.QueryDate));
        assertEquals(LocalDate.of(2021, 1, 1), FMT_DATE_PSE.parse("21-1", DateParser.QueryDate));
    }
}
