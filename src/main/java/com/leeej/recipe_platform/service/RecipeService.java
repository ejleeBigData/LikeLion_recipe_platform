package com.leeej.recipe_platform.service;

import com.leeej.recipe_platform.dto.AddIngredientDto;
import com.leeej.recipe_platform.dto.RecipeDto;
import com.leeej.recipe_platform.dto.RecipeResponseDto;
import com.leeej.recipe_platform.model.Ingredient;
import com.leeej.recipe_platform.model.Recipe;
import com.leeej.recipe_platform.model.RecipeIngredient;
import com.leeej.recipe_platform.model.RecipeIngredientId;
import com.leeej.recipe_platform.repository.IngredientRepository;
import com.leeej.recipe_platform.repository.RecipeIngredientRepository;
import com.leeej.recipe_platform.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public Page<RecipeResponseDto> list(Pageable pageable) {
        return recipeRepository.findAll(pageable)
                .map(recipe -> new RecipeResponseDto(recipe.getId(), recipe.getTitle(), recipe.getDescription()));
    }

    public RecipeResponseDto create(RecipeDto dto) {
        Recipe recipe = new Recipe();
        recipe.setTitle(dto.getTitle());
        recipe.setDescription(dto.getDescription());

        Recipe saved = recipeRepository.save(recipe);

        return new RecipeResponseDto(saved.getId(), saved.getTitle(), saved.getDescription());
    }

    public void addIngredient(Long recipeId, AddIngredientDto dto) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NoSuchElementException("레시피 없음"));

        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new NoSuchElementException("재료 없음"));

        RecipeIngredientId id = new RecipeIngredientId(recipeId, ingredient.getId());
        if(recipeIngredientRepository.existsById(id)) {
            throw new IllegalStateException("[Recipe]재료 중복");
        }

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId(id);
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(dto.getQuantity());

        recipe.getRecipeIngredients().add(recipeIngredient);
        recipeRepository.save(recipe);
    }
}
