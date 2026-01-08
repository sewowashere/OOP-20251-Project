package com.airline.services.pricing;

// Ekonomi sınıfı için fiyat olduğu gibi kalır veya küçük bir katsayı ile çarpılır
public class EconomyPricing implements PricingStrategy {
    @Override
    public double calculate(double basePrice) {
        return basePrice; // Sabit fiyat
    }
}

