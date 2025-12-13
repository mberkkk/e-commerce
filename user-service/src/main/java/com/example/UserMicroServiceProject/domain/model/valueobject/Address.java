package com.example.UserMicroServiceProject.domain.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Address Value Object
 *
 * Encapsulates:
 * - Turkish address validation
 * - Address normalization
 * - Postal code validation
 * - City/district validation
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private static final String[] VALID_TURKISH_CITIES = {
            "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya",
            "Artvin", "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur",
            "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne",
            "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane",
            "Hakkari", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir", "Kars", "Kastamonu",
            "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya",
            "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde",
            "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ",
            "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak",
            "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın",
            "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"
    };

    private String title;
    private String fullAddress;
    private String city;
    private String district;
    private String postalCode;

    public Address(String title, String fullAddress, String city, String district, String postalCode) {
        this.title = validateTitle(title);
        this.fullAddress = validateFullAddress(fullAddress);
        this.city = validateCity(city);
        this.district = validateDistrict(district);
        this.postalCode = validatePostalCode(postalCode);
    }

    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Address title cannot be null or empty");
        }

        String normalized = title.trim();
        if (normalized.length() > 50) {
            throw new IllegalArgumentException("Address title is too long (max 50 characters)");
        }

        return normalized;
    }

    private String validateFullAddress(String fullAddress) {
        if (fullAddress == null || fullAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Full address cannot be null or empty");
        }

        String normalized = fullAddress.trim();
        if (normalized.length() < 10) {
            throw new IllegalArgumentException("Full address is too short (min 10 characters)");
        }

        if (normalized.length() > 500) {
            throw new IllegalArgumentException("Full address is too long (max 500 characters)");
        }

        return normalized;
    }

    private String validateCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }

        String normalized = city.trim();
        if (normalized.length() < 2 || normalized.length() > 50) {
            throw new IllegalArgumentException("City name must be between 2-50 characters");
        }

        // Turkish city validation
        if (!isValidTurkishCity(normalized)) {
            throw new IllegalArgumentException("Invalid Turkish city name: " + normalized);
        }

        return normalized;
    }

    private String validateDistrict(String district) {
        if (district == null) {
            return null; // District is optional
        }

        String normalized = district.trim();
        if (normalized.isEmpty()) {
            return null;
        }

        if (normalized.length() > 50) {
            throw new IllegalArgumentException("District name is too long (max 50 characters)");
        }

        return normalized;
    }

    private String validatePostalCode(String postalCode) {
        if (postalCode == null) {
            return null; // Postal code is optional
        }

        String normalized = postalCode.trim().replaceAll("\\s", "");
        if (normalized.isEmpty()) {
            return null;
        }

        // Turkish postal code format: 5 digits
        if (!normalized.matches("^\\d{5}$")) {
            throw new IllegalArgumentException("Invalid Turkish postal code format (must be 5 digits)");
        }

        return normalized;
    }

    private boolean isValidTurkishCity(String city) {
        for (String validCity : VALID_TURKISH_CITIES) {
            if (validCity.equalsIgnoreCase(city)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get short address representation
     */
    public String getShortAddress() {
        return city + (district != null ? ", " + district : "");
    }

    /**
     * Check if postal code is provided
     */
    public boolean hasPostalCode() {
        return postalCode != null && !postalCode.isEmpty();
    }

    /**
     * Get complete address with title
     */
    public String getCompleteAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append(": ");
        sb.append(fullAddress);
        sb.append(", ").append(getShortAddress());

        if (hasPostalCode()) {
            sb.append(" ").append(postalCode);
        }

        return sb.toString();
    }

    /**
     * Check if this is a business address (basic heuristic)
     */
    public boolean isBusinessAddress() {
        String lowerTitle = title.toLowerCase();
        String lowerAddress = fullAddress.toLowerCase();

        String[] businessKeywords = {"şirket", "firma", "ofis", "plaza", "iş", "ticaret", "ltd", "a.ş"};

        for (String keyword : businessKeywords) {
            if (lowerTitle.contains(keyword) || lowerAddress.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return getCompleteAddress();
    }

    public static Address of(String title, String fullAddress, String city, String district, String postalCode) {
        return new Address(title, fullAddress, city, district, postalCode);
    }
}
