package pro.fessional.mirana.best;

import org.jetbrains.annotations.Contract;
import pro.fessional.mirana.evil.TweakingContext;

import java.util.function.Consumer;

/**
 * 关闭一些安全的代码块的IDE警报
 *
 * @author trydofor
 * @since 2022-10-24
 */
public class DummyBlock {

    public static final TweakingContext<Consumer<Throwable>> TweakIgnore = new TweakingContext<>();

    /**
     * Catch block should not be empty
     */
    public static void ignore(Throwable t) {
        final Consumer<Throwable> handler = TweakIgnore.current(false);
        if (handler != null) {
            handler.accept(t);
        }
    }

    /**
     * statement has empty body
     */
    public static void empty() {
    }

    /**
     * 业务上不可到达的代码
     */
    @Contract("->fail")
    public static void never() throws IllegalStateException {
        throw new IllegalStateException("should NOT invoke NEVER");
    }

    /**
     * 业务上不可到达的代码
     */
    @Contract("_->fail")
    public static void never(String msg) throws IllegalStateException {
        throw new IllegalStateException("should NOT invoke NEVER:" + msg);
    }

    /**
     * 未实现的方法，不可执行
     */
    @Contract("->fail")
    public static void todo() throws IllegalStateException {
        throw new IllegalStateException("should NOT invoke TODO");
    }

    /**
     * 未实现的方法，不可执行
     */
    @Contract("_->fail")
    public static void todo(String msg) throws IllegalStateException {
        throw new IllegalStateException("should NOT invoke TODO:" + msg);
    }

    /**
     * 等待修复的代码，不可执行
     */
    @Contract("->fail")
    public static void fixme() throws IllegalStateException {
        throw new IllegalStateException("should NOT invoke FIXME");
    }

    /**
     * 等待修复的代码，不可执行
     */
    @Contract("_->fail")
    public static void fixme(String msg) throws IllegalStateException {
        throw new IllegalStateException("should NOT invoke FIXME:" + msg);
    }
}