package ch.puzzle.ccm3;

import static java.lang.System.getProperty;

public class DefaultValues {
    public static final int PAGE_SIZE = 25;
    public static String SWAGGER_BASE_PATH = getProperty("swagger.api.basepath", "/ccm3-viewer/api/v1");
    public static String SWAGGER_HOST = getProperty("swagger.api.host", "localhost:8080");

}
