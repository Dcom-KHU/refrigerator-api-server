package dcom.refrigerator.api.domain.refrigerator.service;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.FoodCategory;
import dcom.refrigerator.api.domain.food.dto.FoodRequestDto;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.ingredient.repository.IngredientRepository;
import dcom.refrigerator.api.domain.ingredient.service.IngredientService;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.recipe.dto.RecipeResponseDto;
import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import dcom.refrigerator.api.domain.refrigerator.Refrigerator;
import dcom.refrigerator.api.domain.refrigerator.dto.RefrigeratorRequestDto;
import dcom.refrigerator.api.domain.refrigerator.dto.RefrigeratorResponseDto;
import dcom.refrigerator.api.domain.refrigerator.repository.RefrigeratorRepository;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.sql.Ref;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class RefrigeratorService {
    private final RefrigeratorRepository refrigeratorRepository;
    private final IngredientRepository ingredientRepository;
    private final UserService userService;

    public List<RefrigeratorResponseDto.WithIngredient> getRefrigerator() {
        User currentUser = userService.getCurrentUser();
        return RefrigeratorResponseDto.WithIngredient.of(refrigeratorRepository.findByUser(currentUser));
    }

    public void addIngredient(RefrigeratorRequestDto.addIngredient ingredientRegister){
        User currentUser = userService.getCurrentUser();
        Optional<Ingredient> ingredient = ingredientRepository.findByName(ingredientRegister.getName());

        if (ingredient.isPresent()) {
            Refrigerator refrigerator = Refrigerator.builder()
                    .user(currentUser)
                    .ingredient(ingredient.get())
                    .expiredDate(ingredientRegister.getExpiredDate())
                    .build();

            refrigeratorRepository.save(refrigerator);
        } else {
            Ingredient newIngredient = ingredientRepository.save(
                    Ingredient.builder()
                            .name(ingredientRegister.getName())
                            .build()
            );

            Refrigerator refrigerator = Refrigerator.builder()
                    .user(currentUser)
                    .ingredient(newIngredient)
                    .expiredDate(ingredientRegister.getExpiredDate())
                    .build();

            refrigeratorRepository.save(refrigerator);
        }
    }

    public void modifyIngredient(RefrigeratorRequestDto.modifyIngredient modifyIngredient){
        User currentUser = userService.getCurrentUser();
        Refrigerator refrigerator = refrigeratorRepository.findById(modifyIngredient.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 재료입니다.")
        );

        if (refrigerator.getUser().getId() == currentUser.getId()){
            refrigerator.setExpiredDate(modifyIngredient.getExpiredDate());

            refrigerator.builder()
                    .user(currentUser)
                    .ingredient(refrigerator.getIngredient())
                    .expiredDate(refrigerator.getExpiredDate())
                    .build();

            refrigeratorRepository.save(refrigerator);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }
    }

    public void deleteIngredient(Integer refrigeratorId){
        User currentUser = userService.getCurrentUser();
        Refrigerator refrigerator = refrigeratorRepository.findById(refrigeratorId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제 된 재료 입니다.")
        );

        if (refrigerator.getUser().getId() == currentUser.getId()){
            refrigeratorRepository.deleteById(refrigeratorId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }
    }

    @Scheduled(cron="0 0 12 * * *")
    public void expiredDateNotification() {
        List<Refrigerator> refrigerator = refrigeratorRepository.findOlds();
    }
}
