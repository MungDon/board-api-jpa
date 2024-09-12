package com.example.boardapi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.boardapi.entity.Board;
import com.example.boardapi.entity.Image;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.repository.ImageRepository;
import com.example.boardapi.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.directory}")
    private String uploadDir;

    @Value("${upload.url}")
    private String uploadUrl;

    @Transactional
    public String processContentWithImages(Board board, String content) {
        StringBuilder renewContent = new StringBuilder();
        int lastIdx = 0;
        int imageStart;

        // 이미지 변환 처리
        // base 64 인코딩된 이미지는 "data:image" 로 시작함, indexOf 는 찾는 타켓이 없을경우 -1 반환 == 이미지 없음
        while ((imageStart = content.indexOf("data:image", lastIdx)) != -1) {
            //content 에서 첫부분부터 이미지 부분 전까지 저장
            renewContent.append(content, lastIdx, imageStart);

            // 이미지 끝부분을 찾음 (Quill에서 "/>"로 끝남)
            int imageEnd = content.indexOf("\"/>", imageStart);
            CommonUtils.throwCustomExceptionIf(imageEnd == -1, ErrorCode.IMAGE_NOT_VALID);

            // 이미지의 base64 코드가 시작되는 부분
            int base64Start = content.indexOf(",", imageStart)+1;

            // base64 인코딩된 이미지의  base64 코드 부분
            String base64Data = content.substring(base64Start,imageEnd);

            // 디코딩
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
            Image uploadFile = uploadImageFile(decodedBytes,board);
            renewContent.append(uploadFile.getUploadUrl());

            lastIdx = imageEnd + 3;
        }
        renewContent.append(content.substring(lastIdx));

        return renewContent.toString();
    }

    @Transactional
    public Image uploadImageFile(byte[] decodedBytes, Board board){
        String uuid = UUID.randomUUID().toString().replace("-","");
        String s3UploadPath = uploadDir+"/"+uuid;

        amazonS3.putObject(bucketName,s3UploadPath,new ByteArrayInputStream(decodedBytes),null);

        Image uploadImage = Image.builder()
                .fileName(uuid)
                .uploadUrl(uploadUrl+"/"+s3UploadPath)
                .board(board)
                .build();
        return uploadImage;
    }
}
