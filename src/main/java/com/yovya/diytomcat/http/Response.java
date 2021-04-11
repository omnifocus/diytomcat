package com.yovya.diytomcat.http;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Response {
    private PrintWriter writer;
    private StringWriter stringWriter;

    public Response() {
        this.stringWriter = new StringWriter();
        this.writer = new PrintWriter(stringWriter);
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public byte[] getBytes() {
        writer.flush();
        return stringWriter.toString().getBytes();
    }


}
