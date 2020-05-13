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


    public static Author getBooks(String name){
       Author author = entityManager.createQuery("select a from authors a where a.name = :name", Author.class)
               .setParameter("name", name)
               .getSingleResult();
       return author;
    }
}
