package fr.pinguet62.xjc.swagger;

import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.deleteDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.sun.tools.xjc.Driver;

public class SwaggerPluginTestRunner {

    public static TypeDeclaration<?> runDriverAndParseClass(String nameName, String... additionalArgs) {
        // Run plugin
        List<String> args = new ArrayList<>(asList("src/test/resources/model.xsd", "-Xswagger"));
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
            CompilationUnit compilationUnit = JavaParser.parse(Paths.get("target/fr/pinguet62/" + nameName + ".java").toFile());
            return compilationUnit.getTypes().get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}