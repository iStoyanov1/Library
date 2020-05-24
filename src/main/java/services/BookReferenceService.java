package services;

import domain.entities.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import repositories.BookRepository;
import repositories.BorrowRepository;
import repositories.ReaderRepository;
import repositories.RentBookRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BookReferenceService {



    public TextField bookField;
    public TableColumn<RentBook, String> title;
    public TableColumn<RentBook, String> author;
    public TableColumn<RentBook, String> date;
    public Label bookLabel;
    public Button reference;
    public TableView<RentBook> tableView = new TableView<>();
    public DatePicker fromDate;
    public DatePicker toDate;


    public void initialize() {


        title.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getTitle()));
        author.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getAuthor().getName()));
        date.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getDate())));

        List<RentBook> rentBooks = RentBookRepository.rentBooks();

        tableView.getItems().addAll(rentBooks);
    }


    public void reference(ActionEvent actionEvent) {

        String title = bookField.getText();


        List<RentBook> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        List<RentBook> rentBook = RentBookRepository.findBookByTitle(title);
        tableView.getItems().addAll((rentBook));

    }


    public void referenceByDate(ActionEvent actionEvent) {
        LocalDate dateFrom = fromDate.getValue();
        Date from = Date.valueOf(dateFrom);

        LocalDate dateTo = toDate.getValue();
        Date to = Date.valueOf(dateTo);

        List<RentBook> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        List<RentBook> rentBook = RentBookRepository.booksByDate(from, to);
        tableView.getItems().addAll((rentBook));

        bookField.clear();
    }
}
