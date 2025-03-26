package com.x.mcp.server.service.impl;

import com.x.mcp.server.db.entity.User;
import com.x.mcp.server.db.service.UserIService;
import com.x.mcp.server.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : xuemingqi
 * @since : 2025/03/26 16:55
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserIService userIService;


    @Override
    @Tool(name = "getUserList", description = "Get user list")
    public List<User> getUserList() {
        return userIService.list();
    }
}
