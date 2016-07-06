package android.johanmagnusson.se.projector.constant;

public final class Firebase {

    public static final String NODE_SITES = "sites";
    public static final String NODE_PROJECTS = "projects";

    public static final String PROPERTY_SITE_NAME = "name";

    public static String slash(String string) {
        return "/" + string + "/";
    }
}
