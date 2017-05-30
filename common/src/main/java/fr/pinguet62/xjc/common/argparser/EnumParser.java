package fr.pinguet62.xjc.common.argparser;

/** {@link ArgumentParser} with fixed list of values defined into {@link Enum}. */
public class EnumParser<T extends Enum<T>> implements ArgumentParser {

    private T defaultOrParsed;

    private final String prefix;

    private final Class<T> type;

    /**
     * @param prefix
     *            The argument prefix, with {@code "="}. Example: {@code "-Xfoo="}.
     * @param type
     *            The {@link Enum} type (for reflection).
     */
    public EnumParser(String prefix, Class<T> type) {
        this.prefix = prefix;
        this.type = type;
    }

    /**
     * @param prefix
     *            The argument prefix, with {@code "="}. Example: {@code "-Xfoo="}.
     * @param type
     *            The {@link Enum} type (for reflection).
     * @param defaultValue
     *            The value to use if argument is not present.
     */
    public EnumParser(String prefix, Class<T> type, T defaultValue) {
        this(prefix, type);
        this.defaultOrParsed = defaultValue;
    }

    public T getSelected() {
        return defaultOrParsed;
    }

    /**
     * @throws IllegalArgumentException
     *             See {@link Enum#valueOf(Class, String)}
     */
    @Override
    public int parse(String[] args, int start) {
        String arg = args[start];
        if (!arg.startsWith(prefix))
            return 0;
        arg = arg.substring(prefix.length());
        defaultOrParsed = Enum.valueOf(type, arg);
        return 1;
    }

}