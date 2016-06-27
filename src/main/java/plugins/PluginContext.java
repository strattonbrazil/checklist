package com.github.strattonbrazil.checklist;

import java.nio.file.*;
import java.util.*;

public class PluginContext {
    public final Path cwd;
    private ArrayList<TaskFile> _files = new ArrayList<TaskFile>();

    public PluginContext(Path cwd) {
        this.cwd = cwd;
    }

    public void push(TaskFile file) {
        _files.add(file);
    }

    public boolean hasPushedFiles() {
        return _files.size() > 0;
    }

    public ArrayList<TaskFile> getFiles() {
        return _files;
    }
}
