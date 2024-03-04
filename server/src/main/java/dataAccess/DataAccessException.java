package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {

    public DataAccessException(String message) {
        super(message);
    }

    public static int code;
    public DataAccessException(int code, String message) {
        super(message);
        DataAccessException.code = code;
    }
    public int getCode() {
        return code;
    }

}
