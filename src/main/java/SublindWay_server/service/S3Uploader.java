package SublindWay_server.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final String bucket;


    @Autowired
    public S3Uploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;

    }
/*
    public String uploadPersonal(MultipartFile multipartFile, String dirName,String reviewId) throws IOException {
        // 파일 이름에서 공백을 제거한 새로운 파일 이름 생성
        String originalFileName = multipartFile.getOriginalFilename();

        // UUID를 파일명에 추가
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        String fileName = "uniqueFileName";
        log.info("fileName: " + uniqueFileName);
        File uploadFile = convert(multipartFile);
        Optional<UserReviewEntity> userReviewEntity=userReviewRepository.findById(reviewId);
        if(userReviewEntity.isPresent()){
            ReviewImageEntity reviewImageEntity=new ReviewImageEntity();
            reviewImageEntity.setImageId(uuid);
            reviewImageEntity.setUserReviewEntity(userReviewEntity.get());
            reviewImageEntity.setTimestamp(LocalDate.now());
            reviewImageRepository.save(reviewImageEntity);

            String uploadImageUrl = putS3(uploadFile, fileName);
            removeNewFile(uploadFile);
            return uploadImageUrl;
        }
        else{
            return null;
        }
    }

    */



    /*
    public String uploadGroup(MultipartFile multipartFile, String dirName,String muckatId) throws IOException {
        // 파일 이름에서 공백을 제거한 새로운 파일 이름 생성
        String originalFileName = multipartFile.getOriginalFilename();


        // UUID를 파일명에 추가
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid;

        String fileName = uniqueFileName;
        log.info("fileName: " + uniqueFileName);
        File uploadFile = convert(multipartFile);
        //Optional<MuckatListEntity> muckatListEntity=muckatListRepository.findById(muckatId);

        if(muckatListEntity.isPresent()){
            ImageEntity imageEntity=new ImageEntity();
            imageEntity.setImageId(uuid);
            imageEntity.setMuckatListEntity(muckatListEntity.get());
            imageEntity.setTimestamp(LocalDate.now());
            imageRepository.save(imageEntity);
            String uploadImageUrl = putS3(uploadFile, fileName);
            removeNewFile(uploadFile);
            return uploadImageUrl;
        }
        else{
            return null;
        }
    }
*/


    private File convert(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid;

        File convertFile = new File(uniqueFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                log.error("파일 변환 중 오류 발생: {}", e.getMessage());
                throw e;
            }
            return convertFile;
        }
        throw new IllegalArgumentException(String.format("파일 변환에 실패했습니다. %s", originalFileName));
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return fileName;
    }


    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
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
/*
    public String updateFile(MultipartFile newFile, String oldFileName, String dirName) throws IOException {
        // 기존 파일 삭제
        log.info("S3 oldFileName: " + oldFileName);
        deleteFile(oldFileName);
        // 새 파일 업로드
        return upload(newFile, dirName);
    }

*/}
