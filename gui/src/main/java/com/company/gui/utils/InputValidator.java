package com.company.gui.utils;

/**
 * @author Mike Kostenko on 22/04/2024
 */
public class InputValidator {
    public static boolean isHostnameValid(String hostname) {
        return hostname.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$") || "localhost".equals(hostname);
    }

    public static boolean isPortValid(String port) {
        return port.matches("^[0-9]{1,5}$");
    }
}
