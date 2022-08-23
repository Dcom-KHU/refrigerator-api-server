package dcom.refrigerator.api.domain.recipe.service;

import antlr.StringUtils;
import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.FoodCategory;
import dcom.refrigerator.api.domain.food.repositroy.FoodRepository;
import dcom.refrigerator.api.domain.food.service.FoodService;
import dcom.refrigerator.api.domain.foodImage.FoodImage;
import dcom.refrigerator.api.domain.foodImage.repository.FoodImageRepository;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.ingredient.repository.IngredientRepository;
import dcom.refrigerator.api.domain.ingredient.service.IngredientService;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.recipe.dto.RecipeRequestDto;
import dcom.refrigerator.api.domain.recipe.dto.RecipeResponseDto;
import dcom.refrigerator.api.domain.recipe.repository.RecipeRepository;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.dto.UserResponseDto;
import dcom.refrigerator.api.domain.user.repository.UserRepository;
import dcom.refrigerator.api.domain.user.service.UserService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final FoodRepository foodRepository;
    private final FoodService foodService;
    private final UserService userService;
    private final IngredientRepository ingredientRepository;
    private final FoodImageRepository foodImageRepository;

    private final UserRepository userRepository;


    public Integer joinRecipe(RecipeRequestDto.RecipeRegister data) throws Exception {


        Optional<Food> foodOptional = foodRepository.findByName(data.getName());

        //이미 존재하는 음식인 경우
        if (foodOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이름입니다");
        }


        //add food
        foodService.joinFood(Food.builder()
                .name(data.getName())
                .writer(userService.getCurrentUser())
                .description(data.getDescription())
                .category(FoodCategory.valueOf(data.getCategory()))
                .build());

        //food image 처리
        if (!data.getImages().isEmpty()) {


            String[] imageDescriptions = data.getImageDescriptions()
                    .replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", "").split(",");

            if (data.getImages().size()!=imageDescriptions.length)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"각 이미지에는 설명이 반드시 필요합니다.");




                Iterator<String> iter = Arrays.stream(imageDescriptions).iterator();


            for (MultipartFile multipartFile:data.getImages()) {

                log.info(multipartFile.getContentType().toString());

                if (iter.hasNext()) {


                    String imagePath = null;
                    String absolutePath = new File("").getAbsolutePath() + "/";
                    String path = "images";
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    if (!multipartFile.isEmpty()) {
                        String contentType = multipartFile.getContentType();
                        String originalFileExtension;
                        if (ObjectUtils.isEmpty(contentType)) {
                            throw new Exception("이미지 파일은 jpg, png 만 가능합니다.");
                        } else {
                            if (contentType.contains("image/jpeg")) {
                                originalFileExtension = ".jpg";
                            } else if (contentType.contains("image/png")) {
                                originalFileExtension = ".png";
                            } else {
                                throw new Exception("이미지 파일은 jpg, png 만 가능합니다.");
                            }
                        }
                        imagePath = path + "/" + UUID.randomUUID() + originalFileExtension;
                        file = new File(absolutePath + imagePath);
                        multipartFile.transferTo(file);

                        FoodImage foodImage = FoodImage.builder()
                                .food(foodRepository.findByName(data.getName()).get())
                                .originFileName(multipartFile.getOriginalFilename())
                                .filePath(absolutePath + imagePath)
                                .description(iter.next().strip()).build();
                        foodImageRepository.save(foodImage);


                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미지 파일이 비어있습니다. ");
                    }
                }
            }
        }


        //recipe 생성
        Recipe recipe = Recipe.builder()
                .food(foodRepository.findByName(data.getName()).get())
                .amount(data.getIngredientAmount().
                        replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", ""))
                .build();


        Set<Ingredient> ingredients = new HashSet<>();


        for (String ingredient : data.getIngredient().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", "").split(",")) {

            String ingredientName = ingredient.substring(0, ingredient.length()).strip();


                ingredients.add(Ingredient.builder().name(ingredientName).build());
        }


        recipe.setIngredients(ingredients);
        // cascade persist 로 ingredient set 저장
        return recipeRepository.save(recipe).getId();
    }


    public List<RecipeResponseDto.RecipeInfo> getRecipeByUserId(Integer userId){
        User user= userRepository.getReferenceById(userId);


        if (!user.getId().equals(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user 를 찾을 수 없습니다.");
        else{
            List<RecipeResponseDto.RecipeInfo> recipeInfoList= new ArrayList<>();

            List<Recipe> test=recipeRepository.findAllRecipeByUserId(userId);
            for (Recipe recipe : test ) {
                log.info("test");
                log.info("{}",recipe.getIngredients());
            }


            if(!recipeRepository.findAllRecipeByUserId(userId).isEmpty()) {
                for (Recipe recipe : recipeRepository.findAllRecipeByUserId(userId)) {


                    recipeInfoList.add(RecipeResponseDto.RecipeInfo.of(User.builder().id(userId).nickname(user.getNickname()).build(), recipe));

                }

            }
            else throw new ResponseStatusException(HttpStatus.NOT_FOUND,"해당하는 레시피 를 찾을 수 없습니다.");


            return recipeInfoList;
        }
    }








}