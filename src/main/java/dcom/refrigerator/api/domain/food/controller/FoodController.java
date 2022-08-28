package dcom.refrigerator.api.domain.food.controller;


import dcom.refrigerator.api.domain.food.dto.FoodRequestDto;
import dcom.refrigerator.api.domain.food.dto.FoodResponseDto;
import dcom.refrigerator.api.domain.food.service.FoodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Recipe Controller"})
@Slf4j
@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @ApiOperation("음식 등록, 헤더에 userId 담아야됨 ")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> registerRecipe(@Valid @ModelAttribute FoodRequestDto.FoodRegister foodRegister) throws Exception {
        return ResponseEntity.ok(foodService.joinFood(foodRegister));


    }

    @ApiOperation("음식- 레시피들 검색, 헤더에 userId 담아야됨 ")
    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity< List<FoodResponseDto.FoodRecipesByUserInfo> >getFoodRecipes(){


        return ResponseEntity.ok(foodService.getFoodRecipesByUserId());

    }


}
