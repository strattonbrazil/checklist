package com.github.strattonbrazil.checklist;

import javax.tools.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

class JavacPlugin implements MunchPlugin {
    private Path _tmpDir;
    private ArrayList<JavaSourceFromString> _compilationUnits;

    public JavacPlugin() {

        _compilationUnits = new ArrayList<JavaSourceFromString>();
    }

    public void transform(PluginContext ctx, TaskFile file) {

        // write file
        //Path cwd = Paths.get("").toAbsolutePath();
        //copyFile(file.buffer, cwd.relativize(file.path));

        //System.out.println("copying task file: " + file + " to " + path);

        _compilationUnits.add(new JavaSourceFromString("Foo", "public class Foo { private class Arg {} } "));


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
        }
        System.out.println("Success: " + success);


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
