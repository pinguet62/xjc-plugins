package fr.pinguet62.xjc.common.argparser;

public class BooleanArgumentParser implements ArgumentParser {

    private final boolean defaultValue;

    private final String option;

    private Boolean value;

    public BooleanArgumentParser(String option, boolean defaultValue) {
        this.option = option;
        this.defaultValue = defaultValue;
    }

    public boolean getValue() {
        return value == null ? defaultValue : value;
    }

    @Override
    public int parse(String[] args, int start) {
        value = args[start].equals(option);
        return value ? 1 : 0;
    }

}