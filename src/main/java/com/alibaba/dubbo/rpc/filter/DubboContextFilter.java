package com.alibaba.dubbo.rpc.filter;

import java.util.Map;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.rest.UserRestContext;
import com.maoshen.component.rpc.filter.constant.DubboContextFilterConstant;

public class DubboContextFilter implements Filter{

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		//add userRestContext param
		Map<String,String> map = RpcContext.getContext().getAttachments();
		UserRestContext targetUserRestContext = JSONObject.parseObject(map.get(DubboContextFilterConstant.RPC_USER_REST_CONTEXT),UserRestContext.class);
		UserRestContext.get().setAccessToken(targetUserRestContext.getAccessToken());
		UserRestContext.get().setRequestId(targetUserRestContext.getRequestId());
		if(invocation instanceof RpcInvocation){
			((RpcInvocation)invocation).setInvoker(invoker);
		}
		return invoker.invoke(invocation);
	}

}
