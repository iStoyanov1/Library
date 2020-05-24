package services;

import domain.entities.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repositories.EmployeeRepository;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginEmployeeService {

    public static List<Employee> loggedEmployee = new ArrayList<>();

    public TextField nameField;
    public PasswordField passwordField;
    public Button submit;
    public Label invalid;


    public void login(ActionEvent actionEvent) throws IOException {

        String name = nameField.getText();
        String password = passwordField.getText();

        try {
            Employee employee = EmployeeRepository.findEmployee(name, password);
            loggedEmployee.add(employee);
            home();
        }catch (NoResultException nre){
            invalid.setVisible(true);
        }

    }

    private void home() throws IOException {
        submit.getScene().getWindow().hide();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/employee-references.fxml"));
        Scene scene = new Scene(root);
        login.setScene(scene);
        login.show();
    }

}
