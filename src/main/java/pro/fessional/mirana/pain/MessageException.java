package pro.fessional.mirana.pain;

import pro.fessional.mirana.data.CodeEnum;

/**
 * 仅用来输出业务消息的无堆栈异常
 *
 * @author trydofor
 * @since 2022-09-05
 */
public class MessageException extends CodeException {

    private static final long serialVersionUID = 3630382084139069605L;

    public MessageException(String code) {
        super(false, code);
    }

    public MessageException(String code, String message) {
        super(false, code, message);
    }

    public MessageException(CodeEnum code) {
        super(false, code);
    }

    public MessageException(CodeEnum code, Object... args) {
        super(false, code, args);
    }

    public MessageException(boolean stack, String code) {
        super(stack, code);
    }

    public MessageException(boolean stack, String code, String message) {
        super(stack, code, message);
    }

    public MessageException(boolean stack, CodeEnum code) {
        super(stack, code);
    }

    public MessageException(boolean stack, CodeEnum code, Object... args) {
        super(stack, code, args);
    }

    public MessageException(Throwable cause, String code) {
        super(cause, code);
    }

    public MessageException(Throwable cause, String code, String message) {
        super(cause, code, message);
    }

    public MessageException(Throwable cause, CodeEnum code) {
        super(cause, code);
    }

    public MessageException(Throwable cause, CodeEnum code, Object... args) {
        super(cause, code, args);
    }
}
