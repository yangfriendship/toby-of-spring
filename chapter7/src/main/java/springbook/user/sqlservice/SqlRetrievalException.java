package springbook.user.sqlservice;

public class SqlRetrievalException extends RuntimeException {

    public SqlRetrievalException(String message) {
        super(message);
    }

    public SqlRetrievalException(Throwable cause) {
        super(cause);
    }
}
