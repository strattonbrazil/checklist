package com.github.strattonbrazil.checklist;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;

class DestPlugin implements MunchPlugin {
    final public String path;

    public DestPlugin(String path) {
        this.path = path;
    }

    public void transform(PluginContext ctx, TaskFile file) {
        // write file
        Path cwd = Paths.get("").toAbsolutePath();
        copyFile(file.buffer, cwd.relativize(file.path));

        System.out.println("copying task file: " + file + " to " + path);
    }

    private void copyFile(ByteBuffer buffer, Path relativePath) {
        Path destPath = Paths.get(this.path, relativePath.toString());

        // create the full directory path
        destPath.getParent().toFile().mkdirs();

        try {
            FileChannel channel = new FileOutputStream(destPath.toFile()).getChannel();
            channel.write(buffer);
            channel.close();
        } catch (IOException e) {
            // TODO: handle this exception
            System.err.println(e.getMessage());
        }
    }
}
