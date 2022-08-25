package dcom.refrigerator.api.domain.recipe.controller;


import dcom.refrigerator.api.domain.ingredient.dto.IngredientRequestDto;
import dcom.refrigerator.api.domain.ingredient.service.IngredientService;
import dcom.refrigerator.api.domain.recipe.dto.RecipeRequestDto;
import dcom.refrigerator.api.domain.recipe.dto.RecipeResponseDto;
import dcom.refrigerator.api.domain.recipe.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@Api(tags = {"Recipe Controller"})
@Slf4j
@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;



    @ApiOperation("레시피 등록, 헤더에 userId 담아야됨 ")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> registerRecipe(@Valid @ModelAttribute RecipeRequestDto.RecipeRegister recipeRegister) throws Exception {
        return ResponseEntity.ok(recipeService.joinRecipe(recipeRegister));


    }


    @ApiOperation("레시피 info 확인하기")
    @GetMapping("/{userId}/recipeInfo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List< RecipeResponseDto.RecipeInfo>>recipeInformation(@Valid @PathVariable Integer userId) throws Exception {
        List< RecipeResponseDto.RecipeInfo> recipeInfos=recipeService.getRecipeByUserId(userId);

        return ResponseEntity.ok(recipeInfos);
    }

    @ApiOperation("레시피를 삭제 합니다.")
    @DeleteMapping(value = "/delete/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@ApiParam(value="recipe ID", required = true) @PathVariable final Integer recipeId) {
        recipeService.deleteRecipe(recipeId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }



}


