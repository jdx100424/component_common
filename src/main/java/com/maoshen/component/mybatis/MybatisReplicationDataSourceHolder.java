package com.maoshen.component.mybatis;

import org.apache.commons.lang3.StringUtils;

public class MybatisReplicationDataSourceHolder {
	public static final String MASTER = "master";
	public static final String SLAVE = "slave";
    public static final ThreadLocal<MybatisReplicationInfo> holder = new ThreadLocal<MybatisReplicationInfo>();

    public static String getDataSourceKeyName(){
	    	MybatisReplicationInfo m = holder.get();
	    	if(m!=null && StringUtils.isNotBlank(m.getDataSourceName())){
	    		if(MASTER.equals(m.getDataSourceName()) || SLAVE.equals(m.getDataSourceName())){
	        		return m.getDataSourceName();
	    		}
	    	}
	    	//return MybatisReplicationDataSourceHolder.MASTER;
	    	return null;
	}
    
    public static MybatisReplicationInfo getDataSource() {
        return holder.get();
    }

    public static void putDataSource(MybatisReplicationInfo mybatisReplicationInfo) {
        holder.set(mybatisReplicationInfo);
    }
}
