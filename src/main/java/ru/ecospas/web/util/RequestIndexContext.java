package ru.ecospas.web.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestIndexContext {

    public HttpServletRequest req;
    public int index;

    public RequestIndexContext(HttpServletRequest req, int index) {
        this.req = req;
        this.index = index;
    }
}