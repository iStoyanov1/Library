package domain.entities;

import domain.entities.base.BaseEntity;

import javax.persistence.*;
import java.sql.Date;

@Entity(name = "rent_book")
public class RentBook extends BaseEntity {

    private Date date;
    private Book book;

    public RentBook() {
    }

    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}