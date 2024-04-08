import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import constant.LoginResult;
import utils.ChromeConfig;
import utils.ReadExcelUtil;
import utils.WriteExcelUtil;

public class TestLoginMuabantinhbien {
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
		new WriteExcelUtil("test-case.xlsx", "LOG", 0, 3);
		new ReadExcelUtil("test-case.xlsx", "LOG", 0, 3);
		xPaths = new HashMap<String, String>() {
			{
				put("btnLogin", "//*[@id=\"customer_login\"]/div[1]/form/p[3]/button");
				put("btnLogout", "//*[@id=\"main\"]/div[2]/div/div/p[1]/a");
				put("txtUsername", "//*[@id=\"username\"]");
				put("txtPassword", "//*[@id=\"password\"]");
			}
		};
	}

	@DataProvider(name = "loginData")
	public Object[][] getLoginData() {
		String[] params = new String[] { "username", "password" };
		Object[][] loginData = ReadExcelUtil.getTestData(params);
		testCaseCount = loginData.length;
		return loginData;
	}

	@Test(dataProvider = "loginData")
	public void testLoginMuaBanTinhBien(String rowIndex,String testCaseId, String caseNum, String username, String password,
			String expectedResult) {
		WebDriver driver = ChromeConfig.getDriverAllSetUp();
		driver.get(url);

		doLogin(driver, username, password);
		ChromeConfig.setTimeout(5);
		String actual = LoginResult.FAILED.getValue();
		try {
			WebElement logoutButton = driver.findElement(By.xpath(xPaths.get("btnLogout")));
			actual = LoginResult.SUCCEED.getValue();
		} catch (NoSuchElementException e) {
		}
		driver.quit();

		try {
			System.out.println(">>expected: " + expectedResult + ">>actual: " + actual);
			actualResult.append("Case ").append(caseNum).append(": ").append(actual).append(".\n");
			Assert.assertTrue(expectedResult.contains(actual), "testLogin failed at case: " + caseNum);
		} catch (AssertionError e) {
			this.wasSuccessfully = false;
			try {				
				WriteExcelUtil.writeTestDefect(testCaseId+"_"+caseNum, e.getLocalizedMessage());
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

	private void doLogin(WebDriver driver, String username, String password) {
		WebElement usernameInput = driver.findElement(By.xpath(xPaths.get("txtUsername")));
		usernameInput.sendKeys(username);
		WebElement passwordInput = driver.findElement(By.xpath(xPaths.get("txtPassword")));
		passwordInput.sendKeys(password);
		WebElement loginButton = driver.findElement(By.xpath(xPaths.get("btnLogin")));
		loginButton.click();
	}
	
	
	@AfterClass
	public void tearDown() {
		System.out.println(">>terminated");
	}
}
