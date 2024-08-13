package com.szmirren.vxApi.core.options;

import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Api服务端处理类型为Redirect
 * 
 * @author <a href="http://szmirren.com">Mirren</a>
 *
 */
public class VxApiServerEntranceRedirectOptions {

	private String url;// 将要跳转的URL路径
	private List<VxApiParamOptions> vxApiParamOptionsList;

	/**
	 * 将对象装换为JsonObject
	 * 
	 * @return
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.put("url", this.url);
		return json;
	}

	/**
	 * 通过配置文件创建一个对象
	 * 
	 * @param obj
	 * @return
	 */
	public static VxApiServerEntranceRedirectOptions fromJson(JsonObject obj) {
		if (obj == null || obj.getString("url") == null) {
			return null;
		}
		VxApiServerEntranceRedirectOptions options = new VxApiServerEntranceRedirectOptions();
		options.setUrl(obj.getString("url"));

		List<VxApiParamOptions> optionsList = new ArrayList<>();
		if (obj.getJsonArray("params") != null) {
			for (int i = 0; i < obj.getJsonArray("params").size(); i++) {
				optionsList.add(VxApiParamOptions.fromJson(obj.getJsonArray("params").getJsonObject(i)));
			}
		}
		options.setVxApiParamOptionsList(optionsList);
		return options;
	}

	private VxApiServerEntranceRedirectOptions() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<VxApiParamOptions> getVxApiParamOptionsList() {
		return vxApiParamOptionsList;
	}

	public void setVxApiParamOptionsList(List<VxApiParamOptions> vxApiParamOptionsList) {
		this.vxApiParamOptionsList = vxApiParamOptionsList;
	}
}
