package dcom.refrigerator.api.domain.ingredient;

import com.sun.istack.NotNull;
import lombok.*;
import javax.persistence.*;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {



    @Id
    @NotNull
    @Column(name = "ingredient_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 128)
    private String name;
}
