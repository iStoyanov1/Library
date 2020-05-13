package services;


import domain.entities.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import repositories.*;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BookReservationService {

    private static Book book;

    public TableView<Book> tableView = new TableView<>();
    public TableColumn<Book, String> titleColumn;
    public TableColumn<Book, String> authorColumn;
    public TableColumn<Book, String> genreColumn;
    public TableColumn<Book, Integer> yearColumn;
    public TableColumn<Book, String> publisherColumn;
    public TableColumn<Book, String> categoryColumn;
    public TextField firstName;
    public TextField lastName;
    public TextField email;
    public DatePicker dateFrom;
    public DatePicker dateTo;
    public Button searchButton;
    public TextField searchField;
    public ComboBox searchCombo;
    public Button referenceForBook;
    public Button readerHistory;
    public Button add;
    public AnchorPane anchorReader;

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
                //Check whether item is selected and set value of selected iif (tableView.getSelectionModel().getSelectedItem() != null) {
                  /*  TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
                    ObservableList selectedCells = selectionModel.getSelectedCells();
                    TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                    Object val = tablePosition.getTableColumn().getCellData(newValue);

                    String title = val.toString();*/

                String title = tableView.getSelectionModel().getSelectedItem().getTitle();

                book = BookRepository.findBookByTitle(title);
            }
        });
    }

    public void borrow(ActionEvent actionEvent) {
        checkIsBookAvailable();
    }

    public void checkIsBookAvailable() {

        String email1 = email.getText();

        LocalDate fromDate = dateFrom.getValue();
        Date from = Date.valueOf(fromDate);

        LocalDate toDate = dateTo.getValue();
        Date to = Date.valueOf(toDate);


        Employee employee = LoginEmployeeService.loggedEmployee.get(LoginEmployeeService.loggedEmployee.size() - 1);
        Reader reader = null;
        try {
            reader = ReaderRepository.findByEmail(email1);
        } catch (NoResultException nre) {
           // reader = ReaderRepository.saveReader(firstName1, lastName1, email1);
            anchorReader.setVisible(true);
        }
        if (reader != null) {

            if (book != null) {
                List<RentBook> rentBooks = null;
                try {
                    rentBooks = RentBookRepository.isAvailable(book.getTitle(), from, to);
                    Optional<ButtonType> alert = new Alert(Alert.AlertType.ERROR, String.format(
                            "%s е заета за този период: %s до %s!", book.getTitle(), from, to)).showAndWait();

                } catch (NoResultException nre) {
                    Optional<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION, String.format(
                            "Успешно направи заемане на %s от %s до %s!", book.getTitle(), from, to)).showAndWait();
                    BorrowRepository.saveBorrow(from, to, reader, book, employee);
                    saveRentBook(fromDate, toDate, book);
                }
            }else{
                Optional<ButtonType> alert = new Alert(Alert.AlertType.ERROR, String.format(
                        "Моля изберете книга!")).showAndWait();
            }
        }
    }


    public void search(ActionEvent actionEvent) {
        String category = searchCombo.getSelectionModel().getSelectedItem().toString();

        if (category.equals("Жанр")) {
            findBookByGenre();
        } else if (category.equals("Автор")) {
            findBookByAuthor();
        } else if (category.equals("Издателство")) {
            findBookByPublisher();
        } else if (category.equals("Заглавие")) {
            findBookByTitle();
        } else if (category.equals("Категория")) {
            findBookByCategory();
        } else if (category.equals("Година")) {
            findBookByYear();
        }
    }

    private boolean saveRentBook(LocalDate fromDate, LocalDate toDate, Book book) {

        while (!fromDate.isAfter(toDate)) {
            RentBookRepository.rent(Date.valueOf(fromDate), book);
            fromDate = fromDate.plusDays(1);
        }

        return true;
    }

    private void findBookByGenre() {
        String genre = searchField.getText();

        List<Book> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        List<Book> findByGenre = BookRepository.findBookByGenre(genre);
        tableView.getItems().addAll((findByGenre));
    }

    private void findBookByAuthor() {
        String author = searchField.getText();

        List<Book> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        Author author1 = AuthorRepository.getBooks(author);
        tableView.getItems().addAll((author1.getBooks()));
    }

    private void findBookByPublisher() {
        String publisher = searchField.getText();

        List<Book> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        Publisher publisher1 = PublisherRepository.findBookByPublisher(publisher);
        tableView.getItems().addAll((publisher1.getBooks()));
    }

    private void findBookByTitle() {
        String title = searchField.getText();

        List<Book> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
        Book book = BookRepository.findBookByTitle(title);
        tableView.getItems().addAll((book));
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

    public void referenceBook(ActionEvent actionEvent) {
    }

    public void readerHistory(ActionEvent actionEvent) throws IOException {

        readerHistory.getScene().getWindow();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/reader-history.fxml"));
        Scene scene = new Scene(root);
        login.setScene(scene);
        login.show();

    }

    public void addReader(ActionEvent actionEvent) throws IOException {

        add.getScene().getWindow();
        Stage login = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/add-reader.fxml"));
        Scene scene = new Scene(root);
        login.setScene(scene);
        login.show();
        anchorReader.setVisible(false);

    }
}
