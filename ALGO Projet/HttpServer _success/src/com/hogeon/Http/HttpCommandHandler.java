package com.hogeon.Http;

/**
 * 抽象后台命令处理者
 */
public abstract class HttpCommandHandler {
	
	/**
	 * 处理后台命令
	 * @param httpParams
	 * @return
	 */
	public abstract HttpCommandResponse action(HttpCommandParams httpParams);

}
