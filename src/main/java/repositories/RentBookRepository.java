package repositories;

import domain.entities.Book;
import domain.entities.Borrow;
import domain.entities.RentBook;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.sql.Date;
import java.util.List;

public class RentBookRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");
    private static final EntityManager entityManager = emf.createEntityManager();

    public static void rent(Date date, Book book){
        entityManager.getTransaction().begin();
        RentBook rentBook = new RentBook();
        rentBook.setDate(date);
        rentBook.setBook(book);
        entityManager.persist(rentBook);
        entityManager.getTransaction().commit();

    }
   /* public static boolean isAvailable(Book book, Date startDate, Date endDate ){
        List<RentBook> rentBook = entityManager.createQuery("select rb.date, rb.book " +
                "from rent_book rb "+
                "where rb.date between :startDate and :endDate and rb.book.title = :title", RentBook.class)
                .setParameter("title",book)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        if(rentBook == null){
            return true;
        }
        return false;
    }
*/

   public static List<RentBook> isAvailable(String book, Date startDate, Date endDate){
       List<RentBook> rentBook = entityManager.createQuery("select rb from rent_book rb where rb.book.title = :title " +
               "and rb.date between :startDate and :endDate", RentBook.class)
               .setParameter("title",book)
               .setParameter("startDate", startDate)
               .setParameter("endDate", endDate)
               .getResultList();

       if (rentBook.size() == 0){
          throw new NoResultException();

       }
       return rentBook;
   }
}
