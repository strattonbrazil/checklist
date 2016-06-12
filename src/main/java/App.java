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
        Path tasklistPath = args.length > 0 ? Paths.get(args[0]) : null;
        if (tasklistPath != null && !Files.exists(tasklistPath)) {
            tasklistPath = null;
        }
        App app = new App(tasklistPath, args);
    }

    public App(Path tasklistPath, String[] args) {
        Path currentRelativePath = Paths.get("").toAbsolutePath();

        if (tasklistPath == null) {
            try {
                tasklistPath = findTasklistPath();
            } catch (FileNotFoundException e) {
                System.err.println("unable to find suitable tasklist file");
                System.exit(1);
            }
        }

        try {
            System.out.println("args: " + String.join(", ", args));
            CommandLine cl = parseCommandLine(args);
            TaskList tasklist = new TaskList(tasklistPath.getParent());

            Binding binding = new Binding();
            binding.setProperty("out", new PrintStream(System.out));
            binding.setVariable("tasklist", tasklist);
            GroovyShell shell = new GroovyShell(binding);

            try {
                shell.run(tasklistPath.toFile(), new String[]{ } );

                tasklist.run(cl.getArgs());
            } catch (IOException e) {
                System.err.println("error processing script: " + tasklistPath.toString());
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

    private Path findTasklistPath() throws FileNotFoundException {
        Path currentRelativePath = Paths.get("").toAbsolutePath();

        while (currentRelativePath.getParent() != null) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentRelativePath)) {
                for (Path file: stream) {
                    if (file.getFileName().toString().matches("tasklisty.groovy")) {
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
}
