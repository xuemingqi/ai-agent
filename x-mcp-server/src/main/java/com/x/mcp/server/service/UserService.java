package com.x.mcp.server.service;

import com.x.mcp.server.db.entity.User;

import java.util.List;

/**
 * @author : xuemingqi
 * @since : 2025/03/26 16:49
 */
public interface UserService {

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    List<User> getUserList();
}
