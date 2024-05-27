package nosh.nosh_map_server.domain;


// Is this for just your classes, Nou?
public class Validations {
    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }
}
