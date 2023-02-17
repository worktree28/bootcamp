package com.bootcamp.service;

import com.bootcamp.model.Note;
import com.bootcamp.model.Role;
import com.bootcamp.model.User;
import com.bootcamp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.List;

import static software.amazon.awssdk.transfer.s3.SizeConstant.MB;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Map<String,String> pushLog(String email){
        Optional<User> user= userRepository.findByEmail(email);
        if(user.isEmpty())  throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        else if(user.get().getRole()== Role.ADMIN){
            try{
                uploadAWS();
            }
            catch (Exception e){
                return Map.of("error",e.toString());
            }
        }
        return Map.of("success","logs uploaded");
    }
    @Async
    void uploadAWS()throws IOException {
        File file=new File(Paths.get("myappcpy.log").toString());
        file.delete();
        try{
            Files.copy(Paths.get("myapp.log"), Paths.get("myappcpy.log"));
        }catch (Exception e){
            throw new IOException("failed");
        }
        S3AsyncClient s3AsyncClient = S3AsyncClient.crtBuilder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .targetThroughputInGbps(20.0)
                .minimumPartSizeInBytes(8 * MB)
                .build();
        S3TransferManager transferManager = S3TransferManager.builder()
                .s3Client(s3AsyncClient)
                .build();
        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
                .putObjectRequest(req -> req.bucket("bucket").key("key"))
                .addTransferListener(LoggingTransferListener.create())
                .source(Paths.get("myappcpy.log"))
                .build();
        FileUpload upload = transferManager.uploadFile(uploadFileRequest);
        upload.completionFuture().join();

    }
    public List<Note> getSharedNotes(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user with email"+email+" not found");
        return userOpt.get().getSharedFrom();
    }
}
