package dcom.refrigerator.api.domain.food.dto;

import dcom.refrigerator.api.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Setter
public class FoodRequestDto {
    @ApiModel(value="food 등록")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FoodRegister {

        @NotEmpty
        @Size( max = 128, message = "길이는 128자 이내여야 합니다.")
        @ApiModelProperty(value = "음식 이름", required = true)
        private String name;

        @NotEmpty
        @ApiModelProperty(value = "레시피 설명", required = true)
        private String description;

        @NotEmpty
        @ApiModelProperty(value = "음식 카테고리", required = true)
        private String category;

        @NotEmpty
        @ApiModelProperty(value = "재료 이름 리스트(String type), String Array를 stringify해서 보내 주세요. (ex: JSON.stringify)", required = true)
        private String ingredient;

        @NotEmpty
        @ApiModelProperty(value = "재료 개수 리스트(String type), String Array를 stringify해서 보내 주세요. (ex: JSON.stringify)", required = true)
        private String ingredientAmount;

        @ApiModelProperty(value = "이미지 파일들, String Array를 stringify해서 보내 주세요. (ex: JSON.stringify)", required = true)
        private List<MultipartFile> images= new ArrayList<>();

        @ApiModelProperty(value = "이미지 파일들, String Array를 stringify해서 보내 주세요. (ex: JSON.stringify)", required = true)
        private MultipartFile mainImage;

        @ApiModelProperty(value = "이미지 설명들, String Array를 stringify해서 보내 주세요. (ex: JSON.stringify)", required = true)
        private String imageDescriptions;


    }



}
