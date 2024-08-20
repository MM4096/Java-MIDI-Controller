package com.mm4096.midicontroller.tools;

public class Tools {
    public static String generateUID(int length) {
        StringBuilder uid = new StringBuilder();
        for (int i = 0; i < length; i++) {
            uid.append((char) (Math.random() * 26 + 97));
        }
        return uid.toString();
    }
}
