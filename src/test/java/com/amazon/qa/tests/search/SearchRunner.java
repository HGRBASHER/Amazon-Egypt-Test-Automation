package tests.com.amazon.qa.tests.search;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/tests/Search",
        glue = {"tests"},
        plugin = {"pretty","html:report/loginRunner-reports.html",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}

)
public class SearchRunner extends AbstractTestNGCucumberTests {
}
