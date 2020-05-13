package services;

import domain.entities.Author;
import domain.entities.Book;
import domain.entities.Borrow;
import domain.entities.Reader;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import repositories.BookRepository;
import repositories.ReaderRepository;

import java.sql.Date;
import java.util.List;

public class ReaderHistoryService {
    public TextField email;
    public TableView<Borrow> tableView = new TableView<>();
    public TableColumn<Borrow, String> authorColumn;
    public TableColumn<Borrow, String> titleColumn;
    public TableColumn<Borrow, String> startDate;
    public TableColumn<Borrow, String> endDate;
    public Button reference;
    public Label readerEmail;

    public void reference(ActionEvent actionEvent) {

        String email1 = email.getText();

        Reader reader = ReaderRepository.findByEmail(email1);
        readerEmail.setText(reader.getEmail()); 
        List<Borrow> borrows = FXCollections.observableArrayList(reader.getBorrows());
        for (Borrow borrow : borrows) {
            titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getTitle()));
            authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getAuthor().getName()));
            startDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStartDate())));
            endDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEndDate())));
        }
        List<Borrow> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        tableView.getItems().addAll((borrows));
    }
}
