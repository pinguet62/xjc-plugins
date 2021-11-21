package fr.pinguet62.xjc.common.argparser;

import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;

/**
 * @see Plugin#parseArgument(Options, String[], int)
 */
public interface ArgumentParser {

    int parse(String[] args, int start);

}
