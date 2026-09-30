package com.gaurav.orders;
import jakarta.persistence.*;
@Entity public class Product { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id; public String name; public int stock; public Product(){} public Product(String name,int stock){this.name=name;this.stock=stock;} }
