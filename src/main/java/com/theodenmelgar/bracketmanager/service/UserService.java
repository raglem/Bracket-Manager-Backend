package com.theodenmelgar.bracketmanager.service;

import com.theodenmelgar.bracketmanager.config.S3Config;
import com.theodenmelgar.bracketmanager.dto.auth.UserDTO;
import com.theodenmelgar.bracketmanager.exception.BadRequestException;
import com.theodenmelgar.bracketmanager.exception.FileStorageException;
import com.theodenmelgar.bracketmanager.exception.user.UserNotFoundException;
import com.theodenmelgar.bracketmanager.model.User;
import com.theodenmelgar.bracketmanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {
    S3Client s3Client;
    S3Config s3Config;
    UserRepository userRepository;

    public UserService(
            S3Client s3Client, S3Config s3Config,
            UserRepository userRepository) {
        this.s3Client = s3Client;
        this.s3Config = s3Config;
        this.userRepository = userRepository;
    }

    public String changeProfileImage (Long userId, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (image == null || image.isEmpty()) {
            throw new BadRequestException("Profile image cannot be empty");
        }

        String previousKey = user.getProfileImageKey();
        String key = "profile-pictures/" + userId + "/" + UUID.randomUUID() + "-" + image.getOriginalFilename();

        try {
            // Delete prior image if it exists
            if (previousKey != null && !previousKey.isEmpty()){
                s3Client.deleteObject(
                        DeleteObjectRequest.builder()
                                .bucket(s3Config.getBucketName())
                                .key(previousKey)
                                .build()
                );
            }

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(s3Config.getBucketName())
                            .key(key)
                            .contentType(image.getContentType())
                            .build(),
                    RequestBody.fromBytes(image.getBytes())
            );
        }
        catch (IOException e) {
            throw new FileStorageException(e.getMessage(), e);
        }

        user.setProfileImageKey(key);
        userRepository.save(user);
        return s3Config.getPublicUrl(key);
    }

    public void removeProfileImage (Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        String key = user.getProfileImageKey();

        if (key != null && !key.isEmpty()){
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(s3Config.getBucketName())
                            .key(key)
                            .build()
            );
        }

        user.setProfileImageKey("");
        userRepository.save(user);
    }

    public UserDTO constructUserDTO(User user) {
        UserDTO userDTO = new UserDTO(user);
        // Manually compute the image URL (the user object only stores the key)
        if (user.getProfileImageKey() != null) {
            userDTO.setProfileImageURL(getImageURL(user));
        }


        return userDTO;
    }

    public String getImageURL(User user) {
        return s3Config.getPublicUrl(user.getProfileImageKey());
    }
}
