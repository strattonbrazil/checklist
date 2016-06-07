package com.github.strattonbrazil.checklist;

import java.util.concurrent.Callable;

public abstract class Task
{
    public abstract Callable<String> getWork(ChecklistContext ctx);
}
