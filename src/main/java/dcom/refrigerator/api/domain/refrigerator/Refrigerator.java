package dcom.refrigerator.api.domain.refrigerator;

import dcom.refrigerator.api.domain.user.User;
import dcom.refrigerator.api.domain.ingredient.Ingredient;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(length = 128)
    private LocalDateTime expiredDate;
}