package dcom.refrigerator.api.domain.recipe.controller;


import dcom.refrigerator.api.domain.ingredient.dto.IngredientRequestDto;
import dcom.refrigerator.api.domain.ingredient.service.IngredientService;
import dcom.refrigerator.api.domain.recipe.dto.RecipeRequestDto;
import dcom.refrigerator.api.domain.recipe.dto.RecipeResponseDto;
import dcom.refrigerator.api.domain.recipe.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.net.URISyntaxException;

@Api(tags = {"Recipe Controller"})
@Slf4j
@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;


    @ApiOperation("레시 등록 합니다.")
    @PostMapping("/register")
    public ResponseEntity<Void> registerRecipe(@RequestBody final RecipeRequestDto.RecipeRegister data) throws URISyntaxException {

        recipeService.joinRecipe(data);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}