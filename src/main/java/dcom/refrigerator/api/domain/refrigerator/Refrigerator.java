package dcom.refrigerator.api.domain.refrigerator;

import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Refrigerator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private User user_id;

    @ManyToOne
    private Ingredient ingredient_id;

    @Column(length = 128)
    private String expired_date;
}