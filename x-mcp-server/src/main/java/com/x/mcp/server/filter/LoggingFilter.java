package com.x.mcp.server.filter;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author : xuemingqi
 * @since : 2025/03/26 14:39
 */
@Component
public class LoggingFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest originalRequest = exchange.getRequest();

        // 记录 URL、方法、Headers、Query Params
        log.info("=== Request Start ===");
        log.info("URL: {}", originalRequest.getURI());
        log.info("Method: {}", originalRequest.getMethod());
        log.info("Headers: {}", originalRequest.getHeaders());
        log.info("Query Params: {}", originalRequest.getQueryParams());

        // 对请求体进行拦截，但不消费原始数据
        Flux<DataBuffer> loggedBody = originalRequest.getBody()
                .doOnNext(dataBuffer -> {
                    // 使用只读 ByteBuffer 副本记录数据，而不消费原数据
                    ByteBuffer byteBuffer = dataBuffer.asByteBuffer();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String chunk = new String(bytes, StandardCharsets.UTF_8);
                    log.info("Body chunk: {}", chunk);
                });

        // 使用 ServerHttpRequestDecorator 将处理后的 Flux<DataBuffer> 替换原请求体
        ServerHttpRequest decoratedRequest = new ServerHttpRequestDecorator(originalRequest) {
            @Override
            public Flux<DataBuffer> getBody() {
                return loggedBody;
            }
        };

        // 包装响应，拦截响应数据写出
        ServerHttpResponseDecorator decoratedResponse = getServerHttpResponseDecorator(exchange);

        // 使用 mutate 构造新的 exchange 对象，同时替换请求和响应对象
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(decoratedRequest)
                .response(decoratedResponse)
                .build();

        // 继续过滤链，并在请求结束后记录日志
        return chain.filter(mutatedExchange)
                .doOnTerminate(() -> log.info("=== Request End ==="));
    }

    private static ServerHttpResponseDecorator getServerHttpResponseDecorator(ServerWebExchange exchange) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            // 处理 writeAndFlushWith 写出（例如 SSE 流）
            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                Flux<DataBuffer> flattened = Flux.from(body)
                        .flatMapSequential(p -> ((Flux<DataBuffer>) p)
                                .doOnNext(dataBuffer -> {
                                    ByteBuffer byteBuffer = dataBuffer.asByteBuffer().duplicate();
                                    byte[] bytes = new byte[byteBuffer.remaining()];
                                    byteBuffer.get(bytes);
                                    String chunk = new String(bytes, StandardCharsets.UTF_8);
                                    log.info("Response Body chunk: {}", chunk);
                                }));
                return super.writeWith(flattened);
            }
        };
        return decoratedResponse;
    }
}
