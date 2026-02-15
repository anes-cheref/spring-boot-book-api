package com.library.book_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record BookCreateDTO(
        @NotBlank(message = "Le titre est obligatoire")
        String title,
        @NotBlank(message = "L'auteur est obligatoire")
        String author,
        @Positive(message = "Le prix doit être positif")
        double price) {
}
