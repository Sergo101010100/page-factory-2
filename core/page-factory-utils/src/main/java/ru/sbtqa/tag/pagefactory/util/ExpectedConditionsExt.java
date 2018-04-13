package ru.sbtqa.tag.pagefactory.util;

import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.qautils.managers.DateManager;

public class ExpectedConditionsExt {

    private static final Logger LOG = LoggerFactory.getLogger(ExpectedConditionsExt.class);
//    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    /**
     * Wait until element present
     *
     * @param webElement Desired web element
     * @return Expected WebElement
     */
    public static WebElement waitUntilElementPresent(WebDriver driver, WebElement webElement) {
        return new WebDriverWait(driver, 1).
                until(ExpectedConditions.visibilityOf(webElement));
    }

    /**
     * Wait until element present
     *
     * @param webElement Desired web element
     * @param timeout    Timeout in seconds
     * @return Expected WebElement
     */
    public static WebElement waitUntilElementPresent(WebDriver driver, WebElement webElement, int timeout) {
        return new WebDriverWait(driver, timeout).
                until(ExpectedConditions.visibilityOf(webElement));
    }

    /**
     * Wait until element present
     *
     * @param webElement Desired web element
     * @return Expected WebElement
     */
    public static WebElement waitUntilElementToBeClickable(WebDriver driver, WebElement webElement) {
        return new WebDriverWait(driver, 1).
                until(ExpectedConditions.elementToBeClickable(webElement));
    }

    /**
     * Wait until element present
     *
     * @param webElement Desired web element
     * @param timeout    Timeout in seconds
     * @return Expected WebElement
     */
    public static WebElement waitUntilElementToBeClickable(WebDriver driver, WebElement webElement, int timeout) {
        return new WebDriverWait(driver, timeout).
                until(ExpectedConditions.elementToBeClickable(webElement));
    }

    /**
     * Wait until element is present and enable to check page prepare to work
     *
     * @param webElement Desired web element
     */
    public static void waitUntilPagePrepared(WebDriver driver, WebElement webElement) {
        try {
            new WebDriverWait(driver, 1 / 2).
                    until(ExpectedConditions.visibilityOf(webElement));
        } catch (Exception | AssertionError e) {
            LOG.debug("Element {} does not become visible after timeout", webElement, e);
            driver.navigate().refresh();
            LOG.debug("WebElementsPage refreshed");
            new WebDriverWait(driver, 1).
                    until(ExpectedConditions.visibilityOf(webElement));
        }
    }

    /**
     * Wait until element present
     *
     * @param by a {@link By} object.
     * @return return appeared WebElement
     */
    public static WebElement waitUntilElementAppearsInDom(WebDriver driver, By by) {
        return new WebDriverWait(driver, 1)
                .until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * Wait until element present
     *
     * @param by      a {@link By} object.
     * @param timeout timeout in seconds
     * @return return appeared WebElement
     */
    public static WebElement waitUntilElementAppearsInDom(WebDriver driver, By by, long timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * Wait until element gone from dom
     *
     * @param timeout    in milliseconds
     * @param webElement a {@link WebElement} object.
     */
    public static void waitUntilElementGoneFromDom(WebElement webElement, long timeout) {
        Long start = DateManager.getCurrentTimestamp();
        while (DateManager.getCurrentTimestamp() < start + timeout) {
            try {
                if (!webElement.isDisplayed()) {
                    return;
                }
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                LOG.debug("There is no element {} in dom", webElement, e);
                return;
            }
            sleep(1);
        }
        throw new NoSuchElementException("Timed out after " + timeout + " milliseconds waiting for web element '" + webElement.toString() + "' gone from DOM");
    }

    /**
     * @param element a {@link WebElement} object.
     */
    public static void waitUntilElementGetInvisible(WebDriver driver, WebElement element) {
        new WebDriverWait(driver, 1)
                .until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
    }

    /**
     * <p>
     * waitForElementGetEnabled.</p>
     *
     * @param webElement a {@link WebElement} object.
     * @param timeout    a long.
     * @throws WaitException TODO
     */
    public static void waitForElementGetEnabled(WebElement webElement, long timeout) throws WaitException {
        long timeoutTime = DateManager.getCurrentTimestamp() + timeout;
        while (timeoutTime > DateManager.getCurrentTimestamp()) {
            sleep(1);
            try {
                if (webElement.isEnabled()) {
                    return;
                }
            } catch (Exception e) {
                LOG.debug("Target element still not enable", e);
            }

        }
        throw new WaitException("Timed out after '" + timeout + "' milliseconds waiting for availability of '" + webElement + "'");
    }

    /**
     * Accept any alert regardless of its message
     *
     * @throws WaitException if alert didn't appear during timeout
     */
    public static void acceptAlert(WebDriver webDriver) throws WaitException {
        interactWithAlert(webDriver,"", true);
    }

    /**
     * Dismiss any alert regardless of its message
     *
     * @throws WaitException if alert didn't appear during timeout
     */
    public static void dismissAlert(WebDriver webDriver) throws WaitException {
        interactWithAlert(webDriver, "", false);
    }

    /**
     * Wait for an alert with corresponding text (if specified). Depending on the decision, either accept it or decline
     * If messageText is empty, text doesn't matter
     *
     * @param messageText text of an alert. If empty string is provided, it is being ignored
     * @param decision    true - accept, false - dismiss
     * @throws WaitException in case if alert didn't appear during default wait timeout
     */
    public static void interactWithAlert(WebDriver driver, String messageText, boolean decision) throws WaitException {
        long timeoutTime = System.currentTimeMillis() + 1000;

        while (timeoutTime > System.currentTimeMillis()) {
            try {
                Alert alert = driver.switchTo().alert();
                if (!messageText.isEmpty()) {
                    Assert.assertEquals(alert.getText(), messageText);
                }
                if (decision) {
                    alert.accept();
                } else {
                    alert.dismiss();
                }
                return;
            } catch (Exception e) {
                LOG.debug("Alert has not appeared yet", e);
            }
            sleep(1);
        }
        throw new WaitException("Timed out after '" + 1 + "' seconds waiting for alert to accept");
    }


    /**
     * @param text    a {@link String} object.
     * @param timeout a {int} object. wait text during sec period
     * @return true if exists
     */
    public static boolean checkElementWithTextIsPresent(WebDriver driver, String text, long timeout) {
        try {
            new WebDriverWait(driver, 1)
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), '" + text + "')]")));
            return true;
        } catch (TimeoutException e) {
            LOG.debug("Element with text {} is not located on page", text, e);
            return false;
        }
    }

    /**
     * @param sec a int.
     */
    private static void sleep(int sec) {
        try {
            Thread.sleep(sec * 100L);
        } catch (InterruptedException e) {
            LOG.warn("Error while thread is sleeping", e);
            Thread.currentThread().interrupt();
        }
    }
}
