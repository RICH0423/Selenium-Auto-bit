/** 
 * Project Name:web-test 
 * File Name:PurchaseWorker.java 
 * Package Name:com.rich.test.multithread 
 * Date:2015年11月10日上午9:51:52 
 * 
*/  
  
package com.rich.test.multithread;  

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/** 
 * ClassName:PurchaseWorker <br/> 
 * Function: Auto Purchase worker. <br/> 
 * Date:     2015年11月10日 上午9:51:52 <br/> 
 * @author   rich 
 * @version   
 * @since    
 * @see       
 */
public class PurchaseWorker implements Runnable {
    
    private static final String LOGIN_SITE = 
            "https://secure.rakuten.com.tw/member/signin?service_id=Top&return_url=http:%2F%2Fwww.rakuten.com.tw%2F";
    
    private String webUri;
    private String disableBtnClass;
    private int iterateMilliSecs;
    private int waitResultSecs;
    private String username;
    private String password;
    
    /**
     * 
     * @param webUri
     * @param disableBtnClass
     * @param iterateMilliSecs
     * @param waitResultSecs
     */
    public PurchaseWorker(String webUri, String disableBtnClass,
            int iterateMilliSecs, int waitResultSecs, String username, String password) {
        super();
        this.webUri = webUri;
        this.disableBtnClass = disableBtnClass;
        this.iterateMilliSecs = iterateMilliSecs;
        this.waitResultSecs = waitResultSecs;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        WebDriver driver = login(username, password);
        
        while (true){
            waitMilliSecs();
            
            driver.get(webUri);
            if ( !driver.findElements(By.xpath(disableBtnClass)).isEmpty() ) {
                continue;
            }

            System.out.println("---- PurchaseButton enabled !! ----");
            driver.findElement(By.id("cart-form")).submit();

            if(waitForSuccess(driver)){
                break;
            }
        }
    }
    
    private void waitMilliSecs(){
        try {
            TimeUnit.MILLISECONDS.sleep(iterateMilliSecs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void waitMilliSecs(int millis){
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sleep until the id contents we want is visible
     * @param driver
     */
    private boolean waitForSuccess(WebDriver driver){
        long end = System.currentTimeMillis() + waitResultSecs;
        while (System.currentTimeMillis() < end) {
            waitMilliSecs(500);
            if (driver.findElements(By.id("contents")).isEmpty()) {
                continue;
            }
            
            WebElement resultsDiv = driver.findElement(By.id("contents"));
            if (resultsDiv.isDisplayed()) {
                System.out.println("======= purchase success =======");
             // Not close browser!!
//                driver.quit();    
                return true;
            }
        }
        return false;
    }
    
    private WebDriver login(String username, String password) {
        WebDriver driver = new FirefoxDriver();
        driver.get(LOGIN_SITE);

        driver.findElement(By.id("form:userId")).sendKeys(username);
        driver.findElement(By.id("form:password")).sendKeys(password);
        driver.findElement(By.id("form:loginBtn")).click();
        
        waitMilliSecs(2000);
        return driver;
    }

}
  