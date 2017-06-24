package com.maoshen.component.mybatis;

public class MybatisReplicationDataSource extends MybatisAbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return MybatisReplicationDataSourceHolder.getDataSourceKeyName();
    }

}
