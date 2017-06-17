package com.maoshen.component.mybatis;

public class MybatisReplicationInfo {
	private boolean master;
	private boolean slave;
	private String dataSourceName;
	
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public boolean isMaster() {
		return master;
	}
	public void setMaster(boolean master) {
		this.master = master;
	}
	public boolean isSlave() {
		return slave;
	}
	public void setSlave(boolean slave) {
		this.slave = slave;
	}
}
