package com.maoshen.component.mybatis.route;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalyzeActualSql {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeActualSql.class);
	private static final String SHARD_MOD = "%";
	private static final String SHARD_DATE_DAY = "date";
	MappedStatement mappedStatement;
	Object parameterObject;
	BoundSql boundSql;

	public AnalyzeActualSql(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
		super();
		this.mappedStatement = mappedStatement;
		this.parameterObject = parameterObject;
		this.boundSql = boundSql;
	}

	public String getActualSql(String originalSql, TableSeg tableSeg) {
		String newSql = originalSql;
		if (tableSeg == null)
			return newSql;
		String tableName = tableSeg != null ? tableSeg.tableName().trim() : "";
		String shardType = tableSeg != null ? tableSeg.shardType().trim() : "";
		String shardBy = tableSeg != null ? tableSeg.shardBy().trim() : "";
		String suffix = null;
		//AnalyzeShardSuffix suffixLogic;
		if (shardType != null && shardType.startsWith(SHARD_MOD)) {// 取模
			//suffixLogic = new AnalyzeShardSuffixByModImpl(mappedStatement, parameterObject, boundSql);
			//suffix = suffixLogic.getShardValue(shardType, shardBy);
		} else if (shardType != null && shardType.startsWith(SHARD_DATE_DAY)) {// 按日期分表
			// 待扩展
		} else {
			// 待扩展
		}
		if (suffix == null || suffix.equals("")) {
			return newSql;
		} else {
			// 替换新表名
			String newTableName = tableName + "_" + suffix;
			// 表名尽量保持唯一，不要与字段重名
			newSql = originalSql.replaceAll(tableName, newTableName);
		}
		return newSql;
	}
}
