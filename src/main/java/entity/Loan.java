package entity;

import java.io.Serializable;

/**
 * Model posudbe knjige članu knjižnice.
 */

public class Loan implements Serializable {

    //atributi
    private Long loanId;
    private Member member;
    private Book book;
    private String dateFrom;
    private String dateTo;
    private LoanStatus loanStatus;



    //konstruktor

    public Loan(){}

    public Loan(Long loanId,Member member, Book book, String dateFrom, String dateTo, LoanStatus loanStatus) {
        this.loanId = loanId;
        this.member = member;
        this.book = book;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.loanStatus = loanStatus;
    }

    public Loan(Book book, Member member, String dateFrom, String dateTo, LoanStatus loanStatus) {
        this.book = book;
        this.member = member;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.loanStatus = loanStatus;
    }

    //getteri i setteri

    public Long getLoanId() {
        return loanId;
    }

    public Member getMember() {
        return member;
    }

    public LoanStatus getLoanStatus(){
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus status){
        this.loanStatus = status;
    }

    public void setLoanId(Long loanId){
        this.loanId = loanId;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * Vraća kratki tekstualni opis posudbe.
     */
    @Override
    public String toString(){

        return "Loan: (" + member.getFirstName() + " " + member.getLastName() + " (" + member.getIdentifier() + "), BookID: " + book.getBookId() + " from " + dateFrom + ", status: " + loanStatus;
    }
}
