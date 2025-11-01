package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleWrapper;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepDefinitions", "hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-html-report",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "hooks.StepDetails"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider
    public Object[][] scenarios() {
        // Lấy tags từ system property
        String tags = System.getProperty("cucumber.filter.tags");
        System.out.println("tags: " + tags);
        Object[][] allScenarios = super.scenarios();

        if (tags == null || tags.trim().isEmpty()) {
            System.out.println("No tag filter found. Running all " + allScenarios.length + " scenarios.");
            return allScenarios;
        }

        System.out.println("Filtering scenarios with tags: " + tags);

        // Chuyển tags sang dạng lọc đơn giản (vd: @Smoke hoặc @Regression)
        String normalizedTag = tags.trim().replace("@", "").toLowerCase();

        // Lọc theo tag
        java.util.List<Object[]> filtered = new java.util.ArrayList<>();
        for (Object[] scenario : allScenarios) {
            PickleWrapper pickle = (PickleWrapper) scenario[0];
            boolean hasTag = pickle.getPickle().getTags().stream()
                    .anyMatch(t -> t.replace("@", "").toLowerCase().contains(normalizedTag));

            if (hasTag) {
                filtered.add(scenario);
            }
        }

        System.out.println("Found " + filtered.size() + " matching scenarios (from " + allScenarios.length + ")");
        return filtered.toArray(new Object[0][]);
    }
}
