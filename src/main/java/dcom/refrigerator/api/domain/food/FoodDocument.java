package dcom.refrigerator.api.domain.food;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@ApiModel(value = "음식 검색 결과 모델")
@Document(indexName = "user")
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodDocument {
    @Id
    private Integer id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private FoodCategory category;

    @Field(type = FieldType.Keyword, index = false)
    private String mainImage;

    public FoodDocument(Food food) {
        this.id = food.getId();
        this.name = food.getName();
        this.description = food.getDescription();
        this.mainImage = food.getMainImage();
        this.category = food.getCategory();
    }
}
