package dcom.refrigerator.api.domain.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 128)
    private String name;

    @Column(length = 128)
    private String nickname;

    @Column
    private Integer point;

    @Column(name = "notification_food")
    private Boolean notificationFood;

    @Column(name = "notification_refrigerator")
    private Boolean notificationRefrigerator;
}
