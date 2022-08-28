package dcom.refrigerator.api.domain.recipe.controller;


import dcom.refrigerator.api.domain.recipe.service.RecipeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Recipe Controller"})
@Slf4j
@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;



}

