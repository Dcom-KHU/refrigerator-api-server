package dcom.refrigerator.api.domain.recipe.controller;


import dcom.refrigerator.api.domain.recipe.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Recipe Controller"})
@Slf4j
@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;



    @ApiOperation("레시피 삭제 recipe id가 필요해요  ")
    @DeleteMapping(value = "/delete/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@ApiParam(value="레시피 ID", required = true) @PathVariable final Integer recipeId) {
        recipeService.deleteRecipeById(recipeId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


}

