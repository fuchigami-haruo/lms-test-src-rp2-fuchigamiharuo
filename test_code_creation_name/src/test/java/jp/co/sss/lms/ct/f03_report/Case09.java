package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
		assertEquals("コース詳細", webDriver.findElement(By.className("active")).getText());
		assertEquals("ようこそ受講生ＡＡ２さん",
				webDriver.findElement(By.partialLinkText("受講生ＡＡ２")).getText());

		// 画面遷移後のエビデンスの取得
		getEvidence(new Object() {
		}, "after");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {

		// 「ようこそ受講生ＡＡ２さん」リンクをクリック
		final WebElement namelink = webDriver.findElement(By.linkText("ようこそ受講生ＡＡ２さん"));
		namelink.click();

		// ユーザー詳細画面の表示確認
		assertThat(webDriver.findElement(By.tagName("h2")).getText().contains("ユーザー詳細"));

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		// 画面スクロール
		scrollTo("400");

		// 「修正する」ボタンをクリック
		WebElement element = webDriver.findElement(By.xpath("//tr[td[contains(text(),'週報')]]"));

		WebElement button = element.findElement(By.cssSelector("input[value='修正する']"));

		button.click();

		// レポート登録画面の表示確認
		assertThat(webDriver.findElement(By.tagName("h2")).getText().contains("週報【デモ】"));
		assertTrue(webDriver.findElement(By.id("intFieldName_0")).isDisplayed());
		assertTrue(webDriver.findElement(By.id("intFieldValue_0")).isDisplayed());
		assertTrue(webDriver.findElement(By.id("content_0")).isDisplayed());
		assertTrue(webDriver.findElement(By.id("content_1")).isDisplayed());
		assertTrue(webDriver.findElement(By.id("content_2")).isDisplayed());

		// エビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "bottom");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {

		// 学習項目を空白にする
		WebElement content = webDriver.findElement(By.id("intFieldName_0"));
		content.clear();

		// 理解度の値を選択する
		final Select valueSelect = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		valueSelect.selectByValue("2");

		// 目標の達成度の入力
		final WebElement achievement = webDriver.findElement(By.id("content_0"));
		achievement.clear();
		achievement.sendKeys("5");

		// 所感の入力
		final WebElement Impressions = webDriver.findElement(By.id("content_1"));
		Impressions.clear();
		Impressions.sendKeys("頑張った。");

		// 一週間の振り返りの入力
		final WebElement lookBack = webDriver.findElement(By.id("content_2"));
		lookBack.clear();
		lookBack.sendKeys("楽しかった。");

		// 入力値が入力されている状態のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_bottom");

		// 「提出する」ボタンの押下
		final WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		// コース詳細画面の表示確認
		assertThat(webDriver.findElement(By.tagName("h2")).getText().contains("週報【デモ】"));

		// エラー表示の確認
		content = webDriver.findElement(By.id("intFieldName_0"));
		assertTrue(content.getAttribute("class").contains("errorInput"));

		// エラー表示のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_bottom");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {

		// 学習項目の入力
		final WebElement content = webDriver.findElement(By.id("intFieldName_0"));
		content.clear();
		content.sendKeys("テスト");

		// 理解度の値を選択する
		Select valueSelect = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		valueSelect.selectByValue("");

		// 目標の達成度の入力
		final WebElement achievement = webDriver.findElement(By.id("content_0"));
		achievement.clear();
		achievement.sendKeys("5");

		// 所感の入力
		final WebElement Impressions = webDriver.findElement(By.id("content_1"));
		Impressions.clear();
		Impressions.sendKeys("頑張った。");

		// 一週間の振り返りの入力
		final WebElement lookBack = webDriver.findElement(By.id("content_2"));
		lookBack.clear();
		lookBack.sendKeys("楽しかった。");

		// 入力値が入力されている状態のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_bottom");

		// 「提出する」ボタンの押下
		final WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		// コース詳細画面の表示確認
		assertThat(webDriver.findElement(By.tagName("h2")).getText().contains("週報【デモ】"));

		// エラー表示の確認
		WebElement select = webDriver.findElement(By.id("intFieldValue_0"));
		assertTrue(select.getAttribute("class").contains("errorInput"));

		// エラー表示のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_bottom");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {

		// 学習項目の入力
		final WebElement content = webDriver.findElement(By.id("intFieldName_0"));
		content.clear();
		content.sendKeys("テスト");

		// 理解度の値を選択する
		final Select valueSelect = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		valueSelect.selectByValue("2");

		// 目標の達成度の入力
		WebElement achievement = webDriver.findElement(By.id("content_0"));
		achievement.clear();
		achievement.sendKeys("あいうえお");

		// 所感の入力
		final WebElement Impressions = webDriver.findElement(By.id("content_1"));
		Impressions.clear();
		Impressions.sendKeys("頑張った。");

		// 一週間の振り返りの入力
		final WebElement lookBack = webDriver.findElement(By.id("content_2"));
		lookBack.clear();
		lookBack.sendKeys("楽しかった。");

		// 入力値が入力されている状態のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_bottom");

		// 「提出する」ボタンの押下
		final WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		// コース詳細画面の表示確認
		assertThat(webDriver.findElement(By.tagName("h2")).getText().contains("週報【デモ】"));

		// エラー表示の確認
		achievement = webDriver.findElement(By.id("content_0"));
		assertTrue(achievement.getAttribute("class").contains("errorInput"));

		// エラー表示のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_bottom");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {

		// 学習項目の入力
		final WebElement content = webDriver.findElement(By.id("intFieldName_0"));
		content.clear();
		content.sendKeys("テスト");

		// 理解度の値を選択する
		final Select valueSelect = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		valueSelect.selectByValue("2");

		// 目標の達成度の入力
		WebElement achievement = webDriver.findElement(By.id("content_0"));
		achievement.clear();
		achievement.sendKeys("100");

		// 所感の入力
		final WebElement Impressions = webDriver.findElement(By.id("content_1"));
		Impressions.clear();
		Impressions.sendKeys("頑張った。");

		// 一週間の振り返りの入力
		final WebElement lookBack = webDriver.findElement(By.id("content_2"));
		lookBack.clear();
		lookBack.sendKeys("楽しかった。");

		// 入力値が入力されている状態のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_bottom");

		// 「提出する」ボタンの押下
		final WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		// コース詳細画面の表示確認
		assertThat(webDriver.findElement(By.tagName("h2")).getText().contains("週報【デモ】"));

		// エラー表示の確認
		achievement = webDriver.findElement(By.id("content_0"));
		assertTrue(achievement.getAttribute("class").contains("errorInput"));

		// エラー表示のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_bottom");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {

		// 学習項目の入力
		final WebElement content = webDriver.findElement(By.id("intFieldName_0"));
		content.clear();
		content.sendKeys("テスト");

		// 理解度の値を選択する
		final Select valueSelect = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		valueSelect.selectByValue("2");

		// 目標の達成度の入力
		WebElement achievement = webDriver.findElement(By.id("content_0"));
		achievement.clear();

		// 所感の入力
		WebElement Impressions = webDriver.findElement(By.id("content_1"));
		Impressions.clear();

		// 一週間の振り返りの入力
		final WebElement lookBack = webDriver.findElement(By.id("content_2"));
		lookBack.clear();
		lookBack.sendKeys("楽しかった。");

		// 入力値が入力されている状態のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_bottom");

		// 「提出する」ボタンの押下
		final WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		// コース詳細画面の表示確認
		assertThat(webDriver.findElement(By.tagName("h2")).getText().contains("週報【デモ】"));

		// エラー表示の確認
		achievement = webDriver.findElement(By.id("content_0"));
		assertTrue(achievement.getAttribute("class").contains("errorInput"));

		Impressions = webDriver.findElement(By.id("content_1"));
		assertTrue(Impressions.getAttribute("class").contains("errorInput"));

		// エラー表示のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_bottom");
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {

		// 学習項目の入力
		final WebElement content = webDriver.findElement(By.id("intFieldName_0"));
		content.clear();
		content.sendKeys("テスト");

		// 理解度の値を選択する
		final Select valueSelect = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		valueSelect.selectByValue("2");

		// 目標の達成度の入力
		final WebElement achievement = webDriver.findElement(By.id("content_0"));
		achievement.clear();
		achievement.sendKeys("5");

		// 所感の入力
		WebElement Impressions = webDriver.findElement(By.id("content_1"));
		Impressions.clear();
		Impressions.sendKeys("あ".repeat(2001));

		// 一週間の振り返りの入力
		WebElement lookBack = webDriver.findElement(By.id("content_2"));
		lookBack.clear();
		lookBack.sendKeys("ん".repeat(2001));

		// 入力値が入力されている状態のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "before_bottom");

		// 「提出する」ボタンの押下
		final WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		// コース詳細画面の表示確認
		assertThat(webDriver.findElement(By.tagName("h2")).getText().contains("週報【デモ】"));

		// エラー表示の確認
		Impressions = webDriver.findElement(By.id("content_1"));
		assertTrue(Impressions.getAttribute("class").contains("errorInput"));

		lookBack = webDriver.findElement(By.id("content_2"));
		assertTrue(lookBack.getAttribute("class").contains("errorInput"));

		// エラー表示のエビデンスの取得
		scrollTo("0"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_top");

		scrollTo("400"); // 画面スクロール
		getEvidence(new Object() {
		}, "after_bottom");
	}

}
