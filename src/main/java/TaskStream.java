package com.github.strattonbrazil.checklist;

import java.nio.file.*;
import java.util.ArrayList;
import rx.*;
import rx.functions.Action1;

public class TaskStream {
    final private Observable<TaskFile> files;
    final private Path cwd;

    public TaskStream(Path cwd, Observable<TaskFile> files) {
        this.cwd = cwd;
        this.files = files;
    }

    public TaskStream pipe(MunchPlugin plugin) {
        PluginContext ctx = new PluginContext(cwd);
        final ArrayList<TaskFile> currentFiles = new ArrayList<TaskFile>();
        this.files.subscribe(new Action1<TaskFile>() {
            @Override
            public void call(TaskFile file) {
                plugin.transform(ctx, file);
                currentFiles.add(file);
            }
        });
        plugin.complete(ctx);

        // use the new files pushed from the plugin
        if (ctx.hasPushedFiles()) {
            return new TaskStream(cwd, Observable.from(ctx.getFiles()));
        }

        return new TaskStream(cwd, Observable.from(currentFiles));
    }
}
