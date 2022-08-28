package dcom.refrigerator.api.domain.food;

import dcom.refrigerator.api.domain.user.User;
import lombok.*;

import javax.persistence.*;

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
    private FoodCategory category;
}
