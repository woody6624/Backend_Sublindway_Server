package SublindWay_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "ocr_result")
@Getter
@Setter
@RequiredArgsConstructor
public class OcrResultEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "image_uuid")
    private ImageEntity imageEntity;

    @Column(name="uphill_downward")
    private Boolean upDown;

    @Column(name="first_subway_number")
    private Integer firstNum;

    @Column(name="second_subway_number")
    private Integer secondNum;

    @Column(name="third_subway_number")
    private Integer thirdNum;

}
