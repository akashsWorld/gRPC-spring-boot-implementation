package com.cromxt.clientservice;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(value = "/client-stream")
    public Mono<String> clientStreaming(@RequestPart(value = "file") FilePart file){
        return grpcClient.streamingCall(file);
    }
}
