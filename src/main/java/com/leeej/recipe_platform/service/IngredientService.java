package com.leeej.recipe_platform.service;

import com.leeej.recipe_platform.dto.IngredientDto;
import com.leeej.recipe_platform.dto.IngredientResponseDto;
import com.leeej.recipe_platform.model.Ingredient;
import com.leeej.recipe_platform.repository.IngredientRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.SpringProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public Page<IngredientResponseDto> list(Pageable pageable) {
        return ingredientRepository.findAll(pageable)
                .map(ingredient -> new IngredientResponseDto(
                        ingredient.getId(),
                        ingredient.getName()
                ));
    }

    public IngredientResponseDto get(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("재료 없음"));

        return new IngredientResponseDto(
                ingredient.getId(),
                ingredient.getName()
        );
    }

    public IngredientResponseDto create(IngredientDto dto) {
        if(ingredientRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalStateException("[Ingredient]재료 중복");
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        Ingredient saved = ingredientRepository.save(ingredient);

        return new IngredientResponseDto(saved.getId(), saved.getName());
    }

    public IngredientResponseDto update(Long id, IngredientDto dto) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("재료 존재하지 않음"));

        ingredient.setName(dto.getName());
        Ingredient saved = ingredientRepository.save(ingredient);

        return new IngredientResponseDto(saved.getId(), saved.getName());
    }


    public void delete(Long id) {
        ingredientRepository.deleteById(id);
    }

}
