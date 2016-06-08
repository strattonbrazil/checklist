package com.github.strattonbrazil.checklist;

class DestPlugin implements MunchPlugin {
    final public String path;

    public DestPlugin(String path) {
        this.path = path;

    }

    public void transform(TaskFile file) {
        System.out.println("transforming: " + file);
    }
}
