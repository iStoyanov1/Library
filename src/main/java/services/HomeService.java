package services;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeService {


    public Button bookReservation;
    public Button signOut;
    public Button bookReferences;
    public Label employeeName;
    public Button employeeReferences;

    public void bookReservation(ActionEvent actionEvent) throws IOException {

        bookReservation.getScene().getWindow().hide();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/book-reservation.fxml"));
        Scene scene = new Scene(root);
        login.setScene(scene);
        login.show();

    }

    public void signOut(ActionEvent actionEvent) throws IOException {
        signOut.getScene().getWindow().hide();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login-employee.fxml"));
        Scene scene = new Scene(root);
        login.setScene(scene);
        login.show();

    }

    public void bookReferences(ActionEvent actionEvent) throws IOException {

        bookReferences.getScene().getWindow();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/book-references.fxml"));
        Scene scene = new Scene(root);
        login.setScene(scene);
        login.show();

    }

    public void employeeReferences(ActionEvent actionEvent) throws IOException {

        employeeReferences.getScene().getWindow();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/employee-references.fxml"));
        Scene scene = new Scene(root);
        login.setScene(scene);
        login.show();

    }



}
