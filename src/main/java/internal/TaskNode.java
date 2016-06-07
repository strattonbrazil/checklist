package com.github.strattonbrazil.checklist;

import java.util.ArrayList;

public class TaskNode
{
    final String name;
    final ArrayList<String> _dependencies;
    final Task task;

    public TaskNode(String name, ArrayList<String> deps, Task task) {
        this.name = name;
        this._dependencies = deps;
        this.task = task;
    }


    public ArrayList<String> dependencies() {
        return (ArrayList<String>)(_dependencies.clone());
    }
}

