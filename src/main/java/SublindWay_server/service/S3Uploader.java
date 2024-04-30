package SublindWay_server.service;

import SublindWay_server.entity.ImageEntity;
import SublindWay_server.repository.ImageRepository;
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
import java.util.UUID;


@Slf4j
@Service
public class S3Uploader {
    @Resource
    private ResourceLoader resourceLoader;
    private final AmazonS3 amazonS3;
    private final String bucket;

    private final ImageRepository imageRepository;

    @Autowired
    public S3Uploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket, ImageRepository imageRepository) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;

        this.imageRepository = imageRepository;
    }

    public String uploadImageFile(MultipartFile multipartFile, String dirName) throws IOException {
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
            imageEntity.setLocalDateTime(LocalDate.now());
            imageRepository.save(imageEntity);
            String uploadImageUrl = putS3(uploadFile, fileName);
            return uploadImageUrl;
    }



    private File convert(MultipartFile file,String fileUUID) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uuid = fileUUID;
        String uniqueFileName = uuid+".jpg";

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


    public void removeNewFile(File targetFile) {
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
    public String updateFile(MultipartFile newFile, String oldFileName, String dirName) throws IOException {
        // 기존 파일 삭제
        log.info("S3 oldFileName: " + oldFileName);
        deleteFile(oldFileName);
        // 새 파일 업로드
        return uploadImageFile(newFile, dirName);
    }
    public String getFileURL(String fileName) {
        System.out.println("넘어오는 파일명 : "+fileName);
        String imgName = (fileName).replace(File.separatorChar, '/');
        return amazonS3.generatePresignedUrl(new GeneratePresignedUrlRequest(bucket, imgName)).toString();
    }
}
