package semohan.semohan.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import semohan.semohan.global.exception.CustomException;
import semohan.semohan.global.exception.ErrorCode;


import java.io.IOException;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class AwsS3Uploader {
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;


    public String imgPath(String dirName) {
        return dirName + "/" + UUID.randomUUID(); // 파일 경로 반환
    }

    public String putS3(MultipartFile multipartFile, String fileName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());
        try {
            amazonS3Client.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }
        return amazonS3Client.getUrl(bucket, fileName).toString(); // 업로드된 파일의 S3 URL 주소 반환
    }

    public void deleteS3(String path) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, path));
    }
}