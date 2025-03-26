/*
* Copyright 2024 - 2024 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.x.mcp.server;

import com.x.mcp.server.utils.JsonUtil;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author : xuemingqi
 * @since : 2025/3/26 16:48
 */
@Slf4j
public class SampleClient {

	private final McpClientTransport transport;


	public SampleClient(McpClientTransport transport) {
		this.transport = transport;
	}

	public void run() {
		var client = McpClient.sync(this.transport).build();

		// 初始化client
		client.initialize();

		// ping
		client.ping();

		// 获取可用的tool
		ListToolsResult toolsList = client.listTools();
		log.info("tool list: {}", JsonUtil.toJsonStr(toolsList));

		// 调用tool
		CallToolResult userTool = client.callTool(new CallToolRequest("getUserList", Map.of()));
		log.info("user list: {}", JsonUtil.toJsonStr(userTool));

		// 关闭client
		client.closeGracefully();

	}

}
