package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
    public static int code;
    public DataAccessException(int code, String message) {
        super(message);
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
