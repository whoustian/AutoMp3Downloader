package Selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageObject {

	public By locator;
	public String valueType;

	public PageObject(By locator, String valueType) {
		this.locator = locator;
		this.valueType = valueType;
	}

	public By getLocator() {
		return locator;
	}

	public void setLocator(By locator) {
		this.locator = locator;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public void setValue(WebDriver driver, String input) {
		WebElement element = driver.findElement(locator);
		element.sendKeys(input);
	}

	public void sendKeys(WebDriver driver, CharSequence keys) {
		WebElement element = driver.findElement(locator);
		element.sendKeys(keys);
	}

	public void click(WebDriver driver) {
		WebElement element = driver.findElement(locator);
		element.click();
	}

	public String getText(WebDriver driver) {
		WebElement element = driver.findElement(locator);
		return element.getText();
	}

	public void waitForVisible(WebDriver driver, int secondsToWait) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, secondsToWait);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception e) {
			System.out.println("Waited " + secondsToWait + " seconds for visiblity of element: " + locator
					+ ". Element was not visible.");
		}
	}

	public boolean isVisible(WebDriver driver) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

}
