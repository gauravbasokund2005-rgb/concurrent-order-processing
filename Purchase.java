package com.gaurav.orders;
import jakarta.persistence.*;
@Entity @Table(name="purchases",uniqueConstraints=@UniqueConstraint(columnNames="requestKey")) public class Purchase { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; @Column(nullable=false,unique=true) public String requestKey; public Long productId; public int quantity; public String status; public Purchase(){} public Purchase(String key,Long pid,int qty,String status){requestKey=key;productId=pid;quantity=qty;this.status=status;} }
