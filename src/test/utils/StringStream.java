package utils;

import java.io.IOException;
import java.io.InputStream;

public class StringStream extends InputStream {

    private final byte[] buffer;
    int position = 0;

    public StringStream(String buffer) {
        this.buffer = buffer.getBytes();
    }

    public int read() throws IOException {
        if (position >= buffer.length) {
            return -1;
        } else {
            return buffer[position++];
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
        position = 0;
    }
}
