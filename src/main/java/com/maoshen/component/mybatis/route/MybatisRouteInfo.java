package com.maoshen.component.mybatis.route;

public class MybatisRouteInfo {
	//要根据rouId区分要对哪个表／库操作
	private String routeId;
	private String dataSourceName;
	
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
}
