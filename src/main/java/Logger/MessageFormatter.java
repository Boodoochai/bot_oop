package Logger;

final class MessageFormatter {

    static String format(String template, Object... args) {
        if (template == null || args == null || args.length == 0) {
            return template;
        }

        StringBuilder sb = new StringBuilder(template.length() + 32);
        int argIndex = 0;

        for (int i = 0; i < template.length(); i++) {
            if (i + 1 < template.length()
                    && template.charAt(i) == '{'
                    && template.charAt(i + 1) == '}'
                    && argIndex < args.length) {

                sb.append(String.valueOf(args[argIndex++]));
                i++;
            } else {
                sb.append(template.charAt(i));
            }
        }

        return sb.toString();
    }

    private MessageFormatter() {}
}
