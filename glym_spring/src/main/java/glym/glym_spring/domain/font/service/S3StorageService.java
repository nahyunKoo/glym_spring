package glym.glym_spring.domain.font.service;

import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import glym.glym_spring.global.exception.ErrorCode;
import glym.glym_spring.global.exception.domain.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3StorageService {
    private final AmazonS3 s3Client;


    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    public String storeImage(MultipartFile file, String uuid, String fontName) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String timestamp = String.valueOf(System.currentTimeMillis());
        String key = String.format("%s_%s_original.%s", uuid, timestamp, extension);

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // 메타데이터 추가
            metadata.addUserMetadata("fontName", fontName);
            metadata.addUserMetadata("uploadTime", timestamp);

            s3Client.putObject(new PutObjectRequest(
                    bucketName,
                    key,
                    file.getInputStream(),
                    metadata
            ));

            return String.format("s3://%s/%s", bucketName, key);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.IMAGE_SAVE_ERROR);
        }
    }
}