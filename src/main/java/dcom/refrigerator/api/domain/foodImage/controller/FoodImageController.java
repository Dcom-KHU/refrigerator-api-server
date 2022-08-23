package dcom.refrigerator.api.domain.foodImage.controller;


import dcom.refrigerator.api.domain.food.service.FoodService;
import dcom.refrigerator.api.domain.foodImage.FoodImage;
import dcom.refrigerator.api.domain.foodImage.dto.FoodImageResponseDto;
import dcom.refrigerator.api.domain.foodImage.service.FoodImageService;
import dcom.refrigerator.api.domain.recipe.dto.RecipeRequestDto;
import dcom.refrigerator.api.domain.recipe.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

@Api(tags = {"Food image Controller"})
@Slf4j
@RestController
@RequestMapping("/foodImage")
@RequiredArgsConstructor
public class FoodImageController {

    private final FoodImageService foodImageService;



    @GetMapping("/download/{foodImageId}")
    @ApiOperation("해당 아이디를 가진 이미지의 정보를 반환 합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<FoodImageResponseDto.FoodImageData> getFoodImageById(@ApiParam(value="image ID", required = true) @PathVariable final Integer foodImageId) {
        return ResponseEntity.ok(FoodImageResponseDto.FoodImageData.of( foodImageService.getFoodImageById(foodImageId)));
    }
}
