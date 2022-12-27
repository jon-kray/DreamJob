package ru.ecosystem.dreamjob.app.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class DreamJobUtils {

    public String USER_ID_KEY = "USER_ID";

    public int generateId() {
        return Math.abs(new Random().nextInt());
    }
}
