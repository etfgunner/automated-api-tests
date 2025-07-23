package com.api.tests.utils;

import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

@Slf4j
public class TestDataGenerator {
    private static final Faker faker = new Faker();

    public static String generateRandomName() {
        return faker.name().fullName();
    }

    public static String generateRandomBookTitle() {
        return faker.book().title();
    }

    public static int generateRandomYear() {
        return faker.number().numberBetween(1950, 2024);
    }


}