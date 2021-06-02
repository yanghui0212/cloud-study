package com.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @author: yangqh
 * @create: 2021/6/2 15:30
 **/
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    /**
     * 通过createRestApi函数创建Docket的Bean之后，
     * apiInfo()用来创建该Api的基本信息（这些基本信息会展现在文档页面中）。
     * select()函数返回一个ApiSelectorBuilder实例用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义，
     * Swagger会扫描该包下所有Controller定义的API，
     * 并产生文档内容（除了被@ApiIgnore指定的请求）。
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {
        //添加head参数start
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        //header中的ticket参数非必填，传空也可以
        //根据每个方法名也知道当前方法在设置什么参数
        ticketPar.name("Authorization").description("用户登陆token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        pars.add(ticketPar.build());
        //添加head参数end

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.study.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    /**
     * 用来创建该Api的基本信息（这些基本信息会展现在文档页面中）
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RESTful-APIs")
                .description("项目api文档,POST添加,GET获取,PUT修改,DELETE删除")
                .contact(new Contact("yqh", "", ""))
                .version("1.0")
                .build();
    }
}
