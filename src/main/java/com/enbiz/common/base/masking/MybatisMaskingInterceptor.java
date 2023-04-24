package com.enbiz.common.base.masking;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

/**
 * Result interceptor
*/
@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }))
public class MybatisMaskingInterceptor implements Interceptor {
	
	@Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (Objects.isNull(result)){
            return null;
        }

        if (result instanceof ArrayList) {
            ArrayList<?> resultList = (ArrayList<?>) result;

            for (int i = 0; i < resultList.size(); i++) {
            	Field[] fields = resultList.get(i).getClass().getDeclaredFields();
            	for (Field field : fields) {
            		MaskString annotation = field.getAnnotation(MaskString.class);

            		if(annotation!=null && field.getType() == String.class) {
            			try {
                			String val = MaskingUtils.getValue(BeanUtils.getProperty(resultList.get(i), field.getName())+"", annotation.type());
                			BeanUtils.setProperty(resultList.get(i), field.getName(), val);
            			}
                        catch (IllegalAccessException e) {
                        	System.out.println(e.getMessage());
                        }
            		}
            	}
            }
        }else {
        	Field[] fields = result.getClass().getDeclaredFields();
        	for (Field field : fields) {
        		MaskString annotation = field.getAnnotation(MaskString.class);

        		if(annotation!=null && field.getType() == String.class) {
        			try {
            			String val = MaskingUtils.getValue(BeanUtils.getProperty(result, field.getName())+"", annotation.type());
            			BeanUtils.setProperty(result, field.getName(), val);
        			}catch (IllegalAccessException e) {
                    	System.out.println(e.getMessage());
                    }
        		}
        	}

        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}