package com.maoshen.component.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.update.IDisconfUpdate;

@Configuration
@DisconfFile(filename = "mysql.properties")
//@DisconfUpdateService(classes = {MysqlDisconf.class})
public class MysqlDisconf implements IDisconfUpdate {
	
	private String mysqlMasterIp;
	private String mysqlMasterPort;
	private String mysqlMasterUsername;
	private String mysqlMasterPassword;

	private String mysqlSlaveIp;
	private String mysqlSlavePort;
	private String mysqlSlaveUsername;
	private String mysqlSlavePassword;


	@DisconfFileItem(name = "mysql.master.ip", associateField = "mysqlMasterIp")
	public String getMysqlMasterIp() {
		return mysqlMasterIp;
	}

	public void setMysqlMasterIp(String mysqlMasterIp) {
		this.mysqlMasterIp = mysqlMasterIp;
	}

	@DisconfFileItem(name = "mysql.master.port", associateField = "mysqlMasterPort")
	public String getMysqlMasterPort() {
		return mysqlMasterPort;
	}

	public void setMysqlMasterPort(String mysqlMasterPort) {
		this.mysqlMasterPort = mysqlMasterPort;
	}

	@DisconfFileItem(name = "mysql.master.username", associateField = "mysqlMasterUsername")
	public String getMysqlMasterUsername() {
		return mysqlMasterUsername;
	}

	public void setMysqlMasterUsername(String mysqlMasterUsername) {
		this.mysqlMasterUsername = mysqlMasterUsername;
	}

	@DisconfFileItem(name = "mysql.master.password", associateField = "mysqlMasterPassword")
	public String getMysqlMasterPassword() {
		return mysqlMasterPassword;
	}

	public void setMysqlMasterPassword(String mysqlMasterPassword) {
		this.mysqlMasterPassword = mysqlMasterPassword;
	}

	@DisconfFileItem(name = "mysql.slave.ip", associateField = "mysqlSlaveIp")
	public String getMysqlSlaveIp() {
		return mysqlSlaveIp;
	}

	public void setMysqlSlaveIp(String mysqlSlaveIp) {
		this.mysqlSlaveIp = mysqlSlaveIp;
	}

	@DisconfFileItem(name = "mysql.slave.port", associateField = "mysqlSlavePort")
	public String getMysqlSlavePort() {
		return mysqlSlavePort;
	}

	public void setMysqlSlavePort(String mysqlSlavePort) {
		this.mysqlSlavePort = mysqlSlavePort;
	}

	@DisconfFileItem(name = "mysql.slave.username", associateField = "mysqlSlaveUsername")
	public String getMysqlSlaveUsername() {
		return mysqlSlaveUsername;
	}

	public void setMysqlSlaveUsername(String mysqlSlaveUsername) {
		this.mysqlSlaveUsername = mysqlSlaveUsername;
	}

	@DisconfFileItem(name = "mysql.slave.password", associateField = "mysqlSlavePassword")
	public String getMysqlSlavePassword() {
		return mysqlSlavePassword;
	}

	public void setMysqlSlavePassword(String mysqlSlavePassword) {
		this.mysqlSlavePassword = mysqlSlavePassword;
	}

	@Override
	public void reload() throws Exception {

	}

}
