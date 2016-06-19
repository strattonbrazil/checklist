package com.github.strattonbrazil.checklist;

import java.util.concurrent.Callable;

public abstract class Task
{
    public abstract String call(TaskContext ctx);
}
