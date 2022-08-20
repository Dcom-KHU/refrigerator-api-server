package dcom.refrigerator.api.domain.recipe.service;

import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
}
