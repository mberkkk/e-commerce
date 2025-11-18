package com.example.UserMicroServiceProject.domain.service;

import com.example.UserMicroServiceProject.domain.model.user.User;
import com.example.UserMicroServiceProject.domain.model.user.UserAddress;
import com.example.UserMicroServiceProject.domain.model.valueobject.Address;
import com.example.UserMicroServiceProject.domain.service.supporting.AddressSuggestion;
import com.example.UserMicroServiceProject.domain.service.supporting.AddressSuggestionType;
import com.example.UserMicroServiceProject.domain.service.supporting.AddressValidationResult;
import com.example.UserMicroServiceProject.domain.service.supporting.ShippingPreferences;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AddressDomainService
 *
 * Handles address-related business logic:
 * - Address validation and normalization
 * - Address similarity detection
 * - Geolocation and distance calculations
 */
@Service
public class AddressDomainService {

    /**
     * Validate if user can add new address
     *
     * Business Rules:
     * - User can have maximum 5 addresses
     * - Address should not be duplicate
     * - Business addresses have special validation
     */
    public AddressValidationResult validateNewAddress(User user, Address newAddress) {
        // Rule 1: Maximum address limit
        if (user.getAddressCount() >= 5) {
            return AddressValidationResult.failure("User cannot have more than 5 addresses");
        }

        // Rule 2: Check for duplicate addresses
        if (isDuplicateAddress(user.getAddresses(), newAddress)) {
            return AddressValidationResult.failure("Similar address already exists");
        }

        // Rule 3: Business address validation
        if (newAddress.isBusinessAddress()) {
            AddressValidationResult businessValidation = validateBusinessAddress(newAddress);
            if (!businessValidation.isValid()) {
                return businessValidation;
            }
        }

        // Rule 4: Geographic validation
        if (!isValidGeographicLocation(newAddress)) {
            return AddressValidationResult.failure("Invalid geographic location");
        }

        return AddressValidationResult.success();
    }

    /**
     * Suggest address improvements based on common patterns
     */
    public AddressSuggestion suggestAddressImprovements(Address address) {
        AddressSuggestion suggestion = new AddressSuggestion();

        // Suggest postal code if missing
        if (!address.hasPostalCode()) {
            String suggestedPostalCode = suggestPostalCodeForCity(address.getCity());
            if (suggestedPostalCode != null) {
                suggestion.addSuggestion(
                        AddressSuggestionType.POSTAL_CODE,
                        "Consider adding postal code: " + suggestedPostalCode
                );
            }
        }

        // Suggest district if missing
        if (address.getDistrict() == null || address.getDistrict().isEmpty()) {
            List<String> commonDistricts = getCommonDistrictsForCity(address.getCity());
            if (!commonDistricts.isEmpty()) {
                suggestion.addSuggestion(
                        AddressSuggestionType.DISTRICT,
                        "Common districts in " + address.getCity() + ": " + String.join(", ", commonDistricts)
                );
            }
        }

        // Suggest title improvements
        if (address.getTitle().toLowerCase().equals("address")) {
            suggestion.addSuggestion(
                    AddressSuggestionType.TITLE,
                    "Consider using a more descriptive title like 'Home', 'Work', or 'Office'"
            );
        }

        return suggestion;
    }

    /**
     * Calculate shipping preferences based on user addresses
     */
    public ShippingPreferences calculateShippingPreferences(User user) {
        List<Address> addresses = user.getAddresses().stream()
                .map(userAddress -> userAddress.getAddress())
                .collect(Collectors.toList());

        if (addresses.isEmpty()) {
            return ShippingPreferences.defaultPreferences();
        }

        // Analyze address patterns
        boolean hasBusinessAddresses = addresses.stream()
                .anyMatch(Address::isBusinessAddress);

        List<String> cities = addresses.stream()
                .map(Address::getCity)
                .distinct()
                .collect(Collectors.toList());

        String primaryCity = findMostCommonCity(addresses);

        return ShippingPreferences.builder()
                .hasBusinessDelivery(hasBusinessAddresses)
                .primaryDeliveryCity(primaryCity)
                .multiCityDelivery(cities.size() > 1)
                .preferredDeliveryMethod(determinePreferredDeliveryMethod(addresses))
                .estimatedDeliveryDays(calculateEstimatedDeliveryDays(primaryCity))
                .build();
    }

    /**
     * Find addresses within delivery range of a location
     */
    public List<Address> findAddressesInDeliveryRange(
            User user,
            String centerCity,
            int maxDistanceKm) {

        return user.getAddresses().stream()
                .map(userAddress -> userAddress.getAddress())
                .filter(address -> isWithinDeliveryRange(address, centerCity, maxDistanceKm))
                .collect(Collectors.toList());
    }

    private boolean isDuplicateAddress(List<UserAddress> existingAddresses, Address newAddress) {
        return existingAddresses.stream()
                .anyMatch(existing -> isSimilarAddress(existing.getAddress(), newAddress));
    }

    private boolean isSimilarAddress(Address existing, Address newAddress) {
        // Simple similarity check
        String existingNormalized = normalizeAddressForComparison(existing);
        String newNormalized = normalizeAddressForComparison(newAddress);

        // Check if addresses are 80% similar
        double similarity = calculateStringSimilarity(existingNormalized, newNormalized);
        return similarity > 0.8;
    }

    //ToDo: Add comment about what method does
    private String normalizeAddressForComparison(Address address) {
        return (address.getFullAddress() + " " + address.getCity() + " " + address.getDistrict())
                .toLowerCase()
                .replaceAll("[^a-zçğıöşü0-9\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private double calculateStringSimilarity(String s1, String s2) {
        // Simplified Levenshtein distance-based similarity
        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) return 1.0;

        int editDistance = calculateEditDistance(s1, s2);
        return 1.0 - (double) editDistance / maxLength;
    }

    // ToDo: Edit the method, add comment and make readible
    private int calculateEditDistance(String s1, String s2) {
        // Simplified edit distance calculation
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i-1) == s2.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i-1][j], dp[i][j-1]), dp[i-1][j-1]);
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    private AddressValidationResult validateBusinessAddress(Address address) {
        // Business addresses should have more detailed information
        if (address.getFullAddress().length() < 20) {
            return AddressValidationResult.failure("Business addresses should have detailed information");
        }

        return AddressValidationResult.success();
    }

    private boolean isValidGeographicLocation(Address address) {
        // Simplified geographic validation
        // In real implementation, integrate with mapping service
        return address.getCity() != null && !address.getCity().isEmpty();
    }

    private String suggestPostalCodeForCity(String city) {
        // Simplified postal code suggestion
        // In real implementation, use postal code database
        switch (city.toLowerCase()) {
            case "istanbul": return "34000";
            case "ankara": return "06000";
            case "izmir": return "35000";
            default: return null;
        }
    }

    private List<String> getCommonDistrictsForCity(String city) {
        // Simplified district suggestions
        switch (city.toLowerCase()) {
            case "istanbul": return List.of("Kadıköy", "Beşiktaş", "Şişli", "Beyoğlu");
            case "ankara": return List.of("Çankaya", "Keçiören", "Yenimahalle", "Mamak");
            case "izmir": return List.of("Konak", "Karşıyaka", "Bornova", "Bayraklı");
            default: return List.of();
        }
    }

    private String findMostCommonCity(List<Address> addresses) {
        return addresses.stream()
                .collect(Collectors.groupingBy(Address::getCity, Collectors.counting()))
                .entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse("Istanbul"); // Default
    }

    private String determinePreferredDeliveryMethod(List<Address> addresses) {
        boolean hasBusinessAddresses = addresses.stream().anyMatch(Address::isBusinessAddress);
        return hasBusinessAddresses ? "BUSINESS_HOURS" : "FLEXIBLE";
    }

    private int calculateEstimatedDeliveryDays(String city) {
        // Simplified delivery estimation
        switch (city.toLowerCase()) {
            case "istanbul":
            case "ankara":
            case "izmir": return 1; // Same day or next day
            default: return 2; // 2-3 days for other cities
        }
    }

    private boolean isWithinDeliveryRange(Address address, String centerCity, int maxDistanceKm) {
        // Simplified distance calculation
        // In real implementation, use geolocation services
        return address.getCity().equalsIgnoreCase(centerCity) || maxDistanceKm > 100;
    }
}