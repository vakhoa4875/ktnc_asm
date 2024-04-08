package utils;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeConfig {
	public static WebDriver getDriverAllSetUp() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Program Files\\Google\\Chrome\\chromedriver-win64\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		setUpDriver(driver);
		return driver;
	}

	private static void setUpDriver(WebDriver driver) {
		// Khởi tạo tùy chọn cho trình duyệt Chrome
		ChromeOptions options = new ChromeOptions();
		options.setPageLoadStrategy(PageLoadStrategy.EAGER);
		// Tắt chế độ ẩn danh (private mode)
		options.setCapability("ms:inPrivate", false);
	}

	public static void setTimeout(int sec) {
		try {
			TimeUnit.SECONDS.sleep(sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
