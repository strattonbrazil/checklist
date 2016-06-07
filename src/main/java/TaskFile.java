package com.github.strattonbrazil.checklist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

public class TaskFile {
    final Path path;
    final ByteBuffer buffer;

    public TaskFile(Path path) {
        this.path = path;

        MappedByteBuffer buffer = null;

        try {
            RandomAccessFile file = new RandomAccessFile(path.toFile(), "r");

            FileChannel channel = file.getChannel();
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            buffer.load();
        } catch (FileNotFoundException e) {
            // TODO: handle this elegantly
        } catch (IOException e) {
            // TODO: handle this elegantly
        }

        this.buffer = buffer;
    }
}
