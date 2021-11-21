package fr.pinguet62.xjc.common.argparser;

/**
 * {@link ArgumentParser} where value is an {@link Enum}.
 */
public class EnumArgumentParser<T extends Enum<T>> implements ArgumentParser {

    private final String argumentName;

    private final Class<T> type;

    private T value;

    /**
     * @param argumentName The argument name.<br>
     *                     Must end with {@code "="} (example: {@code "-Xfoo="}).
     * @param type         The {@link Enum} type.
     */
    public EnumArgumentParser(String argumentName, Class<T> type) {
        this.argumentName = argumentName;
        this.type = type;
    }

    /**
     * @param argumentName The argument name.<br>
     *                     Must end with {@code "="} (example: {@code "-Xfoo="}).
     * @param type         The {@link Enum} type.
     * @param defaultValue The value to use if argument is not present.
     */
    public EnumArgumentParser(String argumentName, Class<T> type, T defaultValue) {
        this(argumentName, type);
        this.value = defaultValue;
    }

    public T getSelected() {
        return value;
    }

    /**
     * @throws IllegalArgumentException See {@link Enum#valueOf(Class, String)}
     */
    @Override
    public int parse(String[] args, int start) {
        String arg = args[start];
        if (!arg.startsWith(argumentName))
            return 0;
        arg = arg.substring(argumentName.length());
        value = Enum.valueOf(type, arg);
        return 1;
    }

}
