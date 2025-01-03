package com.cromxt.clientservice;


import com.cromxt.grpc.service.example.HelloReply;
import com.cromxt.grpc.service.example.HelloRequest;
import com.cromxt.grpc.service.example.ReactorHelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GrpcClient {

    private final ReactorHelloServiceGrpc.ReactorHelloServiceStub reactorHelloServiceStub;

    public GrpcClient() {
        ManagedChannel managedChannel =
                ManagedChannelBuilder.forAddress("localhost", 9090)
                        .usePlaintext()
                        .build();
        this.reactorHelloServiceStub = ReactorHelloServiceGrpc.newReactorStub(managedChannel);
    }


    public Mono<String> makeUnaryCall() {

        Mono<HelloRequest> request = Mono.just(HelloRequest.newBuilder().setName("World").build());
        return request.transform(reactorHelloServiceStub::hello)
                .map(HelloReply::getMessage);
    }
}
