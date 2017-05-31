package fr.pinguet62.xjc.common.argparser;

/** {@link ArgumentParser} to check if argument is present. */
public class BooleanArgumentParser implements ArgumentParser {

    private final String argumentName;

    private boolean present;

    public BooleanArgumentParser(String argumentName) {
        this.argumentName = argumentName;
    }

    public boolean isPresent() {
        return present;
    }

    @Override
    public int parse(String[] args, int start) {
        present = args[start].equals(argumentName);
        return present ? 1 : 0;
    }

}