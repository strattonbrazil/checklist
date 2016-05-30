package com.github.strattonbrazil.checklist;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.management.RuntimeErrorException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
//import scala.tools.nsc.interpreter.IMain;
//import scala.tools.nsc.settings.MutableSettings.BooleanSetting;

class App
{
    public static void main(String[] args) {

        //String s = currentRelativePath.toAbsolutePath().toString();

        // filename passed in
        Path checklistPath = args.length > 0 ? Paths.get(args[0]) : null;
        App app = new App(checklistPath);
    }

    public App(Path checklistPath) {
        Path currentRelativePath = Paths.get("").toAbsolutePath();

        if (checklistPath == null) {
            try {
                checklistPath = findChecklistPath();
            } catch (FileNotFoundException e) {
                System.err.println("unable to find suitable checklist file");
                System.exit(1);
            }
        }

        Binding binding = new Binding();
        binding.setProperty("out", new PrintStream(System.out));

        binding.setVariable("wack", new ChecklistContext());
        binding.setVariable("checklist", new Checklist());

        GroovyShell shell = new GroovyShell(binding);

        try {

            shell.run(checklistPath.toFile(), new String[]{ } );
            //Script script = shell.parse(checklistPath.toFile());

            //shell.evaluate(checklistPath.toFile());
        } catch (IOException e) {
            System.err.println("ack");
        }

    }

    private Path findChecklistPath() throws FileNotFoundException {
        Path currentRelativePath = Paths.get("").toAbsolutePath();

        while (currentRelativePath.getParent() != null) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentRelativePath)) {
                for (Path file: stream) {
                    if (file.getFileName().toString().matches("checklisty.groovy")) {
                        return file;
                    }
                }
            } catch (IOException | DirectoryIteratorException x) {
                // IOException can never be thrown by the iteration.
                // In this snippet, it can only be thrown by newDirectoryStream.
                System.err.println(x);
            }

            currentRelativePath = currentRelativePath.getParent();
        }

        throw new FileNotFoundException("file not found");
    }

    private class PrivateThing
    {

    }
}
