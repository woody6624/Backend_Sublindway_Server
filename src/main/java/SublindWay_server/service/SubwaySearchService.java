package SublindWay_server.service;

import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.entity.SubwayDetailEntity;
import SublindWay_server.repository.SubwayDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubwaySearchService {
    private SubwayDetailRepository subwayDetailRepository;

    @Autowired
    public SubwaySearchService(SubwayDetailRepository subwayDetailRepository) {
        this.subwayDetailRepository = subwayDetailRepository;
    }
//지하철 좌표값으로
    public SubwayDetailDTO getSubwayName(double locationX,double locationY){
        SubwayDetailEntity subwayDetailEntity=subwayDetailRepository.findByLocationXY(locationX,locationY);
        SubwayDetailDTO sendDTO=new SubwayDetailDTO();
        sendDTO.setSubwayName(subwayDetailEntity.getSubwayName());
        sendDTO.setSubwayNum(String.valueOf(subwayDetailEntity.getSubwayNum()));
        return sendDTO;
    }
//본인 좌표값에서 가장 가까운 지하철 좌표를 찾아줍니다.
public SubwayDetailDTO getSubwayDetailsByLocation(double locationX, double locationY) {
    List<SubwayDetailEntity> subwayDetailEntity = subwayDetailRepository.findAll();
    BigDecimal locX = new BigDecimal(locationX);
    BigDecimal locY = new BigDecimal(locationY);

    List<SubwayDetailEntity> result = subwayDetailEntity.stream()
            .sorted(Comparator.comparingDouble(entity -> {
                // entity.getSubwayPositionX() 또는 entity.getSubwayPositionY()가 null이면 0.0으로 대체
                BigDecimal posX = Optional.ofNullable(entity.getSubwayPositionX()).map(BigDecimal::new).orElse(BigDecimal.ZERO);
                BigDecimal posY = Optional.ofNullable(entity.getSubwayPositionY()).map(BigDecimal::new).orElse(BigDecimal.ZERO);

                BigDecimal diffX = locX.subtract(posX);
                BigDecimal diffY = locY.subtract(posY);
                return diffX.pow(2).add(diffY.pow(2)).sqrt(MathContext.DECIMAL64).doubleValue();
            }))
            .collect(Collectors.toList());

    SubwayDetailDTO sendDTO = new SubwayDetailDTO();
    if (!result.isEmpty()) {
        sendDTO.setSubwayName(result.get(0).getSubwayName());
        System.out.println(result.get(0).getSubwayName());
        sendDTO.setSubwayNum(result.get(0).getSubwayLine());
    } else {
        // 결과가 비었을 경우의 로직 처리
        System.out.println("No subway stations found.");
        sendDTO.setSubwayName("No station");
        sendDTO.setSubwayNum("0");
    }

    return sendDTO;
}
}
