package com.example.ParserAsThirdTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.ArrayList;


public class Parser {
    static ArrayList<Card> pullOfCards = new ArrayList<>();
    static long timeOfLastResearch;
    static WebDriver webDriver;
    static ArrayList<Card> cardsToShow = new ArrayList<>();

    public static void start() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");
        webDriver = new ChromeDriver();
        firstParse();
        System.out.println("Количество обьявлений при старте программы: " + pullOfCards.size());
        timeOfLastResearch = System.currentTimeMillis();
        SendEmail send = new SendEmail("hack1818181@gmail.com", "Оповещения о новых обьявлениях", "Начинаем работу!");
        while (true) {
            long difference = Math.subtractExact(System.currentTimeMillis(), timeOfLastResearch);
            if (difference > 10000) {

                pullOfCards.remove(80);
                pullOfCards.remove(1);
                pullOfCards.remove(100);
                pullOfCards.remove(54);
                pullOfCards.remove(102);
                pullOfCards.remove(53);
                findNewArticles();
                System.out.println("Новых обьявлений: " + cardsToShow.size());
                timeOfLastResearch = System.currentTimeMillis();
            }
        }

    }


    public static ArrayList<Card> getCardsToShow(){
        return cardsToShow;
    }

    public static void firstParse() throws InterruptedException {
        String url = "https://spb.cian.ru/cat.php?deal_type=sale&engine_version=2&offer_type=offices&office_type%5B0%5D=6&region=4588&sort=creation_date_desc";
        webDriver.get(url);
        webDriver.manage().window().maximize();
        while (webDriver.getTitle().equals("Captcha - база объявлений ЦИАН")) {
            System.out.println("error");
        }
        webDriver.get(url);
        Document doc = Jsoup.parse(webDriver.getPageSource());
        Elements articles = doc.getElementsByTag("article");
        pullOfCards.addAll(addArticlesToCards(articles));
        int numberOfPages = getNumberOfPages(doc);
        for (int i = 1; i < numberOfPages; i++) {
            WebElement paginationButton = webDriver.findElements(By.className("_32bbee5fda--list-itemLink--BU9w6")).get(i - 1);
            doc = Jsoup.parse(webDriver.getPageSource());
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try {
                Thread.sleep(500);
                paginationButton.click();
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }


            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Document newPage = Jsoup.parse(webDriver.getPageSource());
            Elements newArticles = newPage.getElementsByTag("article");
            pullOfCards.addAll(addArticlesToCards(newArticles));
        }
    }

    private static ArrayList<Card> addArticlesToCards(Elements articles) {
        ArrayList<Card> cards = new ArrayList<>();
        for (Element art : articles) {
            String name = art.getElementsByAttributeValue("data-mark", "OfferTitle").get(0).child(0).text();
            String price = art.getElementsByAttributeValue("data-mark", "MainPrice").get(0).child(0).text();
            String address = "Не указан";
            if (art.getElementsByAttributeValue("data-name", "SpecialGeo").size() > 0) {
                address = art.getElementsByAttributeValue("data-name", "SpecialGeo").get(0).child(0).child(1).text();
            }
            String linkToPicture = "http://s1.iconbird.com/ico/2013/9/450/w256h2561380453900FileDefault256x25632.png";
            if (art.getElementsByClass("_32bbee5fda--container--o8CED").size() > 0) {
                linkToPicture = art.getElementsByClass("_32bbee5fda--container--o8CED").get(0).attr("src");
            }
            String link = art.getElementsByAttributeValue("data-name", "LinkArea").get(0).child(0).attr("href");
            Card newCard = new Card(name, price, address, link, linkToPicture);
            cards.add(newCard);
        }
        return cards;
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
        Document doc = Jsoup.parse(webDriver.getPageSource());
        int numberOfPages = getNumberOfPages(doc);
        System.out.println("Найдено " + numberOfPages + " страниц");
        System.out.println("Анализ страницы: 1");
        Elements newArticles = doc.getElementsByTag("article");
        ArrayList<Card> newCards = new ArrayList<>();
        newCards.addAll(addArticlesToCards(newArticles));

        cardsToShow.clear();

        WebElement paginationButton = webDriver.findElements(By.className("_32bbee5fda--list-itemLink--BU9w6")).get(0);

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        try {
            Thread.sleep(1000);
            paginationButton.click();
            Thread.sleep(1500);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        for (int i = 1; i < numberOfPages-1; i++) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            if(webDriver.findElements(By.className("_32bbee5fda--list-itemLink--BU9w6")).size()==(i-1)){
                break;
            }
            paginationButton = webDriver.findElements(By.className("_32bbee5fda--list-itemLink--BU9w6")).get(i - 1);
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(500);
            paginationButton.click();
            Thread.sleep(1000);
            Document newPage = Jsoup.parse(webDriver.getPageSource());
            newArticles = newPage.getElementsByTag("article");
            newCards.addAll(addArticlesToCards(newArticles));

            for (Card card : newCards) {
                if (!haveCard(pullOfCards, card)) {
                    cardsToShow.add(card);
                    pullOfCards.add(card);
                }
            }
            System.out.println("Анализ страницы: " + (i+1));
        }
    }

    public static boolean haveCard(ArrayList<Card> pull, Card card) {
        for (Card cardOfPull : pull) {
            if (card.getLink().equals(cardOfPull.getLink())) {
                return true;
            }
        }
        return false;
    }

}
