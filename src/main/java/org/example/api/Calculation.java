package org.example.api;

import java.util.Arrays;

public class Calculation {
    public static int add(int x, int y) {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        return x + y;
    }
}
