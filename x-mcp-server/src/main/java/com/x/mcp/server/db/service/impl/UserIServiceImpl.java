package com.x.mcp.server.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.x.mcp.server.db.entity.User;
import com.x.mcp.server.db.mapper.UserMapper;
import com.x.mcp.server.db.service.UserIService;
import org.springframework.stereotype.Service;

/**
 * @author : xuemingqi
 * @since : 2022-10-19 15:54
 */
@Service
public class UserIServiceImpl extends ServiceImpl<UserMapper, User> implements UserIService {
}
