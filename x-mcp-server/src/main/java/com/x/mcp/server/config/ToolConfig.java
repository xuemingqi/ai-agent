package com.x.mcp.server.config;

import cn.hutool.core.util.ClassUtil;
import com.x.mcp.server.XMcpServerApplication;
import com.x.mcp.server.annotation.ToolService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @author : xuemingqi
 * @since : 2025/03/26 17:26
 */
@Configuration
public class ToolConfig {

    @Bean
    public ToolCallbackProvider userTool(@Autowired ApplicationContext applicationContext) {
        Set<Class<?>> scans = new HashSet<>(
                ClassUtil.scanPackageByAnnotation(XMcpServerApplication.class.getPackageName(), ToolService.class)
        );
        return MethodToolCallbackProvider.builder()
                .toolObjects(scans.stream()
                        .map(applicationContext::getBean).toArray())
                .build();
    }
}
