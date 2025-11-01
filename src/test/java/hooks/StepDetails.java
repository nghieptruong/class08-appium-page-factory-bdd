package hooks;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A Cucumber plugin to log step details and track step failures/skips within a scenario.
 */
public class StepDetails implements ConcurrentEventListener {

    private final Logger LOG = LogManager.getLogger(StepDetails.class);

    // Static variables to track the status across steps within a scenario
    private static boolean stepFailed = false;
    private static boolean stepSkipped = false;

    // Handlers for the Cucumber events
    private final EventHandler<TestStepStarted> stepStartedHandler = this::handleTestStepStarted;
    private final EventHandler<TestStepFinished> stepFinishedHandler = this::handleTestStepFinished;
    private final EventHandler<TestCaseStarted> scenarioStartedHandler = this::handleTestScenarioStarted;

    /**
     * Registers the event handlers with the Cucumber EventPublisher.
     * @param publisher The event publisher to register handlers with.
     */
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, scenarioStartedHandler);
    }

    /**
     * Handles the start of a test step. Logs the step text if it hasn't failed or been skipped.
     * @param event The TestStepStarted event.
     */
    private void handleTestStepStarted(TestStepStarted event) {
        // Check if the test step is a PickleStepTestStep (i.e., a Gherkin step)
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();

            // Only log the step if the scenario hasn't seen a failed or skipped step yet
            if (!stepFailed && !stepSkipped) {
                String keyword = testStep.getStep().getKeyword();
                String text = testStep.getStep().getText();
                LOG.info("STEP: " + keyword + text);
            }
        }
    }

    /**
     * Handles the finish of a test step. Updates the stepFailed/stepSkipped flags and logs errors.
     * @param event The TestStepFinished event.
     */
    private void handleTestStepFinished(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            Result result = event.getResult();

            // Check for step failure
            if (result.getStatus() == Status.FAILED) {
                stepFailed = true;
                Throwable error = result.getError();
                // Assuming LogUtil.getStackTrace is a utility method to format the stack trace.
                LOG.error(error.getMessage());
            }

            // Check for step skip
            if (result.getStatus() == Status.SKIPPED) {
                stepSkipped = true;
            }
        }
    }

    /**
     * Handles the start of a new test scenario (TestCaseStarted). Resets the tracking flags.
     * @param event The TestCaseStarted event.
     */
    private void handleTestScenarioStarted(TestCaseStarted event) {
        stepFailed = false;
        stepSkipped = false;
    }

    // Getters for the static fields (optional, but good practice for external access)
    public static boolean isStepFailed() {
        return stepFailed;
    }

    public static boolean isStepSkipped() {
        return stepSkipped;
    }
}
