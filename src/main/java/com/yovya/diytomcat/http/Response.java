package com.yovya.diytomcat.http;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Response {
    private PrintWriter writer;
    private StringWriter stringWriter;
    private String contentType;

    public Response() {
        this.stringWriter = new StringWriter();
        this.writer = new PrintWriter(stringWriter);
        //目前固定为text/html
        this.contentType = "text/html";
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public byte[] getBytes() {
        writer.flush();
        return stringWriter.toString().getBytes();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
