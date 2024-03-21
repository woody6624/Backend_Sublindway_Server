package SublindWay_server.Dto;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class VoiceDTO {
    private final Long voice_id;
    private final String voice_data;

}
