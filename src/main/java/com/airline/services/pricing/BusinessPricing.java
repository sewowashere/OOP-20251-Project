package com.airline.services.pricing;

//Business sınıfı için fiyat genelde 2.5 veya 3 katıdır
public class BusinessPricing implements PricingStrategy {
 @Override
 public double calculate(double basePrice) {
     return basePrice * 2.5; 
 }
}
