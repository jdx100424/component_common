package com.maoshen.component.mybatis.route.regular;

/**
 * 分表路由接口
 * @author daxian.jianglifesense.com
 *
 */
public abstract class MybatisRoute {
	private static final long IS_FIRST = 0;

	/**
	 * 根据路由列的属性是INT来分，获取路由表号
	 * @param id
	 * @param routeTableCount
	 * @return
	 * @throws Exception
	 */
	public abstract long getRouteNumberIntTable(Integer id,Integer routeTableCount) throws Exception;
	/**
	 * 根据路由列的属性是Long来分，获取路由表号
	 * @param id
	 * @param routeTableCount
	 * @return
	 * @throws Exception
	 */
	public abstract long getRouteNumberLongTable(Long id,Integer routeTableCount) throws Exception;
	/**
	 * 根据路由列的属性是String来分，获取路由表号
	 * @param id
	 * @param routeTableCount
	 * @return
	 * @throws Exception
	 */
	public abstract long getRouteNumberStringTable(String id,Integer routeTableCount) throws Exception;
	
	/**
	 * 判断是否为第一个表（原来的名字）
	 * @param route
	 * @return
	 */
	public boolean isFirstTable(long route,Integer routeTableCount){
		long result = route % routeTableCount;
		if(result == IS_FIRST){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取unionSql
	 * @param sql
	 * @param tableName
	 * @return
	 */
	public String getUnionSql(String sql,String tableName,Integer routeTableCount){
		StringBuilder newSql = new StringBuilder();
		newSql.append("(").append(sql).append(")");
		for(int i=0;i<routeTableCount;i++){
			//第一条默认是第一个表
			if(i!=IS_FIRST){
				newSql.append(" union all (");
				String unionSql = sql.replaceAll(tableName, tableName+i);
				newSql.append(unionSql).append(")");
			}
		}
		return newSql.toString();
	}
}
