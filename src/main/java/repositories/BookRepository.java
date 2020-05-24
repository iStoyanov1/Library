package repositories;

import domain.entities.Book;
import domain.entities.Category;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class BookRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");
    private static final EntityManager entityManager = emf.createEntityManager();


    public static List<Book> allBooks(){
        List<Book> allBooks = entityManager.createQuery("select b from books b", Book.class)
                .getResultList();

        return allBooks;
    }
    public static Book findBookByTitle(String title){
        Book book = entityManager.createQuery("select b from books b where b.title = :title", Book.class)
                .setParameter("title", title)
                .getSingleResult();

        return book;
    }
    public static List<Book> findBookByCategory(Category category) {

        List<Book> books = entityManager.createQuery("select b from books b where b.category = :category", Book.class)
                .setParameter("category", category)
                .getResultList();

        return books;
    }

    public static List<Book> findBookByYear(String year) {

        List<Book> books = entityManager.createQuery("select b from books b where b.year like :year", Book.class)
                .setParameter("year", '%'+year+'%')
                .getResultList();

        return books;
    }
    public static Book findBookById(int id){
        Book book = entityManager.createQuery("select b from books b where b.id = :id", Book.class)
                .setParameter("id", id)
                .getSingleResult();

        return book;
    }
    public static List<Book> topBooks(){
        List<Book> topBooks = entityManager.createQuery("select b " +
                "from books b\n" +
                "inner join borrows b2 on b.id = b2.book.id\n" +
                "group by b2.book.id\n" +
                "order by count(b2.book.id) desc", Book.class)
                .getResultList();

        return topBooks;
    }
    public static List<Book> findBooksByLetter(String title){
        List<Book> book = entityManager.createQuery("select b from books b where b.title like :title", Book.class)
                .setParameter("title", '%'+title+'%')
                .getResultList();

        return book;
    }
}
