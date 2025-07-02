package com.example.UserMicroServiceProject.domain.service.supporting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Address Suggestion Value Object
 *
 * Contains suggestions for improving address information
 */
@Getter
public class AddressSuggestion {

    private final List<SuggestionItem> suggestions;

    public AddressSuggestion() {
        this.suggestions = new ArrayList<>();
    }

    public void addSuggestion(AddressSuggestionType type, String message) {
        suggestions.add(new SuggestionItem(type, message));
    }

    public boolean hasSuggestions() {
        return !suggestions.isEmpty();
    }

    public int getSuggestionCount() {
        return suggestions.size();
    }

    public List<SuggestionItem> getSuggestionsByType(AddressSuggestionType type) {
        return suggestions.stream()
                .filter(suggestion -> suggestion.getType() == type)
                .toList();
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SuggestionItem {
        private final AddressSuggestionType type;
        private final String message;

        @Override
        public String toString() {
            return type.getDisplayName() + ": " + message;
        }
    }

    @Override
    public String toString() {
        return "AddressSuggestion{suggestions=" + suggestions.size() + "}";
    }
}