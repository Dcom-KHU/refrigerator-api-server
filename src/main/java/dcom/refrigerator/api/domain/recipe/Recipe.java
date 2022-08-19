package dcom.refrigerator.api.domain.recipe;

import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "recipe_id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private Food food_id;

    @Column
    @OneToMany(mappedBy = "ingredient")
    private Set<RecipeIngredient> users = new HashSet<>();

    @Column(length = 128)
    private String amount;
}

