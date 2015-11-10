/** 
 * Project Name:web-test 
 * File Name:PurchaseManager.java 
 * Package Name:com.rich.test.multithread 
 * Date:2015年11月10日上午10:05:03 
 * 
*/  
  
package com.rich.test.multithread;  

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** 
 * ClassName:PurchaseManager <br/> 
 * Function: Auto Purchase Manager. <br/> 
 * Date:     2015年11月10日 上午10:05:03 <br/> 
 * @author   rich 
 * @version   
 * @since    
 * @see       
 */
public class PurchaseManager {

    private static final int WORKER_SIZE = 2;
    
    private static final String WEB_SITE = 
            "http://www.rakuten.com.tw/shop/wayfone/product/1510ss_1/?s-id=Event-marathon-151109-index-111012-101";
    
    private static final String DISABLE_BUTTON_CLASS =
            "//*[@class='b-btn js-popover b-btn-deny b-btn-type-a b-btn-emph b-disabled b-btn-large b-btn-cart']";
        
    public static void main(String[] args) {
        
        ExecutorService executorPool = Executors.newFixedThreadPool(WORKER_SIZE); 
        
        for(int i=0; i<WORKER_SIZE; i++){
            executorPool.execute(new PurchaseWorker( WEB_SITE, DISABLE_BUTTON_CLASS,
                    200, 40, "account", "password"));
        }
        
        shutdownAndAwaitTermination(executorPool);
    }
    
    private static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(10, TimeUnit.MINUTES)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(5, TimeUnit.MINUTES))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

}
  