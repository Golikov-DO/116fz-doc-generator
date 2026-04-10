package ru.ecospas.web.util;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalTime;

public class RequestUtils {

    // -------- SINGLE VALUE --------

    public static String param(HttpServletRequest req, String name) {
        return req.getParameter(name);
    }

    public static int paramInt(HttpServletRequest req, String name) {
        try {
            String value = req.getParameter(name);
            return value != null && !value.isBlank() ? Integer.parseInt(value) : 0;
        } catch (Exception e) {
            return 0;
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

    public static int paramInt(HttpServletRequest req, String name, int index) {
        try {
            String value = param(req, name, index);
            return value != null ? Integer.parseInt(value) : 0;
        } catch (Exception e) {
            return 0;
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

    public static LocalTime paramTime(HttpServletRequest req, String hName, String mName) {
        try {
            String h = req.getParameter(hName);
            String m = req.getParameter(mName);

            if (h == null || h.isBlank()) return null;

            int hours = Integer.parseInt(h);
            int minutes = (m != null && !m.isBlank()) ? Integer.parseInt(m) : 0;

            return LocalTime.of(hours, minutes);
        } catch (Exception e) {
            return null;
        }
    }
}