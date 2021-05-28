package com.study.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.study.json.CustomBeanSerializerModifier;
import com.study.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;


@ConditionalOnClass({MessageSource.class, LocaleResolver.class, CorsFilter.class, HttpMessageConverter.class})
@Configuration
@Slf4j
public class JSONWebMvcConfigurer implements WebMvcConfigurer {


    @Autowired
    private StringToDateConverter stringToDateConverter;


    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        // 允许cookies跨域
        config.setAllowCredentials(true);
        // 允许向该服务器提交请求的URI，*表示全部允许。。这里尽量限制来源域，比如http://xxxx:8080
        // ,以降低安全风险。。
        config.addAllowedOrigin("*");
        // 允许访问的头信息,*表示全部
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);
        // 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
        config.addAllowedMethod("*");

        config.addExposedHeader("Accept-Ranges");
        config.addExposedHeader("Content-Range");
        config.addExposedHeader("Content-Encoding");
        config.addExposedHeader("Content-Length");
        config.addExposedHeader("Authorization");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * 禁用内容协商，总是header返回application/json;charset=utf8
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // 不检查Accept请求头, 只返回application/json 类型的内容
        configurer.ignoreAcceptHeader(true).defaultContentType(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.APPLICATION_FORM_URLENCODED);
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToDateConverter);
    }


    @Bean
    public HttpMessageConverter getMessageConverter(ObjectMapper objectMapper) {

        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        objectMapper.deactivateDefaultTyping();
        objectMapper.setDateFormat(new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS1));
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new CustomBeanSerializerModifier()));
        /**
         * 序列换成json时,将所有的long变成string
         * 因为js中得数字类型不能包含所有的java long值
         */
        SimpleModule simpleModule = new SimpleModule();
        // 序列换成json时,将所有的long变成string 因为js中得数字类型不能包含所有的java long值
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
                return stringToDateConverter.convert(p.getValueAsString());
            }
        });
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);

        return jackson2HttpMessageConverter;
    }


    /**
     * 字符串到日期的转换
     */
    @Component
    public class StringToDateConverter implements Converter<String, Date> {

        private Date stringToDate(String source, String patten) {
            return DateTime.parse(source, DateTimeFormat.forPattern(patten)).toDate();
        }

        @Override
        public Date convert(String source) {
            if (StringUtils.isNotBlank(source)) {
                try {
                    if (StringUtils.isNumeric(source)) {
                        return new Date(Long.parseLong(source));
                    } else if (source.contains("T")) {
                        //标准utc时间
                        DateTime dateTime = DateTime.parse(source, DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
                        return dateTime.toDate();
                    } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                        return stringToDate(source, "yyyy-MM-dd HH:mm:ss");
                    } else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")) {
                        return stringToDate(source, "yyyy/MM/dd");
                    } else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}/\\d{1,2}/\\d{1,2}$")) {
                        return stringToDate(source, "yyyy/MM/dd HH/mm/ss");
                    } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                        return stringToDate(source, "yyyy-MM-dd");
                    } else if (source.matches("(Mon|Tue|Wed|Thu|Fri|Sat|Sun)\\s(Jan|Feb|Mar|" +
                            "Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s(\\d{2})\\s(0\\d|2[0-3]):([0-5]\\d):([0-5]\\d)\\sCST\\s(\\d{4})")) {
                        return new Date(source);
                    }
                } catch (IllegalArgumentException e) {
                    log.error(e.getMessage(), e);
                }
            }
            return null;
        }
    }


}
