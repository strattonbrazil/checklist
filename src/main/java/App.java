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
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

class App
{
    public static void main(String[] args) {
        // if the first arg is an existing file, assume it's the definition file
        Path checklistPath = args.length > 0 ? Paths.get(args[0]) : null;
        if (checklistPath != null && !Files.exists(checklistPath)) {
            checklistPath = null;
        }
        App app = new App(checklistPath, args);
    }

    public App(Path checklistPath, String[] args) {
        Path currentRelativePath = Paths.get("").toAbsolutePath();

        if (checklistPath == null) {
            try {
                checklistPath = findChecklistPath();
            } catch (FileNotFoundException e) {
                System.err.println("unable to find suitable checklist file");
                System.exit(1);
            }
        }

        try {
            CommandLine cl = parseCommandLine(args);
            Checklist checklist = new Checklist();

            Binding binding = new Binding();
            binding.setProperty("out", new PrintStream(System.out));
            binding.setVariable("checklist", checklist);
            GroovyShell shell = new GroovyShell(binding);

            try {
                shell.run(checklistPath.toFile(), new String[]{ } );

                checklist.run(cl.getArgs());
            } catch (IOException e) {
                System.err.println("error processing script: " + checklistPath.toString());
                System.exit(1);
            }
        } catch (org.apache.commons.cli.ParseException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private CommandLine parseCommandLine(String[] args)
        throws org.apache.commons.cli.ParseException {

        DefaultParser parser = new DefaultParser();
        Options opts = new Options();
        CommandLine cl = null;

        return parser.parse(opts, args);
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
