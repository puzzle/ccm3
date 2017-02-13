package ch.puzzle.ccm3;

import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import static java.lang.System.getProperty;

@ApplicationPath("/api/v1 ")
public class JaxRsActivator extends Application {
    public JaxRsActivator() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setScan(true);
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setResourcePackage("ch.puzzle.ccm3");
        beanConfig.setBasePath(getProperty("swagger.api.basepath", "/ccm3-viewer/api/v1"));
        beanConfig.setHost(getProperty("swagger.api.basepath", "localhost:8080"));
    }
}
