import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SkelbiuTest {
    public static ArrayList<String> urls = new ArrayList<>();

    public static WebDriver driver;

    @Test
    public void baigesFantazija() {
        pagination();
        openUrls();

    }
    public void openUrls(){
        double sum = 0;
        int countIncompatibles = 0;
        for (String url: urls) {
            driver.get(url);
            try {
                double price = Double.parseDouble(driver.findElement(By.className("price")).getText().split("")[0]);
                System.out.println(price);
                sum += price;
            } catch (Exception e) {
                countIncompatibles ++;

            }
        }

        System.out.println(sum / (urls.size()+ countIncompatibles));
    }
    public void pagination() {   // pagination reiskia puslapiu numeracija
        for (int i = 1; i < 3; i++) {     // sukuriam cikla. nurodytas skaicius (3) rodo kiek kartu prasuks cikla
            String url = "https://www.skelbiu.lt/skelbimai/" + i + "?keywords=vitaminai";   // ateinam i si puslapi
            driver.get(url);
            if (!url.equals(driver.getCurrentUrl())) {  //  atejes i puslapi issiaiskinu ar tai yra puslapis kuriame noriu dirbti ar mane grazino i pirma puslapi. ! zenklas reiskia, kad nera lygu
                return;  // uzbaigia sia funkcija jeigu pasiekiame puslapiu skaiciu kurio nera
            }
            //System.out.println(driver.getCurrentUrl());  // sautas atspausdina visus puslapius kuriuose yra skelbimai
            getUrl();
        }

    }
    public void getUrl() {
        List<WebElement> cards = Stream.concat(   // concat reiskia sujungti zemiau esancius driver...
                driver.findElement(By.id("itemsList")).findElements(By.className("simpleAds")).stream(),  // paduodam uzklausa is viso skelbimo listo(itemList) istraukti tik simpleAdds) ir paverciam stream
                driver.findElement(By.id("itemsList")).findElements(By.className("boldAds")).stream()
        ).toList();

        for (WebElement card: cards) {   // is kiekv korteles istraukia urla
            String url = card.findElement(By.tagName("a")).getAttribute("href");
            urls.add(url);

        }
    }

    @BeforeClass
    public void beforeClass() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        acceptCookies();
    }

    public void acceptCookies() {
        driver.get("https://www.skelbiu.lt/");
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
    }

    @AfterClass
    public void afterClass() {

    }
////

}
