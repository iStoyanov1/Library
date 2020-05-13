package repositories;

import domain.entities.Publisher;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PublisherRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");
    private static final EntityManager entityManager = emf.createEntityManager();

    public static Publisher findBookByPublisher(String publisher){

        Publisher publisher1 = entityManager.createQuery("select p from publishers p where p.publisher = :publisher"
                , Publisher.class)
                .setParameter("publisher", publisher)
                .getSingleResult();

        return publisher1;
    }

}
