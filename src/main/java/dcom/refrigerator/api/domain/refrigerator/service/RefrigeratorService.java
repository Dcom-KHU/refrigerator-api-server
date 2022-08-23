package dcom.refrigerator.api.domain.refrigerator.service;

import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class RefrigeratorService {
    private final RecipeRepository recipeRepository;
}
