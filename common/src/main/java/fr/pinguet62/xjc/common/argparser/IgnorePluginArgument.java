package fr.pinguet62.xjc.common.argparser;

public class IgnorePluginArgument implements ArgumentParser {

    private final String option;

    public IgnorePluginArgument(String option) {
        this.option = option;
    }

    @Override
    public int parse(String[] args, int start) {
        return args[start].equals(option) ? 1 : 0;
    }

}