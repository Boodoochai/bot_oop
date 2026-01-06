package Logger;

final class LogArguments {

    static Throwable extractThrowable(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }

        Object last = args[args.length - 1];
        return (last instanceof Throwable) ? (Throwable) last : null;
    }

    static Object[] trimThrowable(Object[] args) {
        Throwable t = extractThrowable(args);
        if (t == null) {
            return args;
        }

        Object[] trimmed = new Object[args.length - 1];
        System.arraycopy(args, 0, trimmed, 0, trimmed.length);
        return trimmed;
    }

    private LogArguments() {}
}
