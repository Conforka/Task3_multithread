package by.horevich.threadtask.exception;

public class MultiThreadException extends Exception{
  public MultiThreadException() {
    super();
  }
  public MultiThreadException(String message) {
    super(message);
  }
  public MultiThreadException(String message, Throwable cause) {
    super(message, cause);
  }
  public MultiThreadException(Throwable cause) {
    super(cause);
  }
}
