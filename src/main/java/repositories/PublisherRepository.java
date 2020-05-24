package repositories;

import domain.entities.Book;
import domain.entities.Publisher;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class PublisherRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");
    private static final EntityManager entityManager = emf.createEntityManager();

    public static List<Book> findBookByPublisher(String publisher){

        List<Book> booksByPublisher = entityManager.createQuery("select b from books b inner join " +
                        "publishers p on b.publisher.id = p.id " +
                        "where p.publisher like :publisher"
                , Book.class)
                .setParameter("publisher", '%'+publisher+'%')
                .getResultList();

        return booksByPublisher;
    }

}
