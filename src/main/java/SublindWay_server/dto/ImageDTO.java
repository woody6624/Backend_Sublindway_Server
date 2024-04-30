package SublindWay_server.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class ImageDTO {
    private String imageUUID;
    private String kakaoId; // UserEntity 대신에 사용자 ID만 포함
    private LocalDate timestamp;
}
