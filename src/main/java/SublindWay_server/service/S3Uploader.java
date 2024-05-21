package SublindWay_server.service;

import SublindWay_server.entity.ImageEntity;
import SublindWay_server.entity.UserEntity;
import SublindWay_server.repository.ImageRepository;
import SublindWay_server.repository.UserRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class S3Uploader {
    private final AmazonS3 amazonS3;
    private final String bucket;

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Autowired
    public S3Uploader(AmazonS3 amazonS3, @Value("${AWS_S3_BUCKET}") String bucket, ImageRepository imageRepository, UserRepository userRepository) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;

        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public String uploadImageFile(MultipartFile multipartFile,String kakaoId,String dirName) throws IOException {
        // 파일 이름에서 공백을 제거한 새로운 파일 이름 생성
        String originalFileName = multipartFile.getOriginalFilename();

        // UUID를 파일명에 추가
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid;

        String fileName = uniqueFileName;
        log.info("fileName: " + uniqueFileName);
        File uploadFile = convert(multipartFile,fileName);
        //이미지 저장 부분
            ImageEntity imageEntity=new ImageEntity();
            imageEntity.setImageUUID(uuid);
            imageEntity.setLocalDateTime(LocalDate.now().atStartOfDay());
            Optional<UserEntity> userEntity=userRepository.findById(kakaoId);
            imageEntity.setUserEntity(userEntity.get());
            imageRepository.save(imageEntity);
            String uploadImageUrl = putS3(uploadFile, fileName);

        return uploadImageUrl;
    }



    private File convert(MultipartFile file, String fileUUID) throws IOException {
        String uniqueFileName = fileUUID + ".jpg";

        // 저장할 디렉토리 경로 설정 (예: 'uploads' 폴더 내부)
        File directory = new File("uploads");
        if (!directory.exists()) {
            directory.mkdirs();  // 디렉토리가 없으면 생성
        }

        // 파일을 저장할 전체 경로 생성
        File convertFile = new File(directory, uniqueFileName);
        if (!convertFile.createNewFile()) {
            log.info("File already exists: {}", convertFile.getAbsolutePath());
        }

        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생: {}", e.getMessage());
            throw e;
        }
        return convertFile;
    }


    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return fileName;
    }


    public void removeNewFile(File targetFile) {
        // 현재 작업 디렉토리에 기반한 'uploads' 폴더 경로 사용
        String baseDir = System.getProperty("user.dir");
        File fullPath = new File(baseDir + File.separator + "uploads" + File.separator + targetFile.getName());

        // 파일 삭제 시도
        if (fullPath.delete()) {
            log.info("파일이 삭제되었습니다: {}", fullPath.getPath());
        } else {
            log.info("파일이 삭제되지 못했습니다: {}", fullPath.getPath());
        }
    }


    public void deleteFile(String fileName) {
        try {
            // URL 디코딩을 통해 원래의 파일 이름을 가져옴
            String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("Deleting file from S3: " + decodedFileName);
            amazonS3.deleteObject(bucket, decodedFileName);
        } catch (UnsupportedEncodingException e) {
            log.error("Error while decoding the file name: {}", e.getMessage());
        }
    }

}
