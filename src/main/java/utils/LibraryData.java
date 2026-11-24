package utils;

import entity.Book;
import entity.Librarian;
import entity.Loan;
import entity.Member;

import java.io.Serializable;
import java.util.List;

/**
 * Pomoćna klasa koja služi kao spremnik za sve podatke knjižnice
 */
public class LibraryData implements Serializable {

    private List<Member> members;
    private List<Book> books;
    private List<Librarian> librarians;
    private List<Loan> loans;

    public LibraryData(List<Member> members, List<Book> books, List<Librarian> librarians, List<Loan> loans) {
        this.members = members;
        this.books = books;
        this.librarians = librarians;
        this.loans = loans;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Librarian> getLibrarians() {
        return librarians;
    }

    public List<Loan> getLoans() {
        return loans;
    }
}