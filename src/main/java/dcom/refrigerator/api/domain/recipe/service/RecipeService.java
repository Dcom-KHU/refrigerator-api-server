package dcom.refrigerator.api.domain.recipe.service;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;


    public Integer joinRecipe(Recipe recipe) throws Exception {
        // cascade persist 로 ingredient set 저장
        return recipeRepository.save(recipe).getId();
    }




}