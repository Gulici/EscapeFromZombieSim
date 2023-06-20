package controller;

/**
 * Interface for controller classes.
 */
public interface EntityController {
    boolean isRequestingUp();
    boolean isRequestingDown();
    boolean isRequestingLeft();
    boolean isRequestingRight();
}
