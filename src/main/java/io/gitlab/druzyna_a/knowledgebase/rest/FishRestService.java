package io.gitlab.druzyna_a.knowledgebase.rest;

import io.gitlab.druzyna_a.knowledgebase.model.Fish;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import io.swagger.util.Json;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Damian Terlecki
 */
@SwaggerDefinition(
        info = @Info(
                description = "This API provides information from external sources about RSI project products and services",
                version = "0.1.0-alpha",
                title = "KnowledgeBase API description",
                contact = @Contact(name = "Damian Terlecki", email = "terleckidamian1@gmail.com", url = "http://gitlab.io/Druzyna-A/KnowledgeBase")
        ),
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        tags = {
            @Tag(name = "fish", description = "Operations about fish")}
)
@Api(value = "/rest/fish", tags = {"fish"})
public class FishRestService extends HttpServlet {

    @ApiOperation(httpMethod = "GET", value = "Fetch additional information about fish by name", response = Fish.class, nickname = "does_not_work_bug")
    @ApiResponses({
        @ApiResponse(code = 404, message = "No information about fish with such name", response = Void.class)})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "Fish name to lookup for", dataType = "string", paramType = "query", required = true)})
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getOutputStream().write(Json.pretty(new Fish()).getBytes(StandardCharsets.UTF_8));
    }

}
