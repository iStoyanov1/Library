package repositories;

import domain.entities.Book;
import domain.entities.Borrow;
import domain.entities.Employee;
import domain.entities.Reader;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
    public static List<Borrow> borrows(String title){
        List<Borrow> borrows = entityManager.createQuery("select b from borrows b where b.book.title = :title", Borrow.class)
                .setParameter("title",title)
                .getResultList();

        return borrows;
    }

    public static List<Borrow> employeeBorrows(String name){
        List<Borrow> borrows = entityManager.createQuery("select b from borrows b where b.employee.name = :name", Borrow.class)
                .setParameter("name", name)
                .getResultList();

        return borrows;
    }

}
