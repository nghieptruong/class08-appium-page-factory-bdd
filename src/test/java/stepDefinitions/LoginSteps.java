package stepDefinitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import pages.abstracts.CatalogPage;
import pages.abstracts.LoginPage;
import pages.factories.PageManagerFactory;

public class LoginSteps {

    private Logger logger = LogManager.getLogger(LoginSteps.class);

    private CatalogPage catalogPage;
    private LoginPage loginPage;
    final String username = "bod@example.com";
//    final String username = "";
    final String password = "10203040";

    public LoginSteps() {
        loginPage = PageManagerFactory.getLoginPage();
        catalogPage = PageManagerFactory.getCatalogPage();
    }

    @When("the user navigates to login page")
    public void the_user_navigates_to_login_page() {
        catalogPage.getNavigationBar().openMainMenu();
        catalogPage.getNavigationBar().openLoginPage();
    }

    @When("the user logins with valid username and password")
    public void the_user_logins_with_valid_username_and_password() {
        loginPage.login(username, password);
    }

    @When("the user logins with {string} username and {string} password")
    public void the_user_logins_with_username_and_password(String userName, String pwd) {
        loginPage.login(userName, pwd);
    }

    @When("^the user enters with \".*\" username and \".*\" password$")
    public void enters_with_username_and_password(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("the user should be redirected to Catalog page")
    public void the_user_should_be_redirected_to_catalog_page() {
        Assert.assertTrue(catalogPage.isPageDisplayed());
    }

    @And("the user logins with invalid username and password")
    public void theUserLoginsWithInvalidUsernameAndPassword() {
        System.out.println("And the user logins with invalid username and password");
    }

    @Then("the error message displays {string}")
    public void theErrorMessageDisplays(String error) {
        logger.info("the error message displays " + error);
        //assertion...
    }

    @And("logout menu item displays")
    public void logoutMenuItemDisplays() {
        catalogPage.getNavigationBar().openMainMenu();
        Assert.assertTrue(catalogPage.getNavigationBar().isLogoutMenuItemDisplayed());
    }
}
