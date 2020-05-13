package services;

import domain.entities.Book;
import domain.entities.Borrow;
import domain.entities.Reader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import repositories.BookRepository;
import repositories.BorrowRepository;
import repositories.ReaderRepository;

import java.util.List;

public class BookReferenceService {

    public TextField bookField;
    public TableColumn<Borrow, String> title;
    public TableColumn<Borrow, String> author;
    public TableColumn<Borrow, String> startDate;
    public TableColumn<Borrow, String> endDate;
    public Label bookLabel;
    public Button reference;
    public TableView<Borrow> tableView = new TableView<>();

    public void reference(ActionEvent actionEvent) {

        String bookTitle = bookField.getText();
        System.out.println();
        Book book = BookRepository.findBookByTitle(bookTitle);
        bookLabel.setText(book.getTitle());
        List<Borrow> borrows = BorrowRepository.borrows(book.getTitle());
       /* for (Book book1 : books) {
            title.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getTitle()));
            author.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getAuthor().getName()));
            startDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStartDate())));
            endDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEndDate())));
        }*/

        title.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getTitle()));
        author.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getAuthor().getName()));
        startDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStartDate())));
        endDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEndDate())));

        List<Borrow> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        tableView.getItems().addAll((borrows));
    }


}
