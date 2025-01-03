package com.cromxt.clientservice;


import com.cromxt.grpc.service.example.*;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GrpcClient {

    private final ReactorHelloServiceGrpc.ReactorHelloServiceStub reactorHelloServiceStub;

    private final ReactorFileUploadServiceGrpc.ReactorFileUploadServiceStub reactorFileUploadServiceStub;
    public GrpcClient() {
        ManagedChannel managedChannel =
                ManagedChannelBuilder.forAddress("localhost", 9090)
                        .usePlaintext()
                        .build();
        this.reactorHelloServiceStub = ReactorHelloServiceGrpc.newReactorStub(managedChannel);
        this.reactorFileUploadServiceStub = ReactorFileUploadServiceGrpc.newReactorStub(managedChannel);
    }


    public Mono<String> makeUnaryCall() {

        Mono<HelloRequest> request = Mono.just(HelloRequest.newBuilder().setName("World").build());
        return request.transform(reactorHelloServiceStub::hello)
                .map(HelloReply::getMessage);
    }

    public Mono<String> streamingCall(FilePart file) {
        Flux<FileUploadRequest> data = file.content()
                .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            return FileUploadRequest.newBuilder().setFile(ByteString.copyFrom(bytes)).build();
                        }
                );

        return data.as(reactorFileUploadServiceStub::uploadFile)
                .map(fileUploadResponse -> {
                    if (fileUploadResponse.getStatus() == FileUploadStatus.OK) {
                        return "File uploaded successfully";
                    } else {
                        return "File upload failed";
                    }
                });
    }
}
