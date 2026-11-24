package entity;

/**
 * Ugovor za objekte koje se mogu posuditi i vratiti.
 *
 * @author Mario Loncaric
 * @since 1.0
 */

public interface Borrowable {
    /**
     * Evidentira posudbu primjerka.
     */
    void borrow();

    /**
     * Evidentira povrat primjerka.
     */
    void returnItem();
}
