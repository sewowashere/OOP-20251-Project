package com.airline.services.pricing;

import com.airline.models.Seat;
import com.airline.services.implementations.EconomyPricing;

public class CalculatePrice {
    
    /**
     * İş mantığını içeren ana metod. 
     * JUnit ile test edeceğimiz yer tam olarak burasıdır.
     */
    public double getFinalPrice(double basePrice, Seat.SeatClass seatClass) {
        PricingStrategy strategy;

        if (seatClass == Seat.SeatClass.BUSINESS) {
            strategy = new BusinessPricing();
        } else {
            strategy = new EconomyPricing();
        }

        return strategy.calculate(basePrice);
    }

    /**
     * Opsiyonel: Bagaj ağırlığına göre ek ücret ekleme mantığı
     */
    public double calculateBaggageSurcharge(double weight, double allowance) {
        if (weight > allowance) {
            double extra = weight - allowance;
            return extra * 20.0; // Her extra kg için 20 birim ücret
        }
        return 0;
    }
}