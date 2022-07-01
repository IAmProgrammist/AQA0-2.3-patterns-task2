package ru.netology.aqa0patterns.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.UtilityClass;

import java.util.Locale;

import static io.restassured.RestAssured.given;

@UtilityClass
public class DataGenerator {
    private final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private final Faker faker = new Faker(new Locale("en"));
    private final int MIN_PASSWORD_LENGTH = 6;
    private final int ADD_PASSWORD_LENGTH = 10;
    private final char PASSWORD_LOWER_BOUND = '0';
    private final char PASSWORD_UPPER_BOUND = '~';

    private static void sendRequest(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
        .when()
                .post("/api/system/users")
        .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return faker.name().firstName() + faker.name().lastName() + faker.random().nextInt(1900, 2022);
    }

    public static String getRandomPassword() {
        String password = "";
        for(int i = 0; i < MIN_PASSWORD_LENGTH + (faker.random().nextInt(0, ADD_PASSWORD_LENGTH)); i++){
            password += ((char)(faker.random().nextInt(
                    (PASSWORD_UPPER_BOUND - PASSWORD_LOWER_BOUND) + 1) + PASSWORD_LOWER_BOUND));
        }
        return password;
    }

    @UtilityClass
    public static class Registration {
        
        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
        }

        public static RegistrationDto getRegisteredUser(String status) {
            RegistrationDto registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

    @Data
    @AllArgsConstructor
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
