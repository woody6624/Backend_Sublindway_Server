package SublindWay_server.service;

import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.entity.SubwayDetailEntity;
import SublindWay_server.repository.SubwayDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
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
        sendDTO.setSubwayNum(subwayDetailEntity.getSubwayNum());
        return sendDTO;
    }
//본인 좌표값에서 가장 가까운 지하철 좌표를 찾아줍니다.
    public SubwayDetailDTO getSubwayDetailsByLocation(double locationX,double locationY){
        List<SubwayDetailEntity> subwayDetailEntity=subwayDetailRepository.findAll();
        List<SubwayDetailEntity> result=subwayDetailEntity.stream()
                .sorted(Comparator.comparingDouble(entity ->
                        Math.sqrt(Math.pow(entity.getSubwayPositionX() - locationX, 2) + Math.pow(entity.getSubwayPositionY() - locationY, 2))))
                .collect(Collectors.toList());
        SubwayDetailDTO sendDTO=new SubwayDetailDTO();
        sendDTO.setSubwayName(result.get(0).getSubwayName());
        System.out.println(result.get(0).getSubwayName());
        sendDTO.setSubwayNum(result.get(0).getSubwayLine());
        return sendDTO;
    }
}
