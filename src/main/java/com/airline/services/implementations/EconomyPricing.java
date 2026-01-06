package com.airline.services.implementations;

import com.airline.services.pricing.PricingStrategy;

// Ekonomi sınıfı için fiyat olduğu gibi kalır veya küçük bir katsayı ile çarpılır
public class EconomyPricing implements PricingStrategy {
    @Override
    public double calculate(double basePrice) {
        return basePrice; // Sabit fiyat
    }
}

