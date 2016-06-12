package com.github.strattonbrazil.checklist;

import java.util.*;

public class PluginContext {
    private ArrayList<TaskFile> _files = new ArrayList<TaskFile>();

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
