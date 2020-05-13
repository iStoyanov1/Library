package services;

import domain.entities.Book;
import domain.entities.Borrow;
import domain.entities.Employee;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import repositories.BookRepository;
import repositories.BorrowRepository;

import java.util.List;

public class EmployeeReferencesService {

    public TextField bookField;
    public TableColumn<Borrow, String> title;
    public TableColumn<Borrow, String> author;
    public TableColumn<Borrow, String> startDate;
    public TableColumn<Borrow, String> endDate;
    public Label bookLabel;
    public Button reference;
    public TableView<Borrow> tableView = new TableView<>();


    public void initialize(){
        reference();
    }


    public void reference() {

      /*  String bookTitle = bookField.getText();
        Book book = BookRepository.findBookByTitle(bookTitle);

        bookLabel.setText(book.getTitle());*/

        Employee employee = LoginEmployeeService.loggedEmployee.get(LoginEmployeeService.loggedEmployee.size() - 1);

         List<Borrow> borrows = BorrowRepository.employeeBorrows(employee.getName());
        for (Borrow borrow : borrows) {
            title.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getTitle()));
            author.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getAuthor().getName()));
            startDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStartDate())));
            endDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEndDate())));
        }
        List<Borrow> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        tableView.getItems().addAll((borrows));
    }

}
