package Logger;

final class LogCall {

    private final String formattedMessage;
    private final Throwable throwable;

    private LogCall(String formattedMessage, Throwable throwable) {
        this.formattedMessage = formattedMessage;
        this.throwable = throwable;
    }

    static LogCall of(String template, Object... args) {
        if (args == null || args.length == 0) {
            return new LogCall(template, null);
        }

        Object last = args[args.length - 1];
        Throwable t = (last instanceof Throwable) ? (Throwable) last : null;

        Object[] fmtArgs = (t == null)
                ? args
                : trimLast(args);

        String msg = MessageFormatter.format(template, fmtArgs);
        return new LogCall(msg, t);
    }

    String formattedMessage() {
        return formattedMessage;
    }

    Throwable throwable() {
        return throwable;
    }

    private static Object[] trimLast(Object[] args) {
        Object[] trimmed = new Object[args.length - 1];
        System.arraycopy(args, 0, trimmed, 0, trimmed.length);
        return trimmed;
    }
}
