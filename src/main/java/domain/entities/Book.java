package domain.entities;

import domain.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "books")
public class Book extends BaseEntity {

    private String title;
    private Author author;
    private Genre genre;
    private String year;
    private Category category;
    private Publisher publisher;
    private List<Borrow> borrows;

    public Book() {
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne(targetEntity = Author.class)
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
    @ManyToOne
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    @Column(name = "year", nullable = false)
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    @ManyToOne
    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @OneToMany(mappedBy = "book")
    public List<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(List<Borrow> borrows) {
        this.borrows = borrows;
    }
}
