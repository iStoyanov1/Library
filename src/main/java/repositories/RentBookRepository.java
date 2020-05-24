package repositories;

import domain.entities.Book;
import domain.entities.RentBook;

import javax.persistence.*;
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

  public static void deleteRent(Date startDate, Date endDate, int bookId){
      entityManager.getTransaction().begin();
      Query deleteRent = entityManager.createQuery("delete From rent_book rb where " +
              "rb.date between :startDate and :endDate and rb.book.id = :id")
              .setParameter("startDate", startDate)
              .setParameter("endDate", endDate)
              .setParameter("id", bookId);
      deleteRent.executeUpdate();
      entityManager.getTransaction().commit();
  }

  public static List<RentBook> rentBooks(){
      List<RentBook> rentBooks = entityManager.createQuery("select rb from rent_book rb", RentBook.class)
              .getResultList();

      return rentBooks;
  }

  public static Date maxDate(int bookId, Date date){
      return (Date) entityManager.createQuery("select MAX(rb.date)" +
              "from rent_book rb\n" +
              "inner join books b on rb.book.id = b.id " +
              "where b.id = :bookId and rb.date = :date")
              .setParameter("bookId", bookId)
              .setParameter("date", date)
              .getSingleResult();

  }

    public static List<RentBook> findBookByTitle(String title) {

      List<RentBook> rentBooks = entityManager.createQuery("select rb from rent_book rb " +
              "inner join books b on rb.book.id = b.id " +
              "where b.title like :title ", RentBook.class)
              .setParameter("title", '%'+title+'%')
              .getResultList();

      return rentBooks;

    }
    public static List<RentBook> booksByDate(Date startDate, Date endDate){
      List<RentBook> booksByDate = entityManager.createQuery("select rb from rent_book rb " +
              "where rb.date between :startDate and :endDate", RentBook.class)
              .setParameter("startDate", startDate)
              .setParameter("endDate", endDate)
              .getResultList();

      return booksByDate;
    }
}
