package SublindWay_server.Dto;

import SublindWay_server.Domain.CameraCheck;
import SublindWay_server.Domain.UserCameraData;
import SublindWay_server.Domain.Voice;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class CameraCheckDTO {
    private final Long user_camera_data_id;
    private final UserCameraData userCameraData;
    private final Voice voice;

    public CameraCheck toEntity(){ // DTO -> Entity
        return CameraCheck.builder()
                .user_camera_data_id(user_camera_data_id)
                .userCameraData(userCameraData)
                .voice(voice).build();
    }

    public CameraCheckDTO toDTO(CameraCheck cameraCheck){ // Entity -> DTO
        return CameraCheckDTO.builder()
                .user_camera_data_id(cameraCheck.getUser_camera_data_id())
                .userCameraData(cameraCheck.getUserCameraData())
                .voice(cameraCheck.getVoice()).build();
    }
}
