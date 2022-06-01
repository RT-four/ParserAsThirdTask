package com.example.ParserAsThirdTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;


public class Parser {
    static Elements pull;

    public static void start() throws IOException {
//        String url = "https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&offer_type=offices&office_type%5B0%5D=6&region=4588&sort=creation_date_desc";
//        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36";
//        Document document = Jsoup.connect(url).userAgent(userAgent).get();
//        System.out.println(document);
        firstParse();

    }


    public static Document getDocument(String url) {
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36";
        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent(userAgent).get();
        } catch (IOException ioe) {
            System.out.println("Unable to connect to the URL");
            ioe.printStackTrace();
        }
        return document;
    }

    public static void parseArticles() {
        Document firstPage = getDocument("https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&offer_type=offices&office_type%5B0%5D=6&p=1&region=-2");
        Elements articles = firstPage.getElementsByTag("article");
        int i = 1;
        while (true) {
            i++;
            Document newPage = getDocument("https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&offer_type=offices&office_type%5B0%5D=6&p=" + Integer.toString(i) + "&region=-2");
            Elements newArticles = newPage.getElementsByTag("article");
            if (articles.contains(newArticles.get(0))) {
                break;
            }
            articles.addAll(newArticles);
        }
    }

    public static Elements firstParse() {
        String url = "https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&offer_type=offices&office_type%5B0%5D=6&region=4588&sort=creation_date_desc";
        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get(url);
        while (webDriver.getTitle().equals("Captcha - база объявлений ЦИАН")) {
            System.out.println("error");
        }
        webDriver.get(url);
        Document doc = Jsoup.parse(webDriver.getPageSource());
        Elements articles = doc.getElementsByTag("article");
        int numberOfPages = getNumberOfPages(doc);
        System.out.println(numberOfPages);
        for (int i = 1; i <= numberOfPages; i++) {
            Document newPage = getDocument("https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&offer_type=offices&office_type%5B0%5D=6&p=" + Integer.toString(i) + "&region=4588&sort=creation_date_desc");
            Elements newArticles = newPage.getElementsByTag("article");
            articles.addAll(newArticles);
            System.out.println(articles.size());
        }
        return articles;
    }

    public static int getNumberOfPages(Document doc) {
        System.out.println(doc);
        Element counterOfArticlesElement = doc.getElementsByAttributeValue("data-name", "SummaryHeader").get(0);
        Integer countOfArticles = Integer.valueOf(counterOfArticlesElement.child(0).text().replaceAll("[^0-9]", ""));
        Elements articles = doc.getElementsByTag("article");
        Integer artOnThePage = articles.size();
        Integer numberOfPages = (int) (Math.ceil(Double.valueOf(countOfArticles) / Double.valueOf(artOnThePage)));
        return numberOfPages;
    }

}
