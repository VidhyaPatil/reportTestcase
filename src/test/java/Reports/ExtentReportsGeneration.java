package Reports;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ExtentReportsGeneration {
	WebDriver driver;
	ExtentReports extent;
	ExtentTest eTest;
	public static Logger logger;
	@BeforeTest
	public void init() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		logger=LogManager.getLogger("ExtentReportDemo");
		driver.get("http://omayo.blogspot.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		logger.info("Web page open successfuly");
		String path = System.getProperty("user.dir") + "\\report\\index.html";
		ExtentSparkReporter reporter = new ExtentSparkReporter(path);
		reporter.config().setDocumentTitle("ExtentReport");
		reporter.config().setReportName("Demo Document");
		extent = new ExtentReports();
		extent.attachReporter(reporter);
		
	}

	@Test
	public void getText() {
		System.out.println("WebPage title:" + driver.getTitle());

		// Reports
		eTest = extent.createTest("GetTitle");
		eTest.info("This is get title test");

	}

	@Test
	public void getURL() {
		System.out.println("WebPage URL:" + driver.getCurrentUrl());

		// Reports
		eTest = extent.createTest("Page URL");
		eTest.info("This is get URL test");

	}

	@Test
	public void failTest() throws IOException {
		File path=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(path, new File(System.getProperty("user.dir")+"\\Screenshot\\photo.png"));
		eTest = extent.createTest("Asserst Fail");
		//eTest.log(Status.FAIL, "Failed Test Case");
		String screenshotPath = System.getProperty("user.dir") + "./Screenshot/photo.png";
		File file = new File(screenshotPath);
		if (file.exists()) {
			eTest.fail("screenshot : " + eTest.addScreenCaptureFromPath(screenshotPath));
		}
		driver.findElement(By.linkText("Hom")).click();
		
		logger.info("Failuer report generated successfuly");

	}

	@AfterTest
	public void tearOf() {
		driver.quit();
		extent.flush();
	}
}
