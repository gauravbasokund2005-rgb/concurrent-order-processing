package com.gaurav.orders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
@Service public class OrderService {
 private static final Logger log=LoggerFactory.getLogger(OrderService.class);
 private final ProductRepository products;private final PurchaseRepository purchases;
 public OrderService(ProductRepository products,PurchaseRepository purchases){this.products=products;this.purchases=purchases;}
 @Transactional public Purchase order(String key,Long productId,int quantity,boolean paymentSuccessful){
  if(key==null||key.isBlank()||productId==null||quantity<=0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid order");
  // Lock the product before checking duplicate keys to serialize orders for this product.
  Product product=products.lockById(productId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));
  var existing=purchases.findByRequestKey(key);if(existing.isPresent()){
    Purchase prev=existing.get();if(!prev.productId.equals(productId)||prev.quantity!=quantity)throw new ResponseStatusException(HttpStatus.CONFLICT,"Key already used for different order");
    return prev;
  }
  if(!paymentSuccessful){log.info("Simulated payment failed for key {}",key);return purchases.save(new Purchase(key,productId,quantity,"PAYMENT_FAILED"));}
  if(product.stock<quantity)throw new ResponseStatusException(HttpStatus.CONFLICT,"Insufficient stock");
  product.stock-=quantity;products.save(product);
  Purchase purchase=purchases.save(new Purchase(key,productId,quantity,"CONFIRMED"));log.info("Order confirmed {}",key);return purchase;
 }
}
