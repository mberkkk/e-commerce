package com.example.UserMicroServiceProject.domain.service.supporting;

import lombok.Builder;
import lombok.Getter;

/**
 * Shipping Preferences Value Object
 *
 * Calculated shipping preferences based on user addresses
 */
@Getter
@Builder
public class ShippingPreferences {

    private final boolean hasBusinessDelivery;
    private final String primaryDeliveryCity;
    private final boolean multiCityDelivery;
    private final String preferredDeliveryMethod;
    private final int estimatedDeliveryDays;

    public static ShippingPreferences defaultPreferences() {
        return ShippingPreferences.builder()
                .hasBusinessDelivery(false)
                .primaryDeliveryCity("Istanbul")
                .multiCityDelivery(false)
                .preferredDeliveryMethod("STANDARD")
                .estimatedDeliveryDays(3)
                .build();
    }

    public boolean supportsExpressDelivery() {
        return "Istanbul".equalsIgnoreCase(primaryDeliveryCity) ||
                "Ankara".equalsIgnoreCase(primaryDeliveryCity) ||
                "Izmir".equalsIgnoreCase(primaryDeliveryCity);
    }

    public boolean supportsSameDayDelivery() {
        return "Istanbul".equalsIgnoreCase(primaryDeliveryCity) &&
                !multiCityDelivery;
    }

    public DeliverySpeed getRecommendedDeliverySpeed() {
        if (supportsSameDayDelivery()) {
            return DeliverySpeed.SAME_DAY;
        } else if (supportsExpressDelivery()) {
            return DeliverySpeed.EXPRESS;
        } else {
            return DeliverySpeed.STANDARD;
        }
    }

    public enum DeliverySpeed {
        SAME_DAY("Same Day", 0, 1.5),
        EXPRESS("Express", 1, 1.25),
        STANDARD("Standard", 2, 1.0),
        ECONOMY("Economy", 5, 0.8);

        private final String displayName;
        private final int days;
        private final double costMultiplier;

        DeliverySpeed(String displayName, int days, double costMultiplier) {
            this.displayName = displayName;
            this.days = days;
            this.costMultiplier = costMultiplier;
        }

        public String getDisplayName() { return displayName; }
        public int getDays() { return days; }
        public double getCostMultiplier() { return costMultiplier; }
    }

    @Override
    public String toString() {
        return "ShippingPreferences{" +
                "primaryCity='" + primaryDeliveryCity + '\'' +
                ", businessDelivery=" + hasBusinessDelivery +
                ", multiCity=" + multiCityDelivery +
                ", estimatedDays=" + estimatedDeliveryDays +
                '}';
    }
}