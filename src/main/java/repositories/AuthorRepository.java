package repositories;

import domain.entities.Book;
import domain.entities.Author;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class AuthorRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");
    private static final EntityManager entityManager = emf.createEntityManager();

    public static List<Book> findBookByAuthor(String author){
        List<Book> booksByAuthor = entityManager.createQuery("select b from books b inner join " +
                "authors a on b.author.id = a.id " +
                "where a.name LIKE :author ", Book.class)
                .setParameter("author", '%'+author+'%')
                .getResultList();

        return booksByAuthor;
    }
}
