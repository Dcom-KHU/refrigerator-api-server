package dcom.refrigerator.api.domain.recipe;

import com.sun.istack.NotNull;
import dcom.refrigerator.api.domain.food.Food;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Food food;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private Set<Ingredient> ingredients=new HashSet<>();

    @Column(length = 128)
    private String amount;





}

