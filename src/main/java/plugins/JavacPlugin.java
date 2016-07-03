package com.github.strattonbrazil.checklist;

import javax.tools.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

class JavacPlugin implements MunchPlugin {
    private Path _tmpDir;
    private ArrayList<JavaSourceFromString> _compilationUnits = new ArrayList<JavaSourceFromString>();

    public void transform(PluginContext ctx, TaskFile file) {
        String charEncoding = System.getProperty("file.encoding");
        CharBuffer charBuffer = Charset.forName(charEncoding).decode(file.buffer);
        //String baseName = FilenameUtils.getBaseName(file.path.getFileName());
        String path = ctx.cwd.relativize(file.path).toString().replace(".java", "");
        System.out.println("src path: " + path);
        _compilationUnits.add(new JavaSourceFromString("test/TestApp", charBuffer.toString()));
    }

    public void complete(PluginContext ctx) {
        try {
            _tmpDir = Files.createTempDirectory("javac_plugin");
            System.out.println("temp dir: " + _tmpDir.toString());
        } catch (IOException e) {
            System.err.println("error creating temp dir");
            System.exit(1);
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        // store the class files in a temp dir
        // TODO: just keep them in a buffer
        ArrayList<String> options = new ArrayList<String>();
        options.add("-d");
        options.add(_tmpDir.toString());

        StringWriter output = new StringWriter();
        JavaCompiler.CompilationTask task = compiler.getTask(output, fileManager, diagnostics, options, null, _compilationUnits);
        boolean success = task.call();
        for (Diagnostic diagnostic : diagnostics.getDiagnostics())
            System.out.format("Error on line %d in %s%n",
                    diagnostic.getLineNumber(),
                    diagnostic.getCode());
        System.out.println(output.toString());
        try {
            fileManager.close();
        } catch (IOException e) {
            System.err.println("compile failed");
            System.exit(1);
        }

        try {
            Files.walk(_tmpDir)
                 .map(p -> ctx.cwd.relativize(p))
                 .filter(p -> Files.isRegularFile(p))
                 .map(p -> new TaskFile(p, ctx.cwd.relativize(_tmpDir)))
                 .forEach(ctx::push);
        } catch (IOException e) {
                // TODO: handle this situation
        }
    }

    public class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        public JavaSourceFromString(String name, String code) {
            super( URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension), Kind.SOURCE);
            //System.out.println(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension).toString());
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}
