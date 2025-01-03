package com.cromxt.backendservice;


import com.cromxt.grpc.service.example.FileUploadRequest;
import com.cromxt.grpc.service.example.FileUploadResponse;
import com.cromxt.grpc.service.example.FileUploadStatus;
import com.cromxt.grpc.service.example.ReactorFileUploadServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@GrpcService
public class FileUploadServiceImpl extends ReactorFileUploadServiceGrpc.FileUploadServiceImplBase {

    @Override
    public Mono<FileUploadResponse> uploadFile(Flux<FileUploadRequest> request) {

        Path absoluteResourcesPath = Paths.get(System.getProperty("user.dir"),  "uploaded_file.mp4");

        return Mono.create(sink -> {
                    try (FileOutputStream fileOutputStream = new FileOutputStream(absoluteResourcesPath.toFile())) {

                        request.subscribeOn(Schedulers.boundedElastic())
                                .subscribe(fileUploadRequest -> {
                                    try {
                                        fileOutputStream.write(fileUploadRequest.toByteArray());
                                    } catch (Exception e) {
                                        sink.error(e);
                                    }
                                });
                    } catch (Exception e) {
                        sink.error(e);
                    }
                }).onErrorResume(e -> Mono.just(FileUploadResponse.newBuilder().setStatus(FileUploadStatus.ERROR).build()))
                .thenReturn(FileUploadResponse.newBuilder()
                        .setStatus(FileUploadStatus.OK)
                        .build());

    }
}
