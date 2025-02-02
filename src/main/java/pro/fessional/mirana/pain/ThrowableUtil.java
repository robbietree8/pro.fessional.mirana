package pro.fessional.mirana.pain;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pro.fessional.mirana.best.Param.Out;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtil {

    /**
     * print StackTrace to buffer in 5-StackTrace (except root cause), Reversed, Shorten.
     * And filename only in 1st stack, full stack only in root cause.
     *
     * @param buffer to write
     * @param t      throwable
     */
    public static void print(@Out Appendable buffer, Throwable t) {
        print(buffer, t, true, true, 5, Integer.MAX_VALUE, 1);
    }

    /**
     * print StackTrace to buffer. but filename only in 1st stack, full stack only in root cause.
     *
     * @param buffer  to write
     * @param t       throwable
     * @param reverse reverse the stack
     * @param shorten shorten the class
     */
    public static void print(@Out Appendable buffer, Throwable t, boolean reverse, boolean shorten) {
        print(buffer, t, reverse, shorten, Integer.MAX_VALUE, Integer.MAX_VALUE, 1);
    }

    /**
     * print StackTrace to buffer. but filename only in 1st stack, full stack only in root cause.
     *
     * @param buffer   to write
     * @param t        throwable
     * @param reverse  reverse the stack
     * @param shorten  shorten the class
     * @param maxStack max Stack (except root cause) to print, default Integer#MAX_VALUE
     * @param maxCause max Cause  to print, default Integer#MAX_VALUE
     */
    public static void print(@Out Appendable buffer, Throwable t, boolean reverse, boolean shorten, int maxStack, int maxCause) {
        print(buffer, t, reverse, shorten, maxStack, maxCause, 1);
    }

    private static void print(@Out final Appendable buffer, final Throwable t, final boolean reverse, final boolean shorten, final int stack, final int max, final int cur) {
        if (t == null || max < cur) return;
        final Throwable c = t.getCause();
        int nextDepth = cur + 1;
        boolean caused = c != null;
        try {
            if (reverse) {
                if (caused) print(buffer, c, reverse, shorten, stack, max, nextDepth);
                doPrint(buffer, t, reverse, shorten, !caused, caused ? stack : Integer.MAX_VALUE, cur);
            }
            else {
                doPrint(buffer, t, reverse, shorten, !caused, caused ? stack : Integer.MAX_VALUE, cur);
                if (caused) print(buffer, c, reverse, shorten, stack, max, nextDepth);
            }
        }
        catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    private static void doPrint(@Out final Appendable buffer, final Throwable t, final boolean reverse, final boolean shorten, final boolean root, final int stk, final int cur) throws IOException {

        if (reverse) {
            if (!root) buffer.append("Causes to: ");
        }
        else {
            if (cur > 1) buffer.append("Caused by: ");
        }

        if (root) buffer.append("(RootCause) ");
        buffer.append(t.getClass().getName()).append(": ").append(t.getMessage()).append('\n');

        int i = 0;
        boolean nfn = true;
        final StringBuilder sb = buffer instanceof StringBuilder ? (StringBuilder) buffer : new StringBuilder();
        for (StackTraceElement el : t.getStackTrace()) {
            if (++i > stk) break;

            buffer.append("\tat ");
            final String cn = el.getClassName();
            if (shorten) {
                int cl = sb.length();
                for (int j = 0, ln = cn.length(); j < ln; j++) {
                    char c = cn.charAt(j);
                    if (c == '.') {
                        sb.setLength(cl + 1);
                        cl = cl + 2;
                    }
                    buffer.append(c);
                }
                if (sb != buffer) {
                    buffer.append(sb.toString());
                    sb.setLength(0);
                }
            }
            else {
                buffer.append(cn);
            }

            buffer.append('.').append(el.getMethodName()).append('(');
            if (el.isNativeMethod()) {
                buffer.append("Native");
            }
            else {
                String fn = el.getFileName();
                if (fn == null) {
                    buffer.append("Unknown");
                }
                else {
                    if (nfn){
                        buffer.append(fn);
                        nfn = false;
                    }
                    buffer.append(":").append(String.valueOf(el.getLineNumber()));
                }
            }

            buffer.append(")\n");
        }
    }

    /**
     * print StackTrace to String
     */
    @NotNull
    public static String toString(Throwable t) {
        if (t == null) return "";
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            t.printStackTrace(pw);
            return sw.toString();
        }
    }

    /**
     * print root StackTrace to String
     */
    @NotNull
    public static String rootString(Throwable t) {
        Throwable r = root(t);
        return toString(r);
    }

    /**
     * get the root StackTrace which is the root cause.
     */
    @Contract("!null->!null")
    public static Throwable root(Throwable t) {
        while (t != null) {
            Throwable x = t.getCause();
            if (x == null) {
                return t;
            }
            else {
                t = x;
            }
        }
        return t;
    }

    /**
     * Whether the specified type exception is included in the exception stack.
     *
     * @param t the exception stack
     * @param e the specified type
     */
    public static boolean contains(Throwable t, Class<? extends Throwable> e) {
        if (e == null) return false;
        while (t != null) {
            if (e.isInstance(t)) return true;
            t = t.getCause();
        }
        return false;
    }

    /**
     * from bottom to top (old to new) of the stack, find the first (newest) specified type exception
     *
     * @param t   the exception stack
     * @param e   the specified type
     * @param <T> Type
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T firstCause(Throwable t, Class<T> e) {
        if (e == null) return null;
        T f = null;
        while (t != null) {
            if (e.isInstance(t)) {
                f = (T) t;
            }
            t = t.getCause();
        }
        return f;
    }

    /**
     * from bottom to top of the stack (old to new), find the last (oldest) specified type exception
     *
     * @param t   the exception stack
     * @param e   the specified type
     * @param <T> Type
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T lastCause(Throwable t, Class<T> e) {
        if (e == null) return null;
        while (t != null) {
            if (e.isInstance(t)) {
                return (T) t;
            }
            t = t.getCause();
        }
        return null;
    }

    @SafeVarargs
    public static void throwMatch(Throwable t, Class<? extends RuntimeException>... runtime) {
        if (runtime != null) {
            for (Class<? extends RuntimeException> re : runtime) {
                if (re.isInstance(t)) {
                    throw (RuntimeException) t;
                }
            }
        }
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }
        else {
            throw new RuntimeException(t);
        }
    }

    @SafeVarargs
    public static void throwCause(Throwable t, Class<? extends RuntimeException>... runtime) {
        if (runtime != null && runtime.length > 0) {
            while (t != null) {
                for (Class<? extends RuntimeException> e : runtime) {
                    if (e.isInstance(t)) {
                        throw (RuntimeException) t;
                    }
                }
                t = t.getCause();
            }
        }
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }
        else {
            throw new RuntimeException(t);
        }
    }
}
