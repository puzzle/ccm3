package ch.puzzle.ccm3;

import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import static ch.puzzle.ccm3.DefaultValues.SWAGGER_BASE_PATH;
import static ch.puzzle.ccm3.DefaultValues.SWAGGER_HOST;

@ApplicationPath("/api/v1 ")
public class JaxRsActivator extends Application {
    public JaxRsActivator() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setScan(true);
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setResourcePackage("ch.puzzle.ccm3");
        beanConfig.setBasePath(SWAGGER_BASE_PATH);
        beanConfig.setHost(SWAGGER_HOST);
    }
}
