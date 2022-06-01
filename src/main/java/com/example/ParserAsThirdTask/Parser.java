package com.example.ParserAsThirdTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;


public class Parser {
    static Elements pull;
    static long startTime;
    static WebDriver webDriver;
    static Elements articlesToShow = new Elements();

    public static void start() throws IOException, InterruptedException {
        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");
        webDriver = new ChromeDriver();
        pull = firstParse();
        startTime = System.currentTimeMillis();
//        sendMail("Cong, we started!");
        while (true) {
            long differense = Math.subtractExact(System.currentTimeMillis(), startTime);
            if (differense > 5000) {
                pull.remove(2);
//                pull.remove(pull.size()-2);
//                pull.remove(pull.size()-3);
//                pull.remove(pull.size()-4);
                findNewArticles();
                System.out.println(articlesToShow);
            }
        }

    }

//    public static String getTimeSinceStart() {
//        return "Time since our start: " + Math.subtractExact(System.currentTimeMillis(), startTime);
//    }


    public static Elements firstParse() throws InterruptedException {
        String url = "https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&offer_type=offices&office_type%5B0%5D=6&region=4588&sort=creation_date_desc";
        webDriver.get(url);
        webDriver.manage().window().maximize();
        while (webDriver.getTitle().equals("Captcha - база объявлений ЦИАН")) {
            System.out.println("error");
        }
        webDriver.get(url);
        Document doc = Jsoup.parse(webDriver.getPageSource());
        Elements articles = doc.getElementsByTag("article");
        int numberOfPages = getNumberOfPages(doc);
        for (int i = 1; i < numberOfPages; i++) {
            WebElement paginationButton = webDriver.findElements(By.className("_32bbee5fda--list-itemLink--BU9w6")).get(i - 1);
            doc = Jsoup.parse(webDriver.getPageSource());
            //Creating the JavascriptExecutor interface object by Type casting
            JavascriptExecutor js = (JavascriptExecutor)webDriver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

//            if (doc.getElementsByAttributeValue("alt", "UX Feedback").size() > 0) {
//                WebElement imgToClick = webDriver.findElement(By.className("uxs-1149hc6"));
//                imgToClick.click();
//                Thread.sleep(500);
//                WebElement placeToClick = webDriver.findElement(By.className("close-icon"));
//                placeToClick.click();
//                Thread.sleep(500);
//            }
//            if (doc.getElementsByClass("uxs-3arm_zHETw").size() > 0) {
//                WebElement imgToClick = webDriver.findElement(By.className("uxs-1oddrgm"));
//                imgToClick.click();
//                Thread.sleep(500);
//            }
            paginationButton.click();
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);
            Document newPage = Jsoup.parse(webDriver.getPageSource());
            Elements newArticles = newPage.getElementsByTag("article");
            articles.addAll(newArticles);
        }
        return articles;
    }

    public static int getNumberOfPages(Document doc) {
        Element counterOfArticlesElement = doc.getElementsByAttributeValue("data-name", "SummaryHeader").get(0);
        Integer countOfArticles = Integer.valueOf(counterOfArticlesElement.child(0).text().replaceAll("[^0-9]", ""));
        Elements articles = doc.getElementsByTag("article");
        Integer artOnThePage = articles.size();
        Integer numberOfPages = (int) (Math.ceil(Double.valueOf(countOfArticles) / Double.valueOf(artOnThePage)));
        return numberOfPages;
    }

    public static void findNewArticles() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor)webDriver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        articlesToShow.clear();
        WebElement paginationButton = webDriver.findElements(By.className("_32bbee5fda--list-itemLink--BU9w6")).get(0);
        Document doc = Jsoup.parse(webDriver.getPageSource());
//        if (doc.getElementsByAttributeValue("alt", "UX Feedback").size() > 0) {
//            WebElement imgToClick = webDriver.findElement(By.className("uxs-1149hc6"));
//            imgToClick.click();
//            Thread.sleep(500);
//            doc = Jsoup.parse(webDriver.getPageSource());
//            WebElement placeToClick = webDriver.findElement(By.className("close-icon"));
//            placeToClick.click();
//            Thread.sleep(500);
//        }
//        if (doc.getElementsByClass("uxs-3arm_zHETw").size() > 0) {
//            Thread.sleep(1000);
//            doc = Jsoup.parse(webDriver.getPageSource());
//            WebElement imgToClick = webDriver.findElement(By.className("close-icon"));
//            imgToClick.click();
//            Thread.sleep(1000);
//        }
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        paginationButton.click();
        Thread.sleep(1000);
        doc = Jsoup.parse(webDriver.getPageSource());
        int numberOfPages = getNumberOfPages(doc);
        for (int i = 1; i < numberOfPages-1; i++) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            paginationButton = webDriver.findElements(By.className("_32bbee5fda--list-itemLink--BU9w6")).get(i-1);
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            paginationButton.click();
            Thread.sleep(1000);
            Document newPage = Jsoup.parse(webDriver.getPageSource());
            Elements newArticles = newPage.getElementsByTag("article");
            if (pull.containsAll(newArticles)) {
                break;
            }
            for (Element article : newArticles) {
                if (!pull.contains(article)) {
                    articlesToShow.add(article);
                    pull.add(article);
                }
            }
        }
    }

}
