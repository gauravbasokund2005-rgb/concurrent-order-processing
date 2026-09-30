package com.gaurav.orders;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface PurchaseRepository extends JpaRepository<Purchase,Long>{ Optional<Purchase> findByRequestKey(String key); }
