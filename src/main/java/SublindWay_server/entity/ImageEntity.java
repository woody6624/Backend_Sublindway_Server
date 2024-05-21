package SublindWay_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
@Getter
@Setter
@RequiredArgsConstructor
public class ImageEntity {

    @Id
    @Column(name = "image_uuid")
    private String imageUUID;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "kakao_id")
    private UserEntity userEntity;

    @CreationTimestamp
    @Column(name="timestamp")
    private LocalDateTime localDateTime;

    //탑승 or 욜로  or 탑승칸
    @Column(name="yolo_ride_board")
    private String yoloOrRideOrBoard;
}
