package SublindWay_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "subway_detail")
@Getter
@Setter
@RequiredArgsConstructor
public class SubwayDetailEntity {
    @Id
    @Column(name = "subway_number")
    private Integer subwayNum;

    @Column(name = "subway_line")
    private String subwayLine;

    @Column(name = "subway_name")
    private String subwayName;

    @Column(name= "subway_position_x")
    private Double subwayPositionX;

    @Column(name= "subway_position_y")
    private Double subwayPositionY;

}
