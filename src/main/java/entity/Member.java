package entity;

import java.io.Serializable;

/**
 * Predstavlja člana knjižnice (korisnik sustava).
 *
 * @author Mario Loncaric
 * @since 1.0
 */


public non-sealed class Member extends Person implements UserType, Serializable {

    //atributi
    private Long memberId;
    private String email;
    private String oib;
    private boolean activity;
    private String birthday;
    private String phoneNumber;


    // konstruktor

    public Member(){
        super();
    }

    /**
     * Kreira novog člana knjižnice.
     *
     * @param memberId    jedinstveni identifikator člana
     * @param firstName   ime (nasljeđeno iz {@link Person})
     * @param lastName    prezime (nasljeđeno iz {@link Person})
     * @param email       e-mail adresa
     * @param oib         hrvatski OIB
     * @param birthday    datum rođenja
     * @param phoneNumber telefonski broj
     * @param activity    je li član aktivan
     */
    public Member(Long memberId, String firstName, String lastName, String email, String oib,
                  String birthday, String phoneNumber, boolean activity){
        super(firstName, lastName);
        this.memberId = memberId;
        this.email = email;
        this.oib = oib;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.activity = activity;
    }

    public Member(String firstName, String lastName, String email, String oib, String birthday, String phoneNumber, boolean activity) {
        this(null, firstName, lastName, email, oib, birthday, phoneNumber, activity);
    }

    @Override
    public Long getIdentifier(){
        return memberId;
    }

    //getter metode

    public String getEmail(){
        return email;
    }
    public String getOib(){
        return oib;
    }
    public boolean isActivity(){
        return activity;
    }
    public String getBirthday(){
        return birthday;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }

    //setter metode
    public void setIdentifier(Long memberId){
        this.memberId = memberId;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setOib(String oib){
        this.oib = oib;
    }
    public void setActivity(boolean activity){
        this.activity = activity;
    }
    public void setBirthday(String birthday){
        this.birthday = birthday;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * Vraća kratak tekstualni opis člana u formatu "Ime Prezime (ID: x)".
     */
    @Override
    public String toString(){
        return super.toString() + " (ID: " + memberId + ")";
    }

}
