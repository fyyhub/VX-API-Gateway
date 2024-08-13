package com.szmirren.vxApi.core.handler.route.impl;

import com.szmirren.vxApi.core.entity.VxApis;
import com.szmirren.vxApi.core.handler.route.VxApiRouteHandlerRedirectType;
import com.szmirren.vxApi.core.options.VxApiParamOptions;
import com.szmirren.vxApi.core.options.VxApiServerEntranceRedirectOptions;
import com.szmirren.vxApi.spi.handler.VxApiAfterHandler;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.*;

/**
 * VxApiRoute页面跳转处理器实现类
 * 
 * @author <a href="http://szmirren.com">Mirren</a>
 *
 */
public class VxApiRouteHandlerRedirectTypeImpl implements VxApiRouteHandlerRedirectType {

	private boolean isNext;
	private VxApiServerEntranceRedirectOptions redirectOptions;
	private VxApis apis;

	private Integer pos = 0;
	private List<String> iplist = new ArrayList<>();

	public VxApiRouteHandlerRedirectTypeImpl(boolean isNext, VxApis api) {
		super();
		this.isNext = isNext;
		this.apis = api;
		JsonObject body = api.getServerEntrance().getBody();
		redirectOptions = VxApiServerEntranceRedirectOptions.fromJson(body);
		if (redirectOptions == null) {
			throw new NullPointerException("页面跳转服务类型的配置文件无法装换为服务类");
		}
		if(!redirectOptions.getUrl().contains(",")){
			iplist.add(redirectOptions.getUrl());
		}else {
			String [] urls = redirectOptions.getUrl().split(",");
			iplist.addAll(Arrays.asList(urls));
		}
	}

	@Override
	public void handle(RoutingContext rct) {
		String redUrl = geturl();
		if(apis.isPassBody()){
			if(redirectOptions.getVxApiParamOptionsList() != null && !redirectOptions.getVxApiParamOptionsList().isEmpty()){
				Map<String,String> params = new HashMap<>();
				for(VxApiParamOptions param : redirectOptions.getVxApiParamOptionsList()){
					params.put(param.getApiParamName(), rct.request().getParam(param.getApiParamName()));
				}
				redUrl = redUrl + "?" + VxApiParamOptions.parseParams(params);
			}
		}
		rct.response().putHeader("Location", redUrl).setStatusCode(302);
		if (isNext) {
			rct.put(VxApiAfterHandler.PREV_IS_SUCCESS_KEY, Future.<Boolean>succeededFuture(true));// 告诉后置处理器当前操作成功执行
			rct.next();
		} else {
			if (!rct.response().ended()) {
				rct.response().end();
			}
		}

	}

	private String geturl() {
		String url =null;
		synchronized (pos){
			if (pos >= iplist.size()){
				pos = 0;
			}
			url = iplist.get(pos);
			pos ++;
		}
		return url;
	}

}
