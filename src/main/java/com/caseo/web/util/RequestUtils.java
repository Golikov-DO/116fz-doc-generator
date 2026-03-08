package com.caseo.web.util;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;

public class RequestUtils {

    // -------- SINGLE VALUE --------

    public static String param(HttpServletRequest req, String name) {
        return req.getParameter(name);
    }

    public static Integer paramInt(HttpServletRequest req, String name) {
        try {
            String value = req.getParameter(name);
            return value != null && !value.isBlank() ? Integer.parseInt(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean paramBool(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v != null && (v.equals("true") || v.equals("on") || v.equals("1"));
    }

    public static LocalDate paramDate(HttpServletRequest req, String name) {
        try {
            String value = req.getParameter(name);
            return value != null && !value.isBlank() ? LocalDate.parse(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    // -------- ARRAY VALUE --------

    public static String param(HttpServletRequest req, String name, int index) {
        String[] arr = req.getParameterValues(name);
        return arr != null && arr.length > index ? arr[index] : null;
    }

    public static Integer paramInt(HttpServletRequest req, String name, int index) {
        try {
            String value = param(req, name, index);
            return value != null ? Integer.parseInt(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDate paramDate(HttpServletRequest req, String name, int index) {
        try {
            String value = param(req, name, index);
            return value != null && !value.isBlank() ? LocalDate.parse(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean paramBool(HttpServletRequest req, String name, int index) {
        String v = param(req, name, index);
        return v != null && (v.equals("true") || v.equals("on") || v.equals("1"));
    }
}