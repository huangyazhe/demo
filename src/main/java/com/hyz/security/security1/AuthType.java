/*
 * Copyright (c) 2013 www.jd.com All rights reserved.
 * 本软件源代码版权归京东成都云平台所有,未经许可不得任意复制与传播.
 */
package com.hyz.security.security1;

/**
 * 权限认证类型
 * @author cfish
 * @since 2013-09-09
 * @modifier yfdongxu
 * @since 2015-09-09
 */
public enum AuthType {
	
	/**
	 * 无需认证直接通过
	 */
	NONE,
	
	/**
	 * 公共资源,需登录
	 */
	PUBLIC,
	
	/**
	 * 用户权限码认证
	 */
	CODE,
	
	/**
	 * 请求需中包含指定Token值
	 */
	TOKEN,

	/**
	 * 对于外部调用接口，使用数据库中url相关的token配置进行权限校验
	 */
	RPC_TOKEN;
}
