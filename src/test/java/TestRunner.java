import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/scenarios",
        glue = {"stepDefinitions", "context"},
        plugin = {"pretty", "html:target/cucumber-reports"},
        monochrome = true,
        tags = "@VerifyCrudActions"
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
