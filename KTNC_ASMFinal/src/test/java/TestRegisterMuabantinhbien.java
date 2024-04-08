import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import constant.RegisterResult;
import utils.ChromeConfig;
import utils.ReadExcelUtil;
import utils.WriteExcelUtil;

public class TestRegisterMuabantinhbien {
	private final static String url = "https://muabantinhbien.com/my-account/";
	private Map<String, String> xPaths;
	private boolean wasSuccessfully = true;
	private StringBuilder actualResult;
	private int testCaseCount = 0;

	@BeforeClass
	public void setUp() {
		System.out.println(">>>set up");
		this.wasSuccessfully = true;
		actualResult = new StringBuilder();
		new WriteExcelUtil("test-case.xlsx", "REG", 0, 3);
		new ReadExcelUtil("test-case.xlsx", "REG", 0, 3);
		xPaths = new HashMap<String, String>() {
			{
				put("btnRegister", "//*[@id=\"customer_login\"]/div[2]/form/p[4]/button");
				put("btnLogout", "//*[@id=\"main\"]/div[2]/div/div/p[1]/a");
				put("txtUsername", "//*[@id=\"reg_username\"]");
				put("txtPassword", "//*[@id=\"reg_password\"]");
				put("txtEmail", "//*[@id=\"reg_email\"]");
				put("txtLog", "//*[@id=\"main\"]/div[2]/div/div[1]/ul/li");
			}
		};
	}

	@DataProvider(name = "registerData")
	public Object[][] getRegisterData() {
		String[] params = new String[] { "username", "email", "password" };
		Object[][] loginData = ReadExcelUtil.getTestData(params);
		testCaseCount = loginData.length;
		return loginData;
	}

	@Test(dataProvider = "registerData")
	public void testLoginMuaBanTinhBien(String rowIndex, String testCaseId, String caseNum, String username,
			String email, String password, String expectedResult) {
		WebDriver driver = ChromeConfig.getDriverAllSetUp();
		driver.get(url);

		doRegister(driver, username, email, password);
		String actual = RegisterResult.FAILED_INVALID_INPUT.getValue();
		ChromeConfig.setTimeout(5);
		try {
			String logContent = driver.findElement(By.xpath(xPaths.get("txtLog"))).getText();
			System.out.println("log content: >>" + logContent);
			actual = (logContent.contains(RegisterResult.FAILED_USERNAME.getValue()))
					? RegisterResult.FAILED_USERNAME.getValue()
					: RegisterResult.FAILED_EMAIL.getValue();
		} catch (NoSuchElementException e) {
			actual = RegisterResult.SUCCEED.getValue();
		}
		driver.quit();

		try {
			System.out.println(">>expected: " + expectedResult + ">>actual: " + actual);
			actualResult.append("Case ").append(caseNum).append(": ").append(actual).append(".\n");
			Assert.assertTrue(expectedResult.contains(actual), "testRegister failed at case: " + caseNum);
		} catch (AssertionError e) {
			this.wasSuccessfully = false;
			try {
				WriteExcelUtil.writeTestDefect(testCaseId + "_" + caseNum, e.getLocalizedMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			throw e;
		} finally {
			System.out.println(">>Result: " + actualResult.toString());
			System.out.println(">>caseNum: " + Integer.valueOf(caseNum));
			System.out.println(">>testCaseCount: " + this.testCaseCount);

			if (Integer.valueOf(caseNum) == this.testCaseCount) {
				System.out.println("abc");
				WriteExcelUtil.writeActualResult(Integer.valueOf(rowIndex), actualResult.toString(), wasSuccessfully);
			}
		}
	}

	private void doRegister(WebDriver driver, String username, String email, String password) {
		WebElement usernameInput = driver.findElement(By.xpath(xPaths.get("txtUsername")));
		usernameInput.sendKeys(username);
		WebElement emailInput = driver.findElement(By.xpath(xPaths.get("txtEmail")));
		emailInput.sendKeys(email);
		WebElement passwordInput = driver.findElement(By.xpath(xPaths.get("txtPassword")));
		passwordInput.sendKeys(password);
		WebElement registerButton = driver.findElement(By.xpath(xPaths.get("btnRegister")));
		registerButton.click();
	}

	@AfterClass
	public void tearDown() {
		System.out.println(">>terminated");
	}
}
