package ru.netology;

import java.io.IOException;
import java.io.OutputStream;

public interface Handler {
    void process(Request request, OutputStream outputStream) throws IOException;
}
