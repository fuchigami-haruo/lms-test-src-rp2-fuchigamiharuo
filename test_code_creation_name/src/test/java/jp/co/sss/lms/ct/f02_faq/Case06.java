package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;

import jp.co.sss.lms.service.FaqService;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

	@Autowired
	private FaqService faqService;

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
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		// 「機能」ドロップダウンメニューを開く
		WebElement dropdown = webDriver.findElement(By.className("dropdown-toggle"));
		dropdown.click();

		// 「ヘルプ」リンクをクリック
		final WebElement link = webDriver.findElement(By.linkText("ヘルプ"));
		link.click();

		// ヘルプ画面の表示確認
		assertEquals("ヘルプ", webDriver.findElement(By.tagName("h2")).getText());

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// 「よくある質問」リンクをクリック
		WebElement link = webDriver.findElement(By.linkText("よくある質問"));
		link.click();

		// 新しく開いたよくある質問画面タブに切り替え
		for (String window : webDriver.getWindowHandles()) {
			webDriver.switchTo().window(window);
		}

		// よくある質問画面の表示確認
		assertEquals("よくある質問", webDriver.findElement(By.tagName("h2")).getText());

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {

		// 「【研修関係】」リンクの押下
		final WebElement link = webDriver.findElement(By.linkText("【研修関係】"));
		link.click();

		// 「キャンセル」を含む質問の一覧表示の確認
		final List<WebElement> elements = webDriver.findElements(By.cssSelector("dt span:nth-of-type(2)"));

		assertEquals("キャンセル料・途中退校について", elements.get(0).getText());
		assertEquals("研修の申し込みはどのようにすれば良いですか？", elements.get(1).getText());

		// 質問一覧のエビデンスの取得
		scrollTo("500");
		getEvidence(new Object() {
		}, "faqList");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		// 「Q.キャンセル料・途中退校について」テキストの押下
		final WebElement link = webDriver.findElement(By.className("mb10"));
		link.click();

		// 回答「A.～」の表示の確認
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fs18")));

		assertTrue(webDriver.findElement(By.className("fs18")).isDisplayed());

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

}
