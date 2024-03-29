package com.kuit.chatdiary.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Component
@Service
public class S3Uploader {

    private final AmazonS3 amazonS3;

    private final String bucket;


    public S3Uploader(AmazonS3 amazonS3, @Value("${S3_BUCKET}") String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    public String uploadBase64File(String base64File, String dirName) throws IOException {
        String[] base64Components = base64File.split(",");
        byte[] decodedBytes;
        String fileExtension = ".png";
        if (base64Components.length > 1) {
            decodedBytes = Base64.getDecoder().decode(base64Components[1]);

            String mimeType = base64Components[0].split(";")[0].split(":")[1];
            switch (mimeType) {
                case "image/png":
                    break;
                case "image/jpeg":
                    fileExtension = ".jpg";
                    break;
                case "image/webp":
                    fileExtension = ".webp";
                    break;
                case "image/gif":
                    fileExtension = ".gif";
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported file type: " + mimeType);
            }
        } else {
            log.info("No prefix found. Decoding entire string...");
            decodedBytes = Base64.getDecoder().decode(base64File);
        }

        String originalFileName = "upload_" + UUID.randomUUID().toString() + fileExtension;
        String fileName = dirName + "/" + originalFileName;

        File uploadFile = new File(System.getProperty("java.io.tmpdir"), originalFileName);
        try (FileOutputStream fos = new FileOutputStream(uploadFile)) {
            fos.write(decodedBytes);
        } catch (IOException e) {
            log.error("Error writing bytes to file: {}", e.getMessage());
            throw e;
        }

        String uploadImageUrl = putS3(uploadFile, fileName);
        uploadFile.delete();

        return uploadImageUrl;
    }

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // 파일 이름에서 공백을 제거 -> 새로운 파일 이름 생성
        String originalFileName = multipartFile.getOriginalFilename();

        // UUID를 파일명에 추가 (중복 이름 막기위함)
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        String fileName = dirName + "/" + uniqueFileName;
        File uploadFile = convert(multipartFile);

        String uploadImageUrl = putS3(uploadFile, fileName);
        uploadFile.delete();

        return uploadImageUrl;
    }

    private File convert(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

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
        throw new IllegalArgumentException(String.format("파일 변환에 실패 %s", originalFileName));
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

}