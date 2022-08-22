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


    @ApiOperation(" 음식-레시피를 DB에 등록 합니다. header에 userId 필요")
    @PostMapping("/register")
    public ResponseEntity<Void> registerRecipe(@RequestPart RecipeRequestDto.RecipeRegister recipeRegister) throws URISyntaxException {

        recipeService.joinRecipe(recipeRegister,recipeRegister.getImages());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*@ApiOperation("레시피를 DB에서 삭제 합니다. food도 같이 미국감")
    @DeleteMapping(value = "/delete/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@ApiParam(value="레시피 ID", required = true) @PathVariable final Integer recipeId) {
        recipeService.deleteRecipe(recipeId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }*/



}


