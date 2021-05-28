package com.study.json;


import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 此modifier主要做的事情为：
 * 1.当序列化类型为数组集合时，当值为null时，序列化成[]
 * 2.String类型值序列化为""
 * @author wanxp
 * @version 1.0
 * @className
 * @date 6/12/20 2:44 PM
 * </pre>
 */
public class CustomBeanSerializerModifier extends BeanSerializerModifier {

    /**
     * 只生效 null array
     * @author wanxp
     * @date 2:50 PM 6/12/20
     * @param
     * @return
     **/
    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = beanProperties.get(i);
            // 判断字段的类型，如果是数组或集合则注册nullArraySerializer
            if (isArrayType(writer)) {
                // 给writer注册一个自己的nullSerializer
                writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullArrayJsonSerializer());
            }
            if (isMapType(writer)) {
                // 给writer注册一个自己的nullMapSerializer
                writer.assignNullSerializer(new CustomizeNullJsonSerializer.NulMapJsonSerializer());
            }
            if (isStringType(writer)) {
                writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullStringJsonSerializer());
            }
//            if (isLongType(writer)) {
//                // 给writer注册一个自己的 ToStringSerializer
//                writer.assignSerializer(ToStringSerializer.instance);
//            }
        }
        return beanProperties;
    }

    /**
     * 是否是数组
     */
    private boolean isArrayType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是String
     */
    private boolean isMapType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return Map.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是String
     */
    private boolean isStringType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是数值类型
     */
    private boolean isNumberType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return Number.class.isAssignableFrom(clazz);
    }

    /**
     * 是否是 Long 类型
     */
    private boolean isLongType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return Long.class.isAssignableFrom(clazz);
    }


    /**
     * 是否是boolean
     */
    private boolean isBooleanType(BeanPropertyWriter writer) {
        Class<?> clazz = writer.getType().getRawClass();
        return clazz.equals(Boolean.class);
    }



}
