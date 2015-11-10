/** 
 * Project Name:web-test 
 * File Name:RakutenAutoPurchase.java 
 * Package Name:com.rich.test.web 
 * Date:2015年11月9日下午4:55:38 
 * 
*/  
  
package com.rich.test.web;  

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/** 
 * ClassName:RakutenAutoPurchase <br/> 
 * Function: rakuten web auto purchase. <br/> 
 * Date:     2015年11月9日 下午4:55:38 <br/> 
 * @author   rich 
 * @version   
 * @since    
 * @see       
 */
public class RakutenAutoPurchase {
    
    private static final String WEB_SITE = 
            "http://www.rakuten.com.tw/shop/wayfone/product/1510ss_1/?s-id=Event-marathon-151109-index-111012-101";
    private static final String WEB_SITE_OK = 
            "http://www.rakuten.com.tw/shop/double-s/product/1109/?s-id=Event-marathon-151109-index-110920-305";
    //http://www.rakuten.com.tw/shop/double-s/product/1109/?s-id=Event-marathon-151109-index-110920-305
    
    private static final String LOGIN_SITE = 
            "https://secure.rakuten.com.tw/member/signin?service_id=Top&return_url=http:%2F%2Fwww.rakuten.com.tw%2F";

    public static void login(WebDriver driver) throws InterruptedException{
        driver.get(LOGIN_SITE);
        
        driver.findElement(By.id("form:userId")).sendKeys("rich04230@gmail.com");
        // Enter something to search for
        driver.findElement(By.id("form:password")).sendKeys("12345678qq");

        driver.findElement(By.id("form:loginBtn")).click();;
        Thread.sleep(2000);
    }
    
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new FirefoxDriver();
        
        login(driver);
        
        while (true){
            Thread.sleep(2000);
            driver.get(WEB_SITE_OK);
            if (driver
                    .findElements(By.xpath(
                            "//*[@class='b-btn js-popover b-btn-deny b-btn-type-a b-btn-emph b-disabled b-btn-large b-btn-cart']")).size() != 0) {
                continue;
            }

            System.out.println("PurchaseButton enabled !!");
            WebElement purchaseForm = driver.findElement(By.id("cart-form"));
            purchaseForm.submit();

            if(waitForSuccess(driver)){
                break;
            }
        }
        
    }
    
    /**
     * Sleep until the div we want is visible or 5 seconds is over
     * @param driver
     * @throws InterruptedException 
     */
    private static boolean waitForSuccess(WebDriver driver) throws InterruptedException{
        long end = System.currentTimeMillis() + 5000;
      while (System.currentTimeMillis() < end) {
          Thread.sleep(1000);
          if (driver.findElements(By.id("contents")).isEmpty()) {
              continue;
          }
          
          WebElement resultsDiv = driver.findElement(By.id("contents"));

          // If results have been returned, the results are displayed in a drop down.
          if (resultsDiv.isDisplayed()) {
              System.out.println("======= purchase success =======");
//              driver.quit();
            return true;
          }
      }
      return true;
    }

}
  