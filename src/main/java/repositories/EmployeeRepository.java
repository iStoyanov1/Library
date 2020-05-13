package repositories;

import domain.entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

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
}
