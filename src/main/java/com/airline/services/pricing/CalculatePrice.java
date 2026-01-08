package com.airline.services.pricing;

import com.airline.models.Seat;

public class CalculatePrice {

    public double getFinalPrice(double basePrice, Seat.SeatClass seatClass) {
        PricingStrategy strategy;
        if (seatClass == Seat.SeatClass.BUSINESS) {
            strategy = new BusinessPricing();
        } else {
            strategy = new EconomyPricing();
        }
        return strategy.calculate(basePrice);
    }

    public double getAllowance(Seat.SeatClass seatClass) {
        if (seatClass == Seat.SeatClass.BUSINESS) {
            return 30.0; // Business: 30kg
        }
        return 15.0; // Economy: 15kg
    }

    public double calculateBaggageSurcharge(double weight, Seat.SeatClass seatClass) {
        double allowance = getAllowance(seatClass);
        if (weight > allowance) {
            double extra = weight - allowance;
            return extra * 50.0; // Her fazla kg için 50 TL
        }
        return 0;
    }
}