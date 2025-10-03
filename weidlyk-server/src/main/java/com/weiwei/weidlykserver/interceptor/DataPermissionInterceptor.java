package com.weiwei.weidlykserver.interceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.weiwei.weidlykserver.aspect.DataScopeAspect;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.SQLException;

/**
 * 数据权限拦截器
 * 是执行者，负责将 Aspect 生成的 SQL 附加到原始 SQL 上
 */
@Component
public class DataPermissionInterceptor implements InnerInterceptor { // ✅ 1. 实现 InnerInterceptor 接口

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        // 从 ThreadLocal 中获取数据权限 SQL
        String dataScopeSql = DataScopeAspect.getDataScopeSql();

        if (StringUtils.hasText(dataScopeSql)) {
            // 获取原始 SQL
            String originalSql = boundSql.getSql();
            // 拼接 SQL
            String newSql = originalSql + dataScopeSql;
            // 使用反射修改 SQL
            setSql(boundSql, newSql);
        }
    }

    /**
     * 使用反射设置 BoundSql 的 sql 字段值
     * @param boundSql BoundSql 对象
     * @param sql      新的 SQL 语句
     */
    private void setSql(BoundSql boundSql, String sql) {
        try {
            Field sqlField = BoundSql.class.getDeclaredField("sql");
            sqlField.setAccessible(true);
            sqlField.set(boundSql, sql);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // 在生产环境中，这里应该记录更详细的日志
            throw new RuntimeException("Failed to modify BoundSql.", e);
        }
    }
}