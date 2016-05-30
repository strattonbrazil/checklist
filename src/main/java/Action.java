package com.github.strattonbrazil.checklist;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import com.github.strattonbrazil.checklist.ChecklistContext;

public abstract class Action
{
    public abstract Callable<String> getWork(ChecklistContext ctx);
    public static Action getAction() {
        return new Action() {
            public Callable<String> getWork(ChecklistContext ctx) { return null; }
        };
    }
}
