package com.maoshen.component.mybatis;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MybatisReplicationDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return MybatisReplicationDataSourceHolder.getDataSource();
    }

}
