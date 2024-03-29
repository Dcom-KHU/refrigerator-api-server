package dcom.refrigerator.api.domain.user;

import dcom.refrigerator.api.domain.food.Food;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128)
    private String name;

    @Column(length = 128)
    private String nickname;

    @Column(length = 128)
    private String email;

    @Column(length = 128)
    private String password;

    @Column
    private Integer point;

    @Column(name = "notification_food")
    private Boolean notificationFood;

    @Column(name = "notification_refrigerator")
    private Boolean notificationRefrigerator;

    @Column
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todayFood_id")
    private Food todayFood;
}
