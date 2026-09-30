package com.gaurav.orders;
import jakarta.validation.Valid;import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
@RestController @RequestMapping("/api") public class OrderController {
 private final OrderService service;private final ProductRepository products;private final PurchaseRepository purchases;
 public OrderController(OrderService s,ProductRepository p,PurchaseRepository r){service=s;products=p;purchases=r;}
 public record ProductInput(@NotBlank String name,@Min(0) int stock){}
 public record OrderInput(@NotBlank String requestKey,@NotNull Long productId,@Min(1) int quantity,boolean paymentSuccessful){}
 @PostMapping("/products") @ResponseStatus(HttpStatus.CREATED) public Product create(@Valid @RequestBody ProductInput in){return products.save(new Product(in.name(),in.stock()));}
 @GetMapping("/products") public List<Product> all(){return products.findAll();}
 @PostMapping("/orders") public Purchase order(@Valid @RequestBody OrderInput in){return service.order(in.requestKey(),in.productId(),in.quantity(),in.paymentSuccessful());}
 @GetMapping("/orders") public List<Purchase> orders(){return purchases.findAll();}
}
