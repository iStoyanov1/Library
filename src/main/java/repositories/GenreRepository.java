package repositories;

import domain.entities.Book;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class GenreRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");
    private static final EntityManager entityManager = emf.createEntityManager();

    public static List<Book> findBooksByGenre(String genre){

        List<Book> books = entityManager.createQuery("select b from books b inner join genres g on b.genre.id = g.id " +
                "where g.genre like :genre", Book.class)
                .setParameter("genre", '%'+genre+'%')
                .getResultList();

        return books;
    }

}
