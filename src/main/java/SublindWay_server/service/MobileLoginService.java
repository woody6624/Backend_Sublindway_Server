package SublindWay_server.service;

import SublindWay_server.entity.UserEntity;
import SublindWay_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MobileLoginService {

    @Autowired
    UserRepository userRepository;


    public String getKakaoId(String kakaoId,String userName){
        Optional<UserEntity> userEntity=userRepository.findById(kakaoId);//만약 해당 id로 이미 db상에 존재한다면
        if(userEntity.isPresent()){
            return userEntity.get().getMuckatUserId();
        }
        else{//존재하지 않는다면
            UserEntity saveEntity=new UserEntity();
            saveEntity.setMuckatUserId(kakaoId);
            saveEntity.setUserName(userName);
            userRepository.save(saveEntity);
            return kakaoId;
        }
    }
}
