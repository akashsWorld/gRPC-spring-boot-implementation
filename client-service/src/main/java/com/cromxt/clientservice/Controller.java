package com.cromxt.clientservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/grpc-example")
public record Controller (
    GrpcClient grpcClient
){

    @GetMapping(value = "/unary")
    public Mono<String> unaryOperation(){
        return grpcClient.makeUnaryCall();
    }
}
