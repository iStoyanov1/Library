package domain.entities;

import domain.entities.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "authors")
public class Author extends BaseEntity {

    private String name;
    private List<Book> books;

    public Author() {
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "author")
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
