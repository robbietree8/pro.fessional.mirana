package pro.fessional.mirana.text;

import org.jetbrains.annotations.NotNull;
import pro.fessional.mirana.evil.ThreadLocalAttention;
import pro.fessional.mirana.evil.ThreadLocalSoft;

import java.lang.ref.SoftReference;

/**
 * <pre>
 * 内部使用 ThreadLocal，有leak隐患，必须使用以下模式之一。
 * ① static，JVM内唯一Ref，避免多次创建临时Ref
 * ② 使用 try-finally-close 模式，remove掉Ref
 * </pre>
 *
 * @author trydofor
 * @since 2021-03-24
 */
public class BuilderHolder extends ThreadLocalSoft<StringBuilder> {

    private final int min;
    private final int max;

    public BuilderHolder() throws ThreadLocalAttention {
        this(1024, 8096);
    }

    /**
     * 初始 holder，只缓存max size内的builder
     *
     * @param min 初始size
     * @param max 最大size
     */
    public BuilderHolder(int min, int max) throws ThreadLocalAttention {
        this(min, max, new ThreadLocal<>());
    }

    public BuilderHolder(int min, int max, ThreadLocal<SoftReference<StringBuilder>> threadLocal) throws ThreadLocalAttention {
        super(threadLocal);
        this.min = min;
        this.max = max;
    }

    @Override
    public @NotNull StringBuilder initValue() {
        return new StringBuilder(min);
    }

    @Override
    public boolean anewValue(@NotNull StringBuilder bd) {
        if (bd.length() > max) {
            return true;
        }
        else {
            bd.setLength(0);
            return false;
        }
    }
}
