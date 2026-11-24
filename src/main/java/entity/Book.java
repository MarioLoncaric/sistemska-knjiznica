package entity;

import exceptions.NegativeValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.Serializable;

/**
 * entitet koji predstavlja knjigu u sustavu knjižnice.
 *
 * @author Mario Loncaric
 * @version 1.0
 */

public class Book
        implements Borrowable, Serializable {

    private static final Logger log = LoggerFactory.getLogger(Book.class);

    //atributi
    private String title;
    private String author;
    private String publisher;
    private int numberOfPages;
    private int availableCopies;
    private Language language;
    private Long bookId;
    private Genre genre;



    //Konstruktori

    public Book(){}

    /**
     * Kreira novu knjigu sa zadanim podacima i početnim stanjem primjeraka.
     *
     * @param title           naslov knjige
     * @param author          autor knjige
     * @param publisher       izdavac
     * @param language        jezik
     * @param bookId          jedinstveni identifikator knjige
     * @param numberOfPages   ukupan broj stranica (>= 0)
     * @param availableCopies broj trenutno dostupnih primjeraka (>= 0)
     * @param genre           žanr knjige
     */
    public Book(String title, String author, String publisher, Language language, Long bookId, int numberOfPages, int availableCopies, Genre genre){
        if (numberOfPages < 0 || availableCopies < 0) {
            throw new NegativeValueException("Broj stranica i količina ne smiju biti negativni.");
        }
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.language = language;
        this.bookId = bookId;
        this.numberOfPages = numberOfPages;
        this.availableCopies = availableCopies;
        this.genre = genre;
    }

    /**
     * Evidentira posudbu primjerka knjige.
     */

    @Override
    public void borrow() {
        log.trace("borrow() bookId={}", bookId);
        if (availableCopies > 0) {
            availableCopies--;
            log.info("Book borrowed: bookId={}, available={}", bookId, availableCopies);
        } else {
            log.warn("Borrow denied, no copies: bookId={}", bookId);
        }
    }

    /**
     * Evidentira povrat primjerka knjige.
     */

    @Override
    public void returnItem() {
        log.trace("returnItem() bookId={}", bookId);
        availableCopies++;
        log.info("Book returned: bookId={}, available={}", bookId, availableCopies);
    }



    //getter metode
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getPublisher(){
        return publisher;
    }
    public Integer getNumberOfPages(){
        return numberOfPages;
    }
    public Integer getAvailableCopies(){
        return availableCopies;
    }
    public Language getLanguage(){
        return language;
    }
    public Long getBookId(){
        return bookId;
    }
    public Genre getGenre(){
        return genre;
    }

    //setter metode
    public void setTitle(String title){
        this.title = title;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public void setPublisher( String publisher){
        this.publisher=publisher;
    }
    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies= availableCopies;
    }
    public void setLanguage(Language language) {
        this.language = language;
    }
    public void setGenre(Genre genre){
        this.genre = genre;
    }
    public void setBookId(Long bookId){
        this.bookId = bookId;
    }
    //metoda za ispis

    @Override
    public String toString(){
        return "Book: " + title + " - " + author + " (Jezik: " + language + ") - BookId kod: " + bookId;
    }



    /**
     * Builder za preglednije kreiranje objekta {@link Book} bez dugačkog konstruktora.
     */

    public static class Builder {
        private String title;
        private String author;
        private String publisher;
        private int numberOfPages;
        private int availableCopies;
        private Language language;
        private Long bookId;
        private Genre genre;

        public Builder title(String v) {
            this.title = v; return this;
        }
        public Builder author(String v) {
            this.author = v; return this;
        }
        public Builder publisher(String v) {
            this.publisher = v; return this;
        }
        public Builder numberOfPages(int v) {
            this.numberOfPages = v; return this;
        }
        public Builder availableCopies(int v) {
            this.availableCopies = v; return this;
        }
        public Builder language(Language v) {
            this.language = v; return this;
        }
        public Builder bookId(Long v) {
            this.bookId = v; return this;
        }
        public Builder genre(Genre g) {
            this.genre = g; return this;
        }

        /**
         * Kreira novi {@link Book} na temelju trenutno postavljenih vrijednosti.
         */

        public Book build() {
            Book b = new Book(title, author, publisher, language, bookId, numberOfPages, availableCopies, genre);
            return b;
        }
    }
}

