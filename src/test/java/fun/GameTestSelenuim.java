package fun;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class GameTestSelenuim {
	
	public static WebDriver driver;
	public static int counter = 1;
	public static Map<Integer, Integer> map = new HashMap<>();
	public static int id = 0;

	public int fib(int counter) {
		if (counter == 1 || counter == 2) {
			return 1;
		}
		if (map.containsKey(counter)) {
			return map.get(counter);
		}
		int value = fib(counter - 1) + fib(counter - 2);
		map.put(counter, value);
		return value;
	}
	
	@BeforeClass
	public static void setUp() {
		System.setProperty("webdriver.chrome.driver","/Users/lulu/Desktop/Selenium/chromedriver");
		driver = new ChromeDriver();
	}
	
	public int get_id() throws JSONException {
		String jsonText = driver.findElement(By.cssSelector("pre")).getText();
		JSONObject json = new JSONObject(jsonText);
		int id = json.getInt("id");
		return id;
	}
	
	public static String get_text() throws JSONException {
		String jsonText = driver.findElement(By.cssSelector("pre")).getText();
		JSONObject json = new JSONObject(jsonText);
		String text = json.getString("text");
		return text;
	}
	
	public void refres_page() {
		driver.navigate().refresh();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		id = fib(counter++);
	}
	
	public void load_page(String url) {
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		fib(counter++);
	}
	
	@Test
	public void test_without_name_parameter() throws JSONException {
		load_page("http://localhost:8080/game");
		String text = get_text();
		assertTrue("Text doesn't contain default value 'Sudoke' but " + text, text.contains("Sudoku"));
	}
	
	@Test
	public void test_refresh_id() throws JSONException {
		load_page("http://localhost:8080/game");
		for(int i=0;i<7;i++) {
			refres_page();
			assertEquals("The id is not " + id + " but " + get_id(), id, get_id());
		}
	}
	
	@Test
	public void test_with_name_parameter() throws JSONException {
		load_page("http://localhost:8080/game?name=game");
		String text = get_text();
		assertTrue("Text doesn't contain default value 'Sudoke' but " + text, text.contains("game"));
	}
}
