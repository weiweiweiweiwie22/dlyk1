package com.weiwei.weidlykserver.aspect;

import com.weiwei.weidlykserver.common.DataScope;
import com.weiwei.weidlykserver.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;
// ⬇️⬇️⬇️ 新增的 import 语句 ⬇️⬇️⬇️
import org.aspectj.lang.annotation.After;
// ⬆️⬆️⬆️ 新增的 import 语句 ⬆️⬆️⬆️
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import java.lang.reflect.Method;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 数据权限切面
 * 数据权限功能的大脑，负责判断和生成SQL
 */
@Aspect
@Component
public class DataScopeAspect {

    // 使用ThreadLocal来传递数据权限SQL，确保线程安全
    private static final ThreadLocal<String> DATA_SCOPE_SQL = new ThreadLocal<>();

    // 定义切点：拦截所有被 @DataScope 注解的方法
    @Pointcut("@annotation(com.weiwei.weidlykserver.common.DataScope)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 清除旧的SQL，以防万一
        clearDataScopeSql();

        // 1. 获取当前登录的用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        User loginUser = (User) authentication.getPrincipal();
        if (loginUser == null) {
            return;
        }

        // 2. 判断用户角色
        boolean isAdmin = loginUser.getAuthorities().stream()
                .anyMatch(authority -> "admin".equals(authority.getAuthority()));

        // 如果是管理员，则不进行任何数据过滤，直接返回
        if (isAdmin) {
            return;
        }

        // 3. 如果不是管理员，则拼接数据权限SQL
        DataScope dataScope = getDataScope(joinPoint);
        if (dataScope != null) {
            String tableAlias = dataScope.tableAlias();
            String tableField = dataScope.tableField();

            String aliasPrefix = tableAlias.isEmpty() ? "" : tableAlias + ".";

            String sql = " AND " + aliasPrefix + tableField + " = " + loginUser.getId();

            DATA_SCOPE_SQL.set(sql);
        }
    }

    // ⬇️⬇️⬇️ 新增的 doAfter 方法 ⬇️⬇️⬇️
    /**
     * 在方法执行之后，无论成功还是失败，都清理 ThreadLocal
     */
    @After("dataScopePointCut()")
    public void doAfter() {
        // 这个方法确保了在被注解的目标方法执行完毕后，ThreadLocal一定会被清理
        clearDataScopeSql();
    }
    // ⬆️⬆️⬆️ 新增的 doAfter 方法 ⬆️⬆️⬆️

    /**
     * 获取方法上的DataScope注解（健壮版本）
     */
    private DataScope getDataScope(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }

    // 辅助方法，用于通过反射获取方法
    private Method getMethod(Class<?> clazz, String name, Object[] args) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(name) && method.getParameterCount() == args.length) {
                return method;
            }
        }
        return null;
    }

    // 提供静态方法给拦截器获取SQL
    public static String getDataScopeSql() {
        return DATA_SCOPE_SQL.get();
    }

    // 提供静态方法给拦截器清除SQL
    public static void clearDataScopeSql() {
        DATA_SCOPE_SQL.remove();
    }
}