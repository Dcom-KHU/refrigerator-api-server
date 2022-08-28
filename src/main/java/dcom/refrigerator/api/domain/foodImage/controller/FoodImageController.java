package dcom.refrigerator.api.domain.foodImage.controller;


import dcom.refrigerator.api.domain.foodImage.dto.FoodImageResponseDto;
import dcom.refrigerator.api.domain.foodImage.service.FoodImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Api(tags = {"Food image Controller"})
@Slf4j
@RestController
@RequestMapping("/foodImage")
@RequiredArgsConstructor
public class FoodImageController {

    private final FoodImageService foodImageService;



    @GetMapping("/info/foodImageId/{foodImageId}")
    @ApiOperation("해당 음식 사진 아이디를 가진 이미지의 정보를 반환 합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<FoodImageResponseDto.FoodImageData> getFoodImageById(@ApiParam(value="image ID", required = true) @PathVariable final Integer foodImageId) {
        return ResponseEntity.ok(FoodImageResponseDto.FoodImageData.of( foodImageService.getFoodImageById(foodImageId)));
    }
    @ApiOperation("해당 음식 사진 아이디를 가진 이미지다운로드.")
    @GetMapping("/attach/{foodImageId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Integer foodImageId) throws MalformedURLException {
        //...itemId 이용해서 고객이 업로드한 파일 이름인 uploadFileName랑 서버 내부에서 사용하는 파일 이름인 storeFileName을 얻는다는 내용은 생략


        UrlResource resource = new UrlResource("file:" + foodImageService.getFoodImageById(foodImageId).getFilePath());

        //한글 파일 이름이나 특수 문자의 경우 깨질 수 있으니 인코딩 한번 해주기
        String encodedUploadFileName = UriUtils.encode(foodImageService.getFoodImageById(foodImageId).getOriginFileName(),
                StandardCharsets.UTF_8);

        //아래 문자를 ResponseHeader에 넣어줘야 한다. 그래야 링크를 눌렀을 때 다운이 된다.
        //정해진 규칙이다.
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @GetMapping("/info/foodId/{foodId}")
    @ApiOperation("해당 음식 아이디를 가진 이미지의 정보를 반환 합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity <List<FoodImageResponseDto.FoodImageData>> getAllFoodImageByFoodId(@ApiParam(value="food ID", required = true) @PathVariable final Integer foodId) {
        return ResponseEntity.ok(FoodImageResponseDto.FoodImageData.of( foodImageService.getAllFoodImageByFoodId(foodId)));
    }

}
