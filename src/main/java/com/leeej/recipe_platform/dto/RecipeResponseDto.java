package com.leeej.recipe_platform.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponseDto {
    private Long id;
    private String title;
    private String description;
}
