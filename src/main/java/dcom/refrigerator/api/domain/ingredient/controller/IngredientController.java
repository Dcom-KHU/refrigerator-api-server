package dcom.refrigerator.api.domain.ingredient.controller;


import dcom.refrigerator.api.domain.ingredient.IngredientDocument;
import dcom.refrigerator.api.domain.ingredient.dto.IngredientRequestDto;
import dcom.refrigerator.api.domain.ingredient.service.IngredientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@Api(tags = {"ingredient Controller"})
@Slf4j
@RestController
@RequestMapping("/ingredient")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @ApiOperation("재료를 DB에 등록 합니다.")
    @PostMapping("/register")
    public ResponseEntity<Void> registerIngredient(@RequestBody @Valid final IngredientRequestDto.IngredientRegister data) throws URISyntaxException {

        ingredientService.joinIngredient(data.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("재료를 DB에서 삭제 합니다.")
    @DeleteMapping(value = "/delete/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@ApiParam(value="재료 ID", required = true) @PathVariable final Integer ingredientId) {
        ingredientService.deleteIngredient(ingredientId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("재료를 검색 합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<IngredientDocument>> searchIngredient(@ApiParam(value="검색어", required = true)
                                                                     @RequestParam String query) {
        return ResponseEntity.ok(ingredientService.searchIngredient(query));
    }
}
