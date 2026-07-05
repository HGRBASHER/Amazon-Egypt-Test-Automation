package com.amazon.qa.tests.searchResults;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/com/amazon/qa/tests/searchResults",
        glue = {"com.amazon.qa.tests"},
        plugin = {"pretty","html:report/loginRunner-reports.html",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}
)
public class SearchResultsRunner extends AbstractTestNGCucumberTests {
}
