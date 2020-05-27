package liwey.json2pojo;

import org.junit.Assert;
import org.junit.Test;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GeneratorTest {

    @Test
    public void testFormatClassName() {
        Assert.assertEquals("Test", Generator.formatClassName("test"));
        Assert.assertEquals("TestCamelCase", Generator.formatClassName("testCamelCase"));
        Assert.assertEquals("TestWithUnderscores", Generator.formatClassName("test_with_underscores"));
        Assert.assertEquals("TestWithHyphens", Generator.formatClassName("test-with-hyphens"));
        Assert.assertEquals("TestWithDots", Generator.formatClassName("test.with.dots"));
        Assert.assertEquals("AbstractTest", Generator.formatClassName("abstractTest"));
        Assert.assertEquals("Test", Generator.formatClassName("1Test"));
        Assert.assertEquals("InvalidChars", Generator.formatClassName("Invalid@$%@#$^&#%@Chars"));
    }

    @Test
    public void testExamples() throws Exception {
        String src = System.getProperty("user.dir") + "/src/test/java";
        File jsonFile = new File(src + "/example.json");
        String json = new String(Files.readAllBytes(jsonFile.toPath()));
        String dest = System.getProperty("user.dir") + "/build/classes/java/main";
        String packageName = "example.spark";
        Generator generator = new Generator(packageName, src, null);
        int n = generator.generateFromJson("SparkProgress", json);
        System.out.println("Generated "+n);
    }

    private boolean compile(String packageName, String src, String dest) throws IOException {
        List<String> classes = new ArrayList<>();
        for (File file : Objects.requireNonNull(new File(src + "/" + packageName.replace('.', '/')).listFiles())) {
            classes.add(file.getAbsolutePath());
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        JavaFileManager.Location oLocation = StandardLocation.CLASS_OUTPUT;
        fileManager.setLocation(oLocation, Arrays.asList(new File(dest)));
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(classes);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
        boolean result = task.call();
        fileManager.close();
        return result;
    }
}