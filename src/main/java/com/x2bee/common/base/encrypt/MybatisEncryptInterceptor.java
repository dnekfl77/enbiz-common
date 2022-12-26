/**
 * 
 */
package com.x2bee.common.base.encrypt;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import lombok.extern.slf4j.Slf4j;

/**
 * @author choiyh44
 * @version 1.0
 * @since 2021. 12. 6.
 *
 */
@Slf4j
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
    @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class })
})
public class MybatisEncryptInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String method = invocation.getMethod().getName();
        
        if ("update".equals(method)) {
            return processUpdate(invocation);
        }
        else if ("handleResultSets".equals(method)) {
            return processQuery(invocation);
        }
        else {
            return invocation.proceed();
        }
    }

    private Object processUpdate(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        Object[] args = invocation.getArgs();
        Object param = args[1];
        if (param != null) {
            Field[] fields = param.getClass().getDeclaredFields();
            for (Field field : fields) {
                Encrypt annotation = field.getAnnotation(Encrypt.class);
                if(annotation!=null && field.getType() == String.class) {
                    field.setAccessible(true);
                    try {
                        String val = EncryptUtils.getEncryptValue(field.get(param)+"", annotation.type());
                        log.info("EncryptValue: {}: {}", val.length(), val);
                        field.set(param, val);
                    }
                    catch (Exception e) {
                        log.warn(e.getMessage(), e);
                    }
                }
            }
        }
        return invocation.proceed();
    }

    private Object processQuery(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        Object result = invocation.proceed();
        if (Objects.isNull(result)){
            return null;
        }

        if (result instanceof ArrayList) {
            ArrayList<?> resultList = (ArrayList<?>) result;

            for (int i = 0; i < resultList.size(); i++) {
                Field[] fields = resultList.get(i).getClass().getDeclaredFields();
                for (Field field : fields) {
                    Encrypt annotation = field.getAnnotation(Encrypt.class);

                    if(annotation!=null && field.getType() == String.class) {
                        field.setAccessible(true);
                        try {
                            String val = EncryptUtils.getDecryptValue(field.get(resultList.get(i))+"", annotation.type());
                            field.set(resultList.get(i), val);
                        }
                        catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }else {
            Field[] fields = result.getClass().getDeclaredFields();
            for (Field field : fields) {
                Encrypt annotation = field.getAnnotation(Encrypt.class);

                if(annotation!=null && field.getType() == String.class) {
                    field.setAccessible(true);
                    try {
                        String val = EncryptUtils.getDecryptValue(field.get(result)+"", annotation.type());
                        field.set(result, val);
                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

        }
        return result;
    }

}
