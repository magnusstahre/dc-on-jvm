package com.pillartechnology.codemash;

import java.io.FileOutputStream;
import java.io.IOException;

public class DC {
    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream("DC.class");
        try {
        } finally {
           fos.close();
        }
    }
}