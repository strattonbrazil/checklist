package com.github.strattonbrazil.checklist;

import rx.Observable;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class TaskContext
{
    private final Path cwd;

    public TaskContext(Path cwd) {
        this.cwd = cwd;
    }

    public TaskStream src(String glob) {
        return src(new String[] { glob }, new LinkedHashMap());
    }

    public TaskStream src(String glob, LinkedHashMap options) {
        return src(new String[] { glob }, options);
    }

    public TaskStream src(String[] globs) {
        return src(globs, new LinkedHashMap());
    }

    public TaskStream src(String[] globs, LinkedHashMap options) {
        // TODO: replace use of ArrayList with stream,
        ArrayList<Path> paths = new ArrayList<>();
        for (String glob : globs) {
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);

            try {
                paths.addAll(Files.walk(cwd)
                                  .filter(p -> matcher.matches(p))
                                  .collect(Collectors.toCollection(ArrayList::new)));
            } catch (IOException e) {
                // TODO: handle this situation
            }
        }

        // map this to something equivalent to a vinyl object
        return new TaskStream(Observable.from(paths).map(path -> new TaskFile(path)));
    }
}
