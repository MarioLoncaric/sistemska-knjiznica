package entity;

import java.io.Serializable;

/**
 * Apstraktna bazna klasa za sve osobe u sustavu (npr. {@code Member}, {@code Librarian}).
 *
 * @author Mario Loncaric
 * @since 1.0
 */

public abstract class Person implements Serializable {
    private String firstName;
    private String lastName;


    protected Person(){
    }

    protected Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public abstract Long getIdentifier();

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    /**
     * Kratak tekstualni opis osobe.
     */
    @Override
    public String toString(){
        return getFirstName() + " " + getLastName();
    }
}
