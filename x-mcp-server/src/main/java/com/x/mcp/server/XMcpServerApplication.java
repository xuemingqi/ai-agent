package com.x.mcp.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.x.mcp.server.db.mapper"})
public class XMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(XMcpServerApplication.class, args);
    }

}
