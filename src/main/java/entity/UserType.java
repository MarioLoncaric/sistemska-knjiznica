package entity;

/**
 * Sealed sučelje koje predstavlja sve dopuštene korisničke tipove u sustavu knjižnice.
 *
 * @author Mario Loncaric
 * @since 1.0
 */

public sealed interface UserType permits Member, Librarian{
}
