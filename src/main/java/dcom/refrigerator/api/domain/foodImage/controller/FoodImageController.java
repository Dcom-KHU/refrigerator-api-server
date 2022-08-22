package dcom.refrigerator.api.domain.foodImage.controller;


import dcom.refrigerator.api.domain.food.service.FoodService;
import dcom.refrigerator.api.domain.foodImage.service.FoodImageService;
import dcom.refrigerator.api.domain.recipe.dto.RecipeRequestDto;
import dcom.refrigerator.api.domain.recipe.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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



    /*@ApiOperation(value = "feed image 조회 ", notes = "feed Image를 반환합니다. 못찾은경우 기본 image를 반환합니다.")
    @GetMapping(value = "image/{imagename}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> userSearch(@PathVariable("imagename") String imagename) throws IOException {
        InputStream imageStream = new FileInputStream("C://images/feed/" + imagename);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();
        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }*/



}
