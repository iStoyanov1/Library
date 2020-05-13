package repositories;

import domain.entities.Reader;

import javax.persistence.*;

public class ReaderRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");
    private static final EntityManager entityManager = emf.createEntityManager();

    public static Reader saveReader(String firstName, String lastName, String email) {

        Reader reader = new Reader();
        reader.setFirstName(firstName);
        reader.setLastName(lastName);
        reader.setEmail(email);
        entityManager.getTransaction().begin();
        entityManager.persist(reader);
        entityManager.getTransaction().commit();

        return reader;
    }

    public static Reader findByEmail(String email) {
        Reader reader = entityManager.createQuery("select r from readers r where r.email = :email", Reader.class)
                .setParameter("email", email)
                .getSingleResult();

        if(reader == null){
            throw new NoResultException();
        }
        return reader;
    }
}
