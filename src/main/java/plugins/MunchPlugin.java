package com.github.strattonbrazil.checklist;

public interface MunchPlugin {
    public void transform(PluginContext ctx, TaskFile file);
    default public void complete(PluginContext ctx) {};
}
