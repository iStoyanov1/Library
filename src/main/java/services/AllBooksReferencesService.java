package services;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import domain.entities.Book;
import domain.entities.Borrow;
import domain.entities.Category;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repositories.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllBooksReferencesService {

    private static String bookTitle;

    public TableView<Book> tableView = new TableView<>();
    public TableColumn<Book, String> titleColumn;
    public TableColumn<Book, String> authorColumn;
    public TableColumn<Book, String> genreColumn;
    public TableColumn<Book, Integer> yearColumn;
    public TableColumn<Book, String> publisherColumn;
    public TableColumn<Book, String> categoryColumn;
    public JFXComboBox searchCombo;
    public TextField searchField;
    public JFXButton topBooks;
    public TableView<Borrow> borrowTable = new TableView<>();
    public TableColumn<Borrow, String> titleBookBorrow;
    public TableColumn<Borrow, String> titleStartDateBorrow;
    public TableColumn<Borrow, String> titleEndDateBorrow;
    public Label bookLabel;
    public JFXButton back;

    public void initialize() {

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor().getName()));
        genreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGenre().getGenre()));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        publisherColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPublisher().getPublisher()));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        List<Book> books = FXCollections.observableArrayList(BookRepository.allBooks());

        tableView.getItems().addAll(books);

        searchCombo.getItems().addAll("Заглавие", "Жанр", "Автор", "Година", "Издателство", "Категория");

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                bookTitle = tableView.getSelectionModel().getSelectedItem().getTitle();
                borrowsBook();
            }
        });
    }

    public void search(ActionEvent actionEvent) {

        String category = searchCombo.getSelectionModel().getSelectedItem().toString();

        if (category.equals("Жанр")) {
            findBookByGenre();
        } else if (category.equals("Автор")) {
            findBookByAuthor();
        } else if (category.equals("Заглавие")) {
            findBookByTitle();
        } else if (category.equals("Година")) {
            findBookByYear();
        } else if (category.equals("Категория")) {
            findBookByCategory();
        }else if (category.equals("Издателство")) {
            findBookByPublisher();
        }

    }


    private void borrowsBook(){
        titleBookBorrow.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getTitle()));
        titleStartDateBorrow.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        titleEndDateBorrow.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        List<Borrow> returned = borrowTable.getItems();

        borrowTable.getItems().removeAll(returned);
        borrowTable.refresh();
        List<Borrow> borrowBooks = BorrowRepository.borrows(bookTitle);
        borrowTable.getItems().addAll((borrowBooks));
        bookLabel.setText(bookTitle);

    }

    public void mostReadBooks(ActionEvent actionEvent) {

        List<Book> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        List<Book> topBooks = BookRepository.topBooks();
        tableView.getItems().addAll((topBooks));

    }

    private void findBookByGenre() {
        String genre = searchField.getText();

        List<Book> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        List<Book> findByGenre = GenreRepository.findBooksByGenre(genre);
        tableView.getItems().addAll((findByGenre));
    }
    private void findBookByAuthor() {
        String author = searchField.getText();

        List<Book> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        List<Book> booksByAuthor = AuthorRepository.findBookByAuthor(author);
        tableView.getItems().addAll((booksByAuthor));
    }

    private void findBookByPublisher() {
        String publisher = searchField.getText();

        List<Book> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        List<Book> booksByPublisher = PublisherRepository.findBookByPublisher(publisher);
        tableView.getItems().addAll((booksByPublisher));
    }

    private void findBookByTitle() {
        String title = searchField.getText();

        List<Book> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        List<Book> books = BookRepository.findBooksByLetter(title);
        tableView.getItems().addAll(books);
    }

    private void findBookByCategory() {

        String category = searchField.getText();
        Category categories = Category.valueOf(category);

        List<Book> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        List<Book> books = BookRepository.findBookByCategory(categories);
        tableView.getItems().addAll((books));
    }

    private void findBookByYear() {
        String year = searchField.getText();

        List<Book> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        List<Book> books = BookRepository.findBookByYear(year);
        tableView.getItems().addAll((books));

    }

    public void back(ActionEvent actionEvent) throws IOException {
        back.getScene().getWindow().hide();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/employee-references.fxml"));
        Scene scene = new Scene(root);
        login.setScene(scene);
        login.show();

    }
}
