package dcom.refrigerator.api.domain.food.service;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.FoodCategory;
import dcom.refrigerator.api.domain.food.FoodDocument;
import dcom.refrigerator.api.domain.food.dto.FoodRequestDto;
import dcom.refrigerator.api.domain.food.dto.FoodResponseDto;
import dcom.refrigerator.api.domain.food.repositroy.FoodDocumentRepository;
import dcom.refrigerator.api.domain.food.repositroy.FoodRepository;
import dcom.refrigerator.api.domain.foodImage.FoodImage;
import dcom.refrigerator.api.domain.foodImage.repository.FoodImageRepository;
import dcom.refrigerator.api.domain.foodImage.service.FoodImageService;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import dcom.refrigerator.api.domain.ingredient.service.IngredientService;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.recipe.service.RecipeService;
import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class FoodService {
    private final FoodRepository foodRepository;
    private final FoodDocumentRepository foodDocumentRepository;
    private final UserService userService;
    private final FoodImageService foodImageService;
    private final FoodImageRepository foodImageRepository;
    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final ElasticsearchOperations elasticsearchOperations;

    public Integer joinFood(FoodRequestDto.FoodRegister data) throws Exception {

        Food food = Food.builder()
                .name(data.getName())
                .writer(userService.getCurrentUser())
                .description(data.getDescription())
                .category(FoodCategory.valueOf(data.getCategory()))
                .ingredientCount(0)
                .build();

        //비어있는 경우 내부에서 처리
        //food image 처리
        List<MultipartFile> mainImage=new ArrayList<>();
        mainImage.add(data.getMainImage());
        food.setMainImage(foodImageService.registerImages(mainImage, food.getDescription(),food));
        foodImageService.registerImages(data.getImages(), data.getImageDescriptions(), food);
        foodRepository.save(food);

/*
        JSONParser jsonParse = new JSONParser().parse()*/


        String[] ingredientAmounts = data.getIngredientAmount().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", "").split(",");
        Iterator<String> iter = Arrays.stream(ingredientAmounts).iterator();
        String[] ingredients = data.getIngredient().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", "").split(",");


        //재료별 재료 양 필수
        if (ingredients.length != ingredientAmounts.length)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "재료와 재료 양의 개수를 맞춰주세요");

        //recipe 생성
        for (String ingredient : ingredients) {
            if (iter.hasNext()) {
                String ingredientName = ingredient.substring(0, ingredient.length()).strip();
                ingredientService.joinIngredient(Ingredient.builder().name(ingredientName).build());
                //recipe save
                Recipe recipe = Recipe.builder()
                        .food(food)
                        .amount(iter.next().strip())
                        .build();
                recipe.setIngredient(ingredientService.getIngredientByName(ingredientName));
                recipeService.joinRecipe(recipe);
            }
        }

        foodDocumentRepository.save(new FoodDocument(food));
        return food.getId();
    }

    public List<FoodResponseDto.Simple> getFoodsSimpleByUserId() {
        User user = userService.getCurrentUser();
        //user id로 음식 리스트 가져옴
        List<Food> foods = foodRepository.findAllByWriterId(user.getId());

        //음식이 없는경우
        if (foods.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " 해당하는 userId 의 음식을 찾을 수 없습니다.");
        }

        return FoodResponseDto.Simple.of(foods);
    }


    public List<FoodResponseDto.Info> getFoodsInfoByUserId() {
        User user = userService.getCurrentUser();
        //user id로 음식 리스트 가져옴
        List<Food> foods = foodRepository.findAllByWriterId(user.getId());

        //음식이 없는경우
        if (foods.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " 해당하는 userId 의 음식을 찾을 수 없습니다.");
        }

        return FoodResponseDto.Info.of(foods);
    }

   public List<FoodResponseDto.RefrigeratorFood> refrigeratorFood(){
       Integer userId = userService.getCurrentUser().getId();
       List<Food> foods = foodRepository.findRefrigeratorFood(userId);

       return FoodResponseDto.RefrigeratorFood.of(foods);
   }


    public FoodResponseDto.Info findFoodById(Integer id) {
        return FoodResponseDto.Info.of(foodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "음식이 존재하지 않습니다.")
        ));
    }

    public void deleteFoodById(Integer foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 음식이 없습니다"));
        List<Recipe> recipes = recipeService.getAllByFoodId(foodId);


        foodImageService.deleteAllFoodImagesByFoodId(foodId);
        foodRepository.delete(food);
        recipeService.deleteAllRecipe(recipes);
    }


    public Integer updateFood(FoodRequestDto.FoodRegister data,Integer foodId) throws Exception{
        Food food= foodRepository.findById(foodId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 음식이 없습니다"));
        food.setName(data.getName());
        food.setCategory(FoodCategory.valueOf( data.getCategory()));
        food.setDescription(data.getDescription());
        food.setImages(null);
        food.setMainImage(null);
        foodImageService.deleteAllFoodImagesByFoodId(foodId);

        /*foodRepository.clear ??*/

        List<MultipartFile> mainImage=new ArrayList<>();
        mainImage.add(data.getMainImage());
        food.setMainImage(foodImageService.registerImages(mainImage, food.getDescription(),food));
        foodImageService.registerImages(data.getImages(), data.getImageDescriptions(), food);
        foodRepository.save(food);




/*
        JSONParser jsonParse = new JSONParser().parse()*/


        String[] ingredientAmounts = data.getIngredientAmount().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", "").split(",");
        Iterator<String> iter = Arrays.stream(ingredientAmounts).iterator();
        String[] ingredients = data.getIngredient().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\\"", "").replaceAll("\\'", "").split(",");


        //재료별 재료 양 필수
        if (ingredients.length != ingredientAmounts.length)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "재료와 재료 양의 개수를 맞춰주세요");

        //recipe 생성
        for (String ingredient : ingredients) {
            if (iter.hasNext()) {
                String ingredientName = ingredient.substring(0, ingredient.length()).strip();
                ingredientService.joinIngredient(Ingredient.builder().name(ingredientName).build());
                //recipe save
                Recipe recipe = Recipe.builder()
                        .food(food)
                        .amount(iter.next().strip())
                        .build();
                recipe.setIngredient(ingredientService.getIngredientByName(ingredientName));
                recipeService.joinRecipe(recipe);
            }
        }



        return food.getId();
    }

    @Scheduled(cron="0 0 12 * * *")
    public void todayFood() {
        List<Food> food = foodRepository.findTodayFood();
    }

    public Page<FoodDocument> searchFood(String query, Pageable pageable) {
        MultiMatchQueryBuilder matchQuery = QueryBuilders.multiMatchQuery(query, "name", "description");
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery).withPageable(pageable).build();
        SearchHits<FoodDocument> search = elasticsearchOperations.search(searchQuery, FoodDocument.class);
        SearchPage<FoodDocument> searchPage = SearchHitSupport.searchPageFor(search, searchQuery.getPageable());
        return (Page<FoodDocument>) SearchHitSupport.unwrapSearchHits(searchPage);
    }
}

