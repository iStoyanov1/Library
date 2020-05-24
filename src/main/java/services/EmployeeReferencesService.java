package services;

import com.jfoenix.controls.JFXButton;
import domain.entities.Book;
import domain.entities.Borrow;
import domain.entities.Employee;
import domain.entities.RentBook;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import repositories.BookRepository;
import repositories.BorrowRepository;
import repositories.EmployeeRepository;
import repositories.RentBookRepository;

import javax.persistence.Query;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

public class EmployeeReferencesService implements Runnable {

    public static List<Borrow> borrows;
    public static int bookId;
    public static Date bookEndDate;
    public TextField bookField;

    public Employee employee;

    public TableColumn<Borrow, String> title;
    public TableColumn<Borrow, String> author;
    public TableColumn<Borrow, String> startDate;
    public TableColumn<Borrow, String> endDate;

    public Label bookLabel;
    public JFXButton reference;
    public TableView<Borrow> tableView = new TableView<>();
    public Pane updateBook;
    public DatePicker newDate;
    public Button confirmDate;
    public Button returnBook;
    public Label nameLabel;
    public JFXButton bookReservation;
    public JFXButton bookReferences;
    public Button signOut;
    public JFXButton search;
    public JFXButton reader;


    public void initialize() {

        Employee employee = LoginEmployeeService.loggedEmployee.get(LoginEmployeeService.loggedEmployee.size() - 1);

        nameLabel.setText(employee.getName());
        reference();
    }


    public void reference() {

      /*  String bookTitle = bookField.getText();
        Book book = BookRepository.findBookByTitle(bookTitle);

        bookLabel.setText(book.getTitle());*/

        employee = LoginEmployeeService.loggedEmployee.get(LoginEmployeeService.loggedEmployee.size() - 1);
        borrows = EmployeeRepository.employeeBorrows(employee.getName());

        for (Borrow borrow : borrows) {
            title.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getTitle()));
            author.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getAuthor().getName()));
            startDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStartDate())));
            endDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEndDate())));
        }

        tableView.getItems().addAll((borrows));

        initThread();
    }

    @FXML
    private void initThread() {
        Thread thread = new Thread(() -> {
            Runnable update = () -> bookNotification();
            while (true) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                }
                Platform.runLater(update);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {

    }

    private void bookNotification() {

        Date date = Date.valueOf(LocalDate.now());
        System.out.println();
        List<Borrow> borrows = EmployeeRepository.employeeBorrows(employee.getName());
        for (Borrow borrow : borrows) {
            if (borrow.getEndDate().before(date)) {
                bookId = borrow.getBook().getId();
                bookEndDate = borrow.getEndDate();
            }
        }

        Book book = BookRepository.findBookById(bookId);

        Date maxDate = RentBookRepository.maxDate(book.getId(), bookEndDate);

        try {
            if (maxDate.before(date)) {
                bookEndDate = maxDate;
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        String.format("Срокът за връщане на %s е приключил", book.getTitle()));
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    updateBook.setVisible(true);
                }
            }
        }catch (NullPointerException np){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    String.format("Няма невърнати книги."));
            Optional<ButtonType> result = alert.showAndWait();
        }
    }


    public void newDate(ActionEvent actionEvent) {

        LocalDate date = this.newDate.getValue();
        Date newDate = Date.valueOf(date);

        updateBook.setVisible(false);
        System.out.println();
        borrows.clear();

        Query borrow = BorrowRepository.updateBorrow(newDate, bookId);
        for (int i = 0; i < tableView.getItems().size(); i++) {
            tableView.getItems().clear();
        }
        tableView.getItems().addAll(employee.getBorrows());

        Book book = BookRepository.findBookById(bookId);

        BookReservationService.saveRentBook(bookEndDate.toLocalDate().plusDays(1), date, book);

        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                String.format("Срокът за връщане на %s е удължен до: %s", book.getTitle(), newDate));
        Optional<ButtonType> result = alert.showAndWait();

    }

    public void returnBook(ActionEvent actionEvent) {
        updateBook.setVisible(false);

        Book book = BookRepository.findBookById(bookId);

        for (Borrow borrow : borrows) {
            RentBookRepository.deleteRent(borrow.getStartDate(), borrow.getEndDate(), bookId);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                String.format("Книгата %s е върната", book.getTitle()));
        Optional<ButtonType> result = alert.showAndWait();

    }

    public void bookReservation(ActionEvent actionEvent) throws IOException {

        bookReservation.getScene().getWindow().hide();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/book-reservation.fxml"));
        Scene scene = new Scene(root);
        login.setScene(scene);
        login.show();
    }

    public void bookReferences(ActionEvent actionEvent) throws IOException {

        bookReferences.getScene().getWindow().hide();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/all-books-references.fxml"));
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

    public void reader(ActionEvent actionEvent) throws IOException {

        reader.getScene().getWindow();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/reader-history.fxml"));
        Scene scene = new Scene(root);
        login.setScene(scene);
        login.show();


    }
}
