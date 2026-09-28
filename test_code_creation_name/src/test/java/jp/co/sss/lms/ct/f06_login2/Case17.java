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
 * ケース17
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース17 受講生 初回ログイン 正常系")
public class Case17 {

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
		loginId.sendKeys("StudentAA07");

		// パスワードの入力
		final WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("StudentAA07");

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
	@DisplayName("テスト04 変更パスワードを入力し「変更」ボタン押下")
	void test04() {

		// 現在のパスワードの入力
		final WebElement currentPassword = webDriver.findElement(By.name("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA07");

		// 新しいパスワードの入力
		final WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("StudentAA0777");

		// 確認パスワードの入力
		final WebElement passwordConfirm = webDriver.findElement(By.name("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("StudentAA0777");

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

		// コース詳細画面の表示確認
		assertEquals("コース詳細", webDriver.findElement(By.className("active")).getText());
		assertEquals("ようこそ受講生ＡＡ７さん", webDriver.findElement(By.partialLinkText("受講生ＡＡ７")).getText());

		// 画面遷移後のエビデンスの取得
		getEvidence(new Object() {
		}, "after");
	}

}
