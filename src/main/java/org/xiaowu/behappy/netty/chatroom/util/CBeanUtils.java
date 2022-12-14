package org.xiaowu.behappy.netty.chatroom.util;

import lombok.experimental.UtilityClass;
import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author xiaowu
 */
@UtilityClass
public class CBeanUtils {

    @SuppressWarnings("unchecked")
    private <T> Map<String, Object> beanToMap(T bean, boolean ignoreNullValue) {
        BeanMap beanMap = BeanMap.create(bean);
        Map<String, Object> map = new HashMap<>(beanMap.size());
        beanMap.forEach((key, value) -> {
            if (ignoreNullValue && Objects.nonNull(value)) {
                map.put(String.valueOf(key), value);
            }
            if (!ignoreNullValue) {
                map.put(String.valueOf(key), value);
            }
        });
        return map;
    }

    /**
     * 忽略空值
     * @param bean
     * @return
     * @param <T>
     */
    public <T> Map<String, Object> beanToMapIgnoreNullValue(T bean) {
        return beanToMap(bean, true);
    }

    /**
     * 不忽略空值
     * @param bean
     * @return
     * @param <T>
     */
    public <T> Map<String, Object> beanToMapNotIgnoreNullValue(T bean) {
        return beanToMap(bean, false);
    }

}
