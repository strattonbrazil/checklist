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

public class ChecklistContext
{
    private final Path cwd;

    public ChecklistContext(Path cwd) {
        this.cwd = cwd;
    }

    public ActionStream src(String glob) {
        return src(new String[] { glob }, new LinkedHashMap());
    }

    public ActionStream src(String glob, LinkedHashMap options) {
        return src(new String[] { glob }, options);
    }

    public ActionStream src(String[] globs) {
        return src(globs, new LinkedHashMap());
    }

    public ActionStream src(String[] globs, LinkedHashMap options) {
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
        return new ActionStream(Observable.from(paths).map(path -> new ActionFile(path)));
    }
}
