package com.gaurav.orders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest class OrderServiceTest {
 @Autowired OrderService service;@Autowired ProductRepository products;@Autowired PurchaseRepository purchases;
 @Test void failedPaymentDoesNotReduceStock(){Product p=products.save(new Product("Test",3));service.order(UUID.randomUUID().toString(),p.id,1,false);assertEquals(3,products.findById(p.id).orElseThrow().stock);}
 @Test void repeatedKeyIsIdempotent(){Product p=products.save(new Product("Test",3));String key=UUID.randomUUID().toString();Purchase a=service.order(key,p.id,1,true);Purchase b=service.order(key,p.id,1,true);assertEquals(a.id,b.id);assertEquals(2,products.findById(p.id).orElseThrow().stock);}
 @Test void concurrentOrdersNeverOversell() throws Exception {Product p=products.save(new Product("Limited",1));ExecutorService pool=Executors.newFixedThreadPool(2);CountDownLatch ready=new CountDownLatch(2),go=new CountDownLatch(1);
  Callable<Boolean> task=()->{ready.countDown();go.await();try{service.order(UUID.randomUUID().toString(),p.id,1,true);return true;}catch(Exception ex){return false;}};
  Future<Boolean> a=pool.submit(task),b=pool.submit(task);ready.await();go.countDown();int successful=(a.get()?1:0)+(b.get()?1:0);pool.shutdown();assertEquals(1,successful);assertEquals(0,products.findById(p.id).orElseThrow().stock);
 }
}
