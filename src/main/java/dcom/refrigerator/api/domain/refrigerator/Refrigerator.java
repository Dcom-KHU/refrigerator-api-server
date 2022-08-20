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
    @Column(name = "refrigerator_id")
    private Integer id;

    @ManyToOne
    private User userId;

    @ManyToOne
    @JoinColumn()
    private Ingredient ingredientId;

    @Column(length = 128)
    private String expiredDate;
}