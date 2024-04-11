package SublindWay_server.service;

import SublindWay_server.dto.SubwayDetailDTO;
import SublindWay_server.entity.SubwayDetailEntity;
import SublindWay_server.repository.SubwayDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubwaySearchService {
    private SubwayDetailRepository subwayDetailRepository;

    @Autowired
    public SubwaySearchService(SubwayDetailRepository subwayDetailRepository) {
        this.subwayDetailRepository = subwayDetailRepository;
    }

    public SubwayDetailDTO getSubwayName(double locationX,double locationY){
        SubwayDetailEntity subwayDetailEntity=subwayDetailRepository.findByLocationXY(locationX,locationY);
        SubwayDetailDTO sendDTO=new SubwayDetailDTO();
        sendDTO.setSubwayName(subwayDetailEntity.getSubwayName());
        sendDTO.setSubwayNum(subwayDetailEntity.getSubwayNum());
        return sendDTO;
    }


}
