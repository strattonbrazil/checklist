package com.github.strattonbrazil.checklist;

import java.util.ArrayList;
import rx.*;
import rx.functions.Action1;

public class TaskStream {
    final private Observable<TaskFile> files;

    public TaskStream(Observable<TaskFile> files) {
        this.files = files;
    }

    public TaskStream pipe(MunchPlugin plugin) {
        PluginContext ctx = new com.github.strattonbrazil.checklist.PluginContext();
        final ArrayList<TaskFile> currentFiles = new ArrayList<TaskFile>();
        this.files.subscribe(new Action1<TaskFile>() {
            @Override
            public void call(TaskFile file) {
                plugin.transform(ctx, file);
                currentFiles.add(file);
            }
        });

        // use the new files pushed from the plugin
        if (ctx.hasPushedFiles()) {
            return new TaskStream(Observable.from(ctx.getFiles()));
        }

        return new TaskStream(Observable.from(currentFiles));
    }
}
