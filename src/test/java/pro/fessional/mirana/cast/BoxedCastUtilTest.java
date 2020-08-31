package pro.fessional.mirana.cast;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author trydofor
 * @since 2020-08-31
 */
public class BoxedCastUtilTest {

    @Test
    public void list() {
        {
            Object obj = new int[]{1, 2, 3};
            BoxedCastUtil.list(obj);
        }
        {
            Object obj = new long[]{1, 2, 3};
            BoxedCastUtil.list(obj);
        }
        {
            Object obj = Arrays.asList(1, 2, 3);
            BoxedCastUtil.list(obj);
        }
        {
            Object obj = 1;
            BoxedCastUtil.list(obj);
        }
    }
}