package repositories;

import domain.entities.Borrow;
import domain.entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.List;

public class EmployeeRepository {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence");
    private static final EntityManager entityManager = emf.createEntityManager();

    public static Employee findEmployee(String name, String password){
        Employee employee = entityManager.createQuery("select e from employees e where e.name = :name and " +
                "e.password = :password", Employee.class)
                .setParameter("name", name)
                .setParameter("password", password)
                .getSingleResult();

        if (employee == null){
            throw new NoResultException();
        }
        return employee;
    }

    public static List<Borrow> employeeBorrows(String name){
        List<Borrow> borrows = entityManager.createQuery("select b from borrows b inner join employees e " +
                "on b.employee.id = e.id where e.name = :name", Borrow.class)
                .setParameter("name", name)
                .getResultList();

        return borrows;
    }
}
