package io.gitlab.druzyna_a.fisheryknowledgebase;

/**
 *
 * @author Damian Terlecki
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.gitlab.druzyna_a.fisheryknowledgebase.crawling.CrawlerController;
import io.gitlab.druzyna_a.fisheryknowledgebase.db.ArticlesRepositoryCleaner;
import io.gitlab.druzyna_a.fisheryknowledgebase.rest.offered.FishRestController;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
@EnableAsync
@ComponentScan(basePackageClasses = {
    RedirectController.class,
    FishRestController.class
})
public class Application extends AsyncConfigurerSupport{

    @Value("${build.version}")
    private String buildVersion;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        ctx.getBean(ArticlesRepositoryCleaner.class).run();
        ctx.getBean(CrawlerController.class).run();
    }
    
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("FisheryKnowledgeBase-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(getClass().getPackage().getName() + ".rest.offered"))
                .paths(PathSelectors.any())
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

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("FisheryKnowledgeBase API - The Fishery project 3rd module")
                .description("KnowledgeBase API allows to fetch information about products and services offered by the Fishery project from external sources (i.e. websites). This project is non-commercial, created in learning purposes.")
                .contact(new Contact("Damian Terlecki, T3r1jj@github", "https://github.com/T3r1jj/FisheryKnowledgeBase", "t3r1jj@gmail.com"))
                .version(buildVersion)
                .license("Used components (licenses)")
                .licenseUrl("/license.html")
                .build();
    }

}
