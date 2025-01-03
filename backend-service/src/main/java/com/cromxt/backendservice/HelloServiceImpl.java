package com.cromxt.backendservice;

import com.cromxt.grpc.service.example.HelloReply;
import com.cromxt.grpc.service.example.HelloRequest;
import com.cromxt.grpc.service.example.ReactorHelloServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;


@GrpcService
public class HelloServiceImpl extends ReactorHelloServiceGrpc.HelloServiceImplBase {
    @Override
    public Mono<HelloReply> hello(Mono<HelloRequest> request) {
        return request.map(helloRequest -> {
            System.out.println(helloRequest.getName());
            return HelloReply.newBuilder().setMessage("Hello " + helloRequest.getName()+ "Anisha").build();
        });
    }
}
