package dcom.refrigerator.api.domain.food.controller;


import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.food.FoodDocument;
import dcom.refrigerator.api.domain.food.dto.FoodRequestDto;
import dcom.refrigerator.api.domain.food.dto.FoodResponseDto;
import dcom.refrigerator.api.domain.food.service.FoodService;
import dcom.refrigerator.api.domain.refrigerator.dto.RefrigeratorResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"Food Controller"})
@Slf4j
@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @ApiOperation("음식 등록, 헤더에 userId 담아야됨 ")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> registerFood(@Valid
                                                    @ModelAttribute FoodRequestDto.FoodRegister foodRegister) throws Exception {

        return ResponseEntity.ok(foodService.joinFood(foodRegister));
    }

    @ApiOperation("간단한 음식정보 (레시피 제외) 들을 user Id로 검색, 헤더에 userId 담아야됨 ")
    @GetMapping("/simple/userId")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FoodResponseDto.Simple>>FoodRecipesSimpleByUserId(){
        return ResponseEntity.ok(foodService.getFoodsSimpleByUserId());
    }

    @ApiOperation("자세한 음식 정보 (레시피 포함)들을 user Id로 검색, 헤더에 userId 담아야됨 ")
    @GetMapping("/info/userId")
    @ResponseStatus(HttpStatus.OK)public ResponseEntity<List<FoodResponseDto.Info>>FoodRecipesInfoByUserId(){
        return ResponseEntity.ok(foodService.getFoodsInfoByUserId());
    }
        
    

    @ApiOperation("음식을 삭제 합니다.")
    @DeleteMapping(value = "/delete/{foodId}")
    public ResponseEntity<Void> deleteFood(@ApiParam(value="food ID", required = true) @PathVariable final Integer foodId) {
        foodService.deleteFoodById(foodId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("음식을 food Id로 검색")
    @GetMapping("/{foodId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FoodResponseDto.Info> findById(@PathVariable Integer foodId) {
        return ResponseEntity.ok(foodService.findFoodById(foodId));
    }

    @ApiOperation("음식 수정, 헤더에 userId 담아야됨")
    @PutMapping("/modify/{foodId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> modifyFood(@Valid @ModelAttribute FoodRequestDto.FoodRegister foodRegister,@PathVariable Integer foodId) throws Exception {
        return ResponseEntity.ok(foodService.updateFood(foodRegister,foodId));
    }

    @ApiOperation("냉장고 속 재료로 만들 수 있는 음식 조회")
    @GetMapping("/refrigeratorFood")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FoodResponseDto.RefrigeratorFood>> refrigeratorFood() {
        return ResponseEntity.ok(foodService.refrigeratorFood());
    }

    @ApiOperation("음식 검색. 제목과 내용을 이용")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<FoodDocument>> searchFood(@ApiParam(value="검색어", required = true) String query, @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(foodService.searchFood(query, pageable));
    }
}
