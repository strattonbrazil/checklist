package com.github.strattonbrazil.checklist;

import rx.*;
import rx.functions.Action1;

public class TaskStream {
    final private Observable<TaskFile> files;

    public TaskStream(Observable<TaskFile> files) {
        this.files = files;

        //files.subscribe(System.out::println);
    }

    public TaskStream pipe(MunchPlugin plugin) {
        Observable<TaskFile> ref = this.files.share();

        this.files.subscribe(new Action1<TaskFile>() {
            @Override
            public void call(TaskFile file) {
                plugin.transform(file);
            }
        });

        return new TaskStream(ref);
    }
}
