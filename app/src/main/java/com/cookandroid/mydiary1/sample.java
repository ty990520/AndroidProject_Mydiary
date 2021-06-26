package com.cookandroid.mydiary1;

public class sample {
    private static final sample ourInstance = new sample();

    public static sample getInstance() {
        return ourInstance;
    }

    private sample() {
    }
}