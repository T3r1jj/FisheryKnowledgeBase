package io.gitlab.druzyna_a.knowledgebase;

/**
 *
 * @author Damian Terlecki
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import io.gitlab.druzyna_a.knowledgebase.rest.FishRestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackageClasses = {
    RedirectController.class,
    FishRestController.class
})
public class Application {

    @Value("${build.version}")
    private String buildVersion;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(getPaths())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(getApiInfo())
                .pathMapping("/");
    }

    /**
     * Pretty print and other serialization features
     */
    @Component
    @Primary
    public class CustomObjectMapper extends ObjectMapper {

        public CustomObjectMapper() {
            setSerializationInclusion(JsonInclude.Include.NON_NULL);
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            enable(SerializationFeature.INDENT_OUTPUT);
        }
    }

    private Predicate<String> getPaths() {
        return Predicates.and(Predicates.not(PathSelectors.regex("^/$")), Predicates.not(PathSelectors.regex("/error")));
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("RSI fishery project 3rd module API (KnowledgeBase)")
                .description("KnowledgeBase API allows to fetch information about products and services offered by RSI project from external sources (i.e. websites). This project is non-commercial, made in learning purposes.")
                .contact(new Contact("Damian Terlecki, T3r1jj@github", "https://gitlab.com/Druzyna-A/KnowledgeBase", "terleckidamian1@gmail.com"))
                .version(buildVersion)
                .build();
    }

}
