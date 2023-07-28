package ru.ultrabasic.bstutp;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;

public class WebTestExample {
    @BeforeEach
    public void openPage() {
        Configuration.browser = "firefox";
        Selenide.open("http://localhost:8080/");
    }

    @Test
    public void regularTest() {
        $("button").shouldHave(Condition.text("Сделать заявление"));
    }
}
