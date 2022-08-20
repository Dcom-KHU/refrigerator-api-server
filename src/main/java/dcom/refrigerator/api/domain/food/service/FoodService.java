package dcom.refrigerator.api.domain.food.service;

import dcom.refrigerator.api.domain.food.repositroy.FoodRepository;
import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
}
