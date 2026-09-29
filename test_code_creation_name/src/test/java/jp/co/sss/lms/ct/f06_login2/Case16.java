package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト ログイン機能②
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// トップページにアクセス
		goTo("http://localhost:8080/lms");

		// 画面表示の確認
		assertEquals("ログイン", webDriver.findElement(By.tagName("h2")).getText());
		assertEquals("", webDriver.findElement(By.name("loginId")).getAttribute("value"));
		assertEquals("", webDriver.findElement(By.name("password")).getAttribute("value"));
		assertTrue(webDriver.findElement(By.className("btn-primary")).isEnabled());

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {

		// ログインIDの入力
		final WebElement loginId = webDriver.findElement(By.name("loginId"));
		loginId.clear();
		loginId.sendKeys("StudentAA06");

		// パスワードの入力
		final WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("StudentAA06");

		// 入力値が入力されている状態のエビデンスの取得
		getEvidence(new Object() {
		}, "before");

		// 「ログイン」ボタンの押下
		final WebElement button = webDriver.findElement(By.className("btn-primary"));
		button.click();

		// 利用規約画面の表示確認
		assertEquals("利用規約", webDriver.findElement(By.tagName("h2")).getText());
		assertTrue(webDriver.findElement(By.name("securityFlg")).isDisplayed());

		// 画面遷移後のエビデンスの取得
		getEvidence(new Object() {
		}, "after");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {

		// 画面スクロール
		scrollTo("200");

		// 「同意します」チェックボックスにチェックを入れる
		final WebElement check = webDriver.findElement(By.name("securityFlg"));
		check.click();

		// チェックボックスにチェックされている状態のエビデンスの取得
		getEvidence(new Object() {
		}, "before");

		// 「次へ」ボタンを押下
		final WebElement button = webDriver.findElement(By.className("btn-primary"));
		button.click();

		// パスワード変更画面の表示確認
		assertEquals("パスワード変更", webDriver.findElement(By.tagName("h2")).getText());
		assertEquals("", webDriver.findElement(By.name("currentPassword")).getAttribute("value"));
		assertEquals("", webDriver.findElement(By.name("password")).getAttribute("value"));
		assertEquals("", webDriver.findElement(By.name("passwordConfirm")).getAttribute("value"));

		// 画面遷移後のエビデンスの取得
		getEvidence(new Object() {
		}, "after");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() {

		// パスワードが未入力状態のエビデンスの取得
		getEvidence(new Object() {
		}, "before");

		// 画面スクロール
		scrollTo("200");

		// 「変更」ボタンの押下
		final WebElement button = webDriver.findElement(By.xpath("//button[text()='変更']"));
		button.click();

		// ダイアログの「変更」ボタンの押下
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("upd-btn")));
		final WebElement updButton = webDriver.findElement(By.id("upd-btn"));
		updButton.click();

		// 画面スクロール
		scrollTo("100");

		// コース詳細画面の表示確認
		assertEquals("パスワード変更", webDriver.findElement(By.tagName("h2")).getText());

		// エラーメッセージの表示確認
		assertTrue(webDriver.findElement(By.xpath("//input[@name='currentPassword']/following-sibling::ul//span"))
				.getText().contains("現在のパスワードは必須です。"));
		assertTrue(webDriver.findElement(By.xpath("//input[@name='password']/following-sibling::ul//span"))
				.getText().contains("パスワードは必須です。"));
		assertTrue(webDriver.findElement(By.xpath("//input[@name='passwordConfirm']/following-sibling::ul//span"))
				.getText().contains("確認パスワードは必須です。"));

		// 画面遷移後のエビデンスの取得
		getEvidence(new Object() {
		}, "after");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 間違った現在のパスワードを入力し「変更」ボタン押下")
	void test05() {

		// 現在のパスワードの入力
		final WebElement currentPassword = webDriver.findElement(By.name("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA99");

		// 新しいパスワードの入力
		final WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("StudentAA0666");

		// 確認パスワードの入力
		final WebElement passwordConfirm = webDriver.findElement(By.name("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("StudentAA0666");

		// 入力値が入力されている状態のエビデンスの取得
		getEvidence(new Object() {
		}, "before");

		// 画面スクロール
		scrollTo("200");

		// 「変更」ボタンの押下
		final WebElement button = webDriver.findElement(By.xpath("//button[text()='変更']"));
		button.click();

		// ダイアログの「変更」ボタンの押下
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("upd-btn")));
		final WebElement updButton = webDriver.findElement(By.id("upd-btn"));
		updButton.click();

		// 画面スクロール
		scrollTo("100");

		// コース詳細画面の表示確認
		assertEquals("パスワード変更", webDriver.findElement(By.tagName("h2")).getText());

		// エラーメッセージの表示確認
		assertTrue(webDriver.findElement(By.xpath("//input[@name='currentPassword']/following-sibling::ul//span"))
				.getText().contains("「現在のパスワード」と登録されているパスワードが一致しません。"));

		// 画面遷移後のエビデンスの取得
		getEvidence(new Object() {
		}, "after");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test06() {

		// 現在のパスワードの入力
		final WebElement currentPassword = webDriver.findElement(By.name("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA06");

		// 新しいパスワードの入力
		final WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("StudentAA12345678901234567890");

		// 確認パスワードの入力
		final WebElement passwordConfirm = webDriver.findElement(By.name("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("StudentAA12345678901234567890");

		// 入力値が入力されている状態のエビデンスの取得
		getEvidence(new Object() {
		}, "before");

		// 画面スクロール
		scrollTo("200");

		// 「変更」ボタンの押下
		final WebElement button = webDriver.findElement(By.xpath("//button[text()='変更']"));
		button.click();

		// ダイアログの「変更」ボタンの押下
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("upd-btn")));
		final WebElement updButton = webDriver.findElement(By.id("upd-btn"));
		updButton.click();

		// 画面スクロール
		scrollTo("100");

		// コース詳細画面の表示確認
		assertEquals("パスワード変更", webDriver.findElement(By.tagName("h2")).getText());

		// エラーメッセージの表示確認
		assertTrue(webDriver.findElement(By.xpath("//input[@name='password']/following-sibling::ul//span"))
				.getText().contains("パスワードの長さが最大値(20)を超えています。"));

		// 画面遷移後のエビデンスの取得
		getEvidence(new Object() {
		}, "after");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test07() {

		// 現在のパスワードの入力
		final WebElement currentPassword = webDriver.findElement(By.name("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA06");

		// 新しいパスワードの入力
		final WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("あいうえおかきくけこ");

		// 確認パスワードの入力
		final WebElement passwordConfirm = webDriver.findElement(By.name("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("あいうえおかきくけこ");

		// 入力値が入力されている状態のエビデンスの取得
		getEvidence(new Object() {
		}, "before");

		// 画面スクロール
		scrollTo("200");

		// 「変更」ボタンの押下
		final WebElement button = webDriver.findElement(By.xpath("//button[text()='変更']"));
		button.click();

		// ダイアログの「変更」ボタンの押下
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("upd-btn")));
		final WebElement updButton = webDriver.findElement(By.id("upd-btn"));
		updButton.click();

		// 画面スクロール
		scrollTo("100");

		// コース詳細画面の表示確認
		assertEquals("パスワード変更", webDriver.findElement(By.tagName("h2")).getText());

		// エラーメッセージの表示確認
		assertTrue(webDriver.findElement(By.xpath("//input[@name='password']/following-sibling::ul//span"))
				.getText().contains("「パスワード」には半角英数字のみ使用可能です。また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。"));

		// 画面遷移後のエビデンスの取得
		getEvidence(new Object() {
		}, "after");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test08() {

		// 現在のパスワードの入力
		final WebElement currentPassword = webDriver.findElement(By.name("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA06");

		// 新しいパスワードの入力
		final WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("StudentAA0666");

		// 確認パスワードの入力
		final WebElement passwordConfirm = webDriver.findElement(By.name("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("StudentAA1234");

		// 入力値が入力されている状態のエビデンスの取得
		getEvidence(new Object() {
		}, "before");

		// 画面スクロール
		scrollTo("200");

		// 「変更」ボタンの押下
		final WebElement button = webDriver.findElement(By.xpath("//button[text()='変更']"));
		button.click();

		// ダイアログの「変更」ボタンの押下
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("upd-btn")));
		final WebElement updButton = webDriver.findElement(By.id("upd-btn"));
		updButton.click();

		// 画面スクロール
		scrollTo("100");

		// コース詳細画面の表示確認
		assertEquals("パスワード変更", webDriver.findElement(By.tagName("h2")).getText());

		// エラーメッセージの表示確認
		assertTrue(webDriver.findElement(By.xpath("//input[@name='password']/following-sibling::ul//span"))
				.getText().contains("パスワードと確認パスワードが一致しません。"));

		// 画面遷移後のエビデンスの取得
		getEvidence(new Object() {
		}, "after");
	}

}
