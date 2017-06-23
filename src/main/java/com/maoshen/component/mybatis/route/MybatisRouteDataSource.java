package com.maoshen.component.mybatis.route;

public class MybatisRouteDataSource extends MybatisAbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return MybatisRouteDataSourceHolder.getDataSource();
    }
}
