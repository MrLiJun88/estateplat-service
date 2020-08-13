package cn.gtmap.estateplat.server.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2019/8/19
 * @description
 */

@Configuration        //让Spring来加载该类配置
@EnableWebMvc        //启用Mvc，非springboot框架需要引入注解@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {
//    @Bean
//    public Docket api() {
//        System.out.println("----XIN----2018/7/11 下午12:58 Line:22,当前类=SwaggerConfig.api()");
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                /***
//                 重要的两个方法:
//                 apis():指定要生成文档的接口包基本路径
//                 paths():指定针对哪些请求生成接口文档
//                 参考官方资料：http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
//                 ****/
//                //.apis(RequestHandlerSelectors.any())
//                .apis(RequestHandlerSelectors.basePackage("cn.gtmap.estateplat.currency.web.rest"))
//                .paths(PathSelectors.ant("/rest/**"))
//                .build()
//                .apiInfo(apiInfo());
//    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("cn.gtmap.estateplat.server.web.rest")).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("不动产登记2.0").description(" 通用系统 RESTful API ").version("1.0").build();
    }
}
