package dcom.refrigerator.api.domain.refrigerator.controller;


import dcom.refrigerator.api.domain.recipe.service.RecipeService;
import dcom.refrigerator.api.domain.refrigerator.dto.RefrigeratorResponseDto;
import dcom.refrigerator.api.domain.refrigerator.service.RefrigeratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"Recipe Controller"})
@Slf4j
@RestController
@RequestMapping("/refrigerator")
@RequiredArgsConstructor
public class RefrigeratorController {
    private final RefrigeratorService refrigeratorService;

    @ApiOperation("음식을 user Id로 검색, 헤더에 userId 담아야됨 ")
    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RefrigeratorResponseDto.WithIngredient>> getRefrigerator() {
        return ResponseEntity.ok(refrigeratorService.getRefrigerator());
    }
}
