package com.poc.springbootserverless.app;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;

public class LambdaHandler implements RequestStreamHandler {
    private static final SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            //
            handler = SpringLambdaContainerHandler.getAwsProxyHandler(AppApplication.class);

            // Estrategia utilizando Async initialization (Spring Boot é lento para iniciar devido aos carregamento dos beans)
//            long startTime = Instant.now().toEpochMilli();
//            handler = new SpringProxyHandlerBuilder<AwsProxyRequest>()
//                    .defaultProxy()
//                    .asyncInit(startTime)
//                    .configurationClasses(AppApplication.class)
//                    .buildAndInitialize();
        } catch (ContainerInitializationException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot Application", e);
        }
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        handler.proxyStream(input, output, context);

        output.close();
    }
}
