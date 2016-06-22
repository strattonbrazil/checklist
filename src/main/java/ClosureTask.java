package com.github.strattonbrazil.checklist;

import groovy.lang.*;

public class ClosureTask extends Task {
    final private Closure closure;
    public ClosureTask(Closure closure) {
        this.closure = closure;
    }

    public String call(TaskContext ctx) {
        return (String)this.closure.call(ctx);
    }
}
