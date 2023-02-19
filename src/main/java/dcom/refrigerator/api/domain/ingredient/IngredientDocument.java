package dcom.refrigerator.api.domain.ingredient;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@ApiModel(value = "재료 검색 결과 모델")
@Document(indexName = "user")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDocument {
    @Id
    private Integer id;

    @Field(type = FieldType.Text)
    private String name;

    public IngredientDocument(Ingredient ingredient) {
        this.id = ingredient.getId();
        this.name = ingredient.getName();
    }
}
