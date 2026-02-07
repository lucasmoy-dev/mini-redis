package com.miniredis.acceptance;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import io.cucumber.spring.CucumberContextConfiguration;
import com.miniredis.MiniRedisApplication;

import javax.annotation.PostConstruct;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@CucumberContextConfiguration
@SpringBootTest(classes = MiniRedisApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisStepDefinitions {

    @LocalServerPort
    private int port;

    @org.springframework.beans.factory.annotation.Autowired
    private com.miniredis.domain.ports.in.RedisPort redisService;

    private Response lastResponse;

    @io.cucumber.java.Before
    public void clearDatabase() {
        redisService.flushAll();
    }

    @PostConstruct
    public void setup() {
        RestAssured.port = port;
    }

    @When("I execute command {string}")
    public void i_execute_command(String command) {
        lastResponse = RestAssured.given()
                .queryParam("cmd", command)
                .get("/api");
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
