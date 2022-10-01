package dcom.refrigerator.api.domain.food;

import dcom.refrigerator.api.domain.foodImage.FoodImage;
import dcom.refrigerator.api.domain.recipe.Recipe;
import dcom.refrigerator.api.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    @Column(length = 128)
    private String name;

    @Column
    private String description;

    @Column
    private String mainImage;

    @Column
    private FoodCategory category;

    @OneToMany(mappedBy = "food", cascade = {CascadeType.ALL})
    private Set<Recipe> recipes;

    @OneToMany(mappedBy = "food", cascade = {CascadeType.ALL})
    private Set<FoodImage> images;

    @Column
    private Integer ingredientCount;
}
