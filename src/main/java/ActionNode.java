package com.github.strattonbrazil.checklist;

import java.util.ArrayList;

public class ActionNode
{
    final String name;
    final ArrayList<String> _dependencies;
    final Action _action;

    public ActionNode(String name, ArrayList<String> deps, Action action) {
        this.name = name;
        this._dependencies = deps;
        this._action = action;
    }

    public ArrayList<String> dependencies() {
        return (ArrayList<String>)(_dependencies.clone());
    }
}
