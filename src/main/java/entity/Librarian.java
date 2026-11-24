package entity;

import java.io.Serializable;

/**
 * Predstavlja korisnički tip knjižničara u sustavu knjižnice.
 *
 * @author Mario Loncaric
 * @since 1.0
 */

public final class Librarian extends Person implements UserType, Serializable {
    private Long employeeId;


    public Librarian(){
        super();
    }

    public Librarian(String firstName, String lastName, Long employeeId){
        super(firstName, lastName);
        this.employeeId = employeeId;
    }
    @Override
    public Long getIdentifier(){

        return employeeId;
    }

    /**
     * Vraća kratki tekstualni opis knjižničara (ime, prezime i employeeId).
     */

    @Override
    public String toString(){
        return super.toString() + " Employee ID: " + employeeId;
    }
}
