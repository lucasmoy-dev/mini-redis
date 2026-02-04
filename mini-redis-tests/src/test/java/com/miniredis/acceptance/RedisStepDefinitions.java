package com.miniredis.acceptance;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.annotation.PostConstruct;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.miniredis.MiniRedisApplication.class)
public class RedisStepDefinitions {

    @LocalServerPort
    private int port;

    private Response lastResponse;

    @PostConstruct
    public void setup() {
        RestAssured.port = port;
    }

    @When("I execute command {string}")
    public void i_execute_command(String command) {
        lastResponse = RestAssured.given()
                .queryParam("cmd", command)
                .get("/");
    }

    @Then("the response should be {string}")
    public void the_response_should_be(String expectedResponse) {
        lastResponse.then().body(equalTo(expectedResponse));
    }

    @Then("the response should contain {string}")
    public void the_response_should_contain(String expectedPart) {
        lastResponse.then().body(containsString(expectedPart));
    }
}
