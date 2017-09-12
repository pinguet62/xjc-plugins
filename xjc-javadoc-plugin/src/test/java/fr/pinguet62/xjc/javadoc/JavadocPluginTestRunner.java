package fr.pinguet62.xjc.javadoc;

import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.deleteDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.sun.tools.xjc.Driver;

public class JavadocPluginTestRunner {

    public static TypeDeclaration<?> runDriverAndParseClass(String className, String... additionalArgs) {
        // Run plugin
        List<String> args = new ArrayList<>(asList("src/test/resources/model.xsd", "-Xjavadoc"));
        args.addAll(asList(additionalArgs));
        args.addAll(asList("-d", "target"));
        String[] pluginArgs = args.toArray(new String[args.size()]);
        try {
            deleteDirectory(new File("target/fr/pinguet62"));
            Driver.run(pluginArgs, System.out, System.out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Parse generated file
        try {
            CompilationUnit compilationUnit = JavaParser.parse(Paths.get("target/fr/pinguet62/" + className + ".java").toFile());
            return compilationUnit.getTypes().get(0);
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

}