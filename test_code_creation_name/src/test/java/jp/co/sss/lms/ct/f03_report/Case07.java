package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
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

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		// ログインIDの入力
		final WebElement loginId = webDriver.findElement(By.name("loginId"));
		loginId.clear();
		loginId.sendKeys("StudentAA02");

		// パスワードの入力
		final WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("StudentAA02");

		// 入力値が入力されている状態のエビデンスの取得
		getEvidence(new Object() {
		}, "before");

		// 「ログイン」ボタンの押下
		final WebElement button = webDriver.findElement(By.className("btn-primary"));
		button.click();

		// コース詳細画面の表示確認
		webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		assertEquals("コース詳細", webDriver.findElement(By.className("active")).getText());
		assertEquals("ようこそ受講生ＡＡ２さん",
				webDriver.findElement(By.partialLinkText("受講生ＡＡ２")).getText());

		// 画面遷移後のエビデンスの取得
		getEvidence(new Object() {
		}, "after");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		// 「詳細」ボタンをクリック
		final WebElement button = webDriver.findElement(By.cssSelector("input[value='詳細']"));
		final WebElement status = webDriver.findElement(By.cssSelector(".w10per span"));

		if ("未提出".equals(status.getText())) {
			button.click();
		}

		// セクション詳細画面の表示確認
		webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		assertEquals("セクション詳細", webDriver.findElement(By.className("active")).getText());
		assertTrue(webDriver.findElement(By.cssSelector("input[value='日報【デモ】を提出する']")).isDisplayed());

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「日報を提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		// 「日報を提出する」ボタンをクリック
		final WebElement button = webDriver.findElement(By.cssSelector("input[value='日報【デモ】を提出する']"));
		button.click();

		// レポート登録画面の表示確認
		assertThat(webDriver.findElement(By.tagName("h2")).getText().contains("日報【デモ】"));
		assertTrue(webDriver.findElement(By.className("btn-primary")).isDisplayed());

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {

		// 報告内容記入欄に値の入力
		final WebElement textarea = webDriver.findElement(By.name("contentArray[0]"));
		textarea.clear();
		textarea.sendKeys("テスト用の報告内容。");

		// 入力値が入力されている状態のエビデンスの取得
		getEvidence(new Object() {
		}, "before");

		// 「提出する」ボタンをクリック
		final WebElement button = webDriver.findElement(By.className("btn-primary"));
		button.click();

		// セクション詳細画面の表示確認
		webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		assertEquals("セクション詳細", webDriver.findElement(By.className("active")).getText());
		assertTrue(webDriver.findElement(By.cssSelector("input[value='提出済み日報【デモ】を確認する']")).isDisplayed());

		// 画面遷移後のエビデンスの取得
		getEvidence(new Object() {
		}, "after");
	}

}
