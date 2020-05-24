package repositories;

import domain.entities.Book;
import domain.entities.Borrow;
import domain.entities.Employee;
import domain.entities.Reader;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.sql.Date;
import java.util.List;

public class BorrowRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");
    private static final EntityManager entityManager = emf.createEntityManager();

    public static void saveBorrow(Date from, Date to, Reader reader, Book book, Employee employee){
        entityManager.getTransaction().begin();

        Borrow borrow = new Borrow();
        borrow.setBook(book);
        borrow.setReader(reader);
        borrow.setEmployee(employee);
        borrow.setStartDate(from);
        borrow.setEndDate(to);
        entityManager.persist(borrow);
        entityManager.getTransaction().commit();

    }




    public static Query updateBorrow(Date date1, int id){
        entityManager.getTransaction().begin();
        Query borrow = entityManager.createQuery("update borrows b set b.endDate = :date1 where b.book.id = :id")
                .setParameter("date1", date1)
                .setParameter("id", id);
        borrow.executeUpdate();
        entityManager.getTransaction().commit();

        return borrow;
    }
    public static List<Borrow> borrows(String book){
        List<Borrow> borrows = entityManager.createQuery("select b from borrows b " +
                "inner join books b2 on b.book.id = b2.id " +
                "where b2.title = :title", Borrow.class)
                .setParameter("title", book)
                .getResultList();

        return borrows;
    }
}
