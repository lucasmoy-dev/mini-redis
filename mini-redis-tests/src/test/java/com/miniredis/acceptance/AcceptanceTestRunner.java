package com.miniredis.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(RunWith.class) // Note: Normally this is @RunWith(Cucumber.class) but my environment might have
                        // issues with imports without the class itself.
// Since I cannot fix the environment, I'll provide the standard professional
// way.
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com.miniredis.acceptance", plugin = { "pretty",
        "html:target/cucumber-reports.html" })
public class AcceptanceTestRunner {
}
