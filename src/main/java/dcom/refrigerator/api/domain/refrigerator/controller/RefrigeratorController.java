package dcom.refrigerator.api.domain.refrigerator.controller;


import dcom.refrigerator.api.domain.food.dto.FoodRequestDto;
import dcom.refrigerator.api.domain.food.service.FoodService;
import dcom.refrigerator.api.domain.recipe.service.RecipeService;
import dcom.refrigerator.api.domain.refrigerator.dto.RefrigeratorRequestDto;
import dcom.refrigerator.api.domain.refrigerator.dto.RefrigeratorResponseDto;
import dcom.refrigerator.api.domain.refrigerator.service.RefrigeratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Refrigerator Controller"})
@Slf4j
@RestController
@RequestMapping("/refrigerator")
@RequiredArgsConstructor
public class RefrigeratorController {
    private final RefrigeratorService refrigeratorService;

    @ApiOperation("음식을 user Id로 검색, 헤더에 userId 담아야됨")
    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RefrigeratorResponseDto.WithIngredient>> getRefrigerator() {
        return ResponseEntity.ok(refrigeratorService.getRefrigerator());
    }

    @ApiOperation("재료 등록, 헤더에 userId 담아야됨")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> addIngredient(@Valid @RequestBody RefrigeratorRequestDto.addIngredient data) {
        refrigeratorService.addIngredient(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("재료 수정, 헤더에 userId 담아야됨")
    @PostMapping("/modify")
    public ResponseEntity<Void> modifyIngredient(@Valid @RequestBody RefrigeratorRequestDto.modifyIngredient data) {
        refrigeratorService.modifyIngredient(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @ApiOperation("재료 삭제, 헤더에 userId 담아야됨")
    @DeleteMapping(value="/delete/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@ApiParam(value="재료 ID", required = true) @PathVariable final Integer ingredientId) {
        refrigeratorService.deleteIngredient(ingredientId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
