package com.leeej.recipe_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientDto {
    private Long ingredientId;
    private String ingredientName;
    private String description;
}
