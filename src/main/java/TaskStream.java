package com.github.strattonbrazil.checklist;

import rx.Observable;

public class TaskStream {
    final private Observable<TaskFile> files;

    public TaskStream(Observable<TaskFile> files) {
        this.files = files;

        //files.subscribe(System.out::println);
    }

    public com.github.strattonbrazil.checklist.TaskStream pipe() {
        return null;
    }

    public com.github.strattonbrazil.checklist.TaskStream dest(String path) {
        return null;
    }
}
