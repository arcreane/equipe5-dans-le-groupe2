package com.hogeon.Http.Controllers;

import com.hogeon.Http.CommandHandler;
import com.hogeon.Http.HttpCommandHandler;
import com.hogeon.Http.HttpCommandParams;
import com.hogeon.Http.HttpCommandResponse;

@CommandHandler(cmd="/TestControl")
public class HttpTestControl extends HttpCommandHandler{
	public HttpCommandResponse action(HttpCommandParams httpParams) {
		//System.out.println(httpParams.getParams());
		HttpCommandResponse success = HttpCommandResponse.valueOfSucc();
		success.setMessage("TestControl return Hogeon");
		return success;
	}
}
