package com.maoshen.component.mybatis.route;

public class MybatisRouteDataSourceHolder {
	public static final ThreadLocal<MybatisRouteInfo> holder = new ThreadLocal<MybatisRouteInfo>();

    public static String getDataSource(){
	    	return null;
	}
    
    public static MybatisRouteInfo get() {
        return holder.get();
    }

    public static void put(MybatisRouteInfo mybatisReplicationInfo) {
        holder.set(mybatisReplicationInfo);
    }
}
