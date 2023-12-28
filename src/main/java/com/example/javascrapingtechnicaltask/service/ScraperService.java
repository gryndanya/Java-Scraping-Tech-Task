package com.example.javascrapingtechnicaltask.service;

import com.example.javascrapingtechnicaltask.model.JobItem;
import com.example.javascrapingtechnicaltask.util.CsvExporter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
//import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScraperService {

    //Для отработки каждую ночь
    //@Scheduled(cron = "0 0 0 * * *")
    public List<JobItem> scrapeJobs(String jobFunction){

        System.setProperty("webdriver.chrome.driver", "your/path/to/chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        List<JobItem> jobs = new ArrayList<>();

        try {
            driver.get("https://jobs.techstars.com/jobs");
            Thread.sleep(2500);

            WebElement jobFunctionButton = driver.findElements(By.cssSelector(".cktMLM")).get(0);
            scrollToElement(driver,jobFunctionButton);
            jobFunctionButton.click();
            Thread.sleep(2500);


            WebElement jobFunctionButtonOption = findButtonByText(driver, jobFunction);
            scrollToElement(driver,jobFunctionButtonOption);
            jobFunctionButtonOption.click();
            Thread.sleep(4000);

            Document doc = Jsoup.parse(driver.getPageSource());
            Elements jobBlocks = doc.select(".job-info");

            for(Element jobBlock : jobBlocks){
                String jobUrl = jobBlock.select(".eMSmau").attr("href");
                if(jobUrl.startsWith("/companies/")){

                    Document jobPage = Jsoup.connect("https://jobs.techstars.com/" + jobUrl).get();
                    Thread.sleep(500);

                    String jobPageUrl = Optional.of(jobPage.location())
                            .orElse("NOT_FOUND"); //1
                    System.out.println(jobPageUrl);
                    String positionName = Optional.of(jobPage.select(".jqWDOR").text())
                            .orElse("NOT_FOUND"); //2
                    System.out.println(positionName);
                    String organizationUrl = Optional.of(jobPage.select("[data-testid='button']").attr("href"))
                            .orElse("NOT_FOUND");  //3
                    System.out.println(organizationUrl);
                    String linkToLogo = Optional.of(jobPage.select(".eTCoCQ").attr("src"))
                            .orElse("NOT_FOUND"); //4
                    System.out.println(linkToLogo);
                    String organizationTitle = Optional.of(jobPage.select("[data-testid='content'] .cxrIfE p").text())
                            .orElse("NOT_FOUND"); //5
                    System.out.println(organizationTitle);
                    String laborFunction = Optional.of(jobPage.select(".dmdAKU .bpXRKw").get(0).text())
                            .orElse("NOT_FOUND"); //6
                    System.out.println(laborFunction);
                    String location = Optional.of(jobPage.select(".dmdAKU .bpXRKw").get(1).text())
                            .orElse("NOT_FOUND"); //7
                    System.out.println(location);
                    String postedDate = Optional.of(jobPage.select(".dmdAKU .gRXpLa").get(0).text())
                            .orElse("NOT_FOUND"); //8
                    System.out.println(postedDate);
                    String description = Optional.of(jobPage.select("[data-testid='careerPage']").html())
                            .orElse("NOT_FOUND"); //9
                    System.out.println(description);
                    String tagsNames = Optional.of(jobBlock.select(".OHsAR").stream()
                            .map(Element::text)
                            .collect(Collectors.joining(","))).orElse("NOT_FOUND"); //10
                    System.out.println(tagsNames);

                    jobs.add(JobItem.builder()
                            .jobPageUrl(jobPageUrl)
                            .positionName(positionName)
                            .organizationUrl(organizationUrl)
                            .linkToLogo(linkToLogo)
                            .organizationTitle(organizationTitle)
                            .laborFunction(laborFunction)
                            .location(location)
                            .postedDate(postedDate)
                            .description(description)
                            .tagsNames(tagsNames)
                            .build()
                    );
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }

        CsvExporter.exportToCsv(jobs, jobFunction);

        return jobs;
    }

    private WebElement findButtonByText(WebDriver driver, String buttonText) {
        try {
            return driver.findElement(By.xpath("//button[text()='" + buttonText + "']"));
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
