package tests.com.amazon.qa.tests.searchResults;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/tests/SearchResults",
        glue = {"tests"},
        plugin = {"pretty","html:report/loginRunner-reports.html",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}
)
public class SearchResultsRunner extends AbstractTestNGCucumberTests {
}
