package services;

import domain.entities.Borrow;
import domain.entities.Reader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import repositories.BorrowRepository;
import repositories.ReaderRepository;

import javax.persistence.NoResultException;
import java.util.List;

public class ReaderHistoryService {

    private static List<Borrow> borrows;

    public TextField email;
    public TableView<Borrow> tableView = new TableView<>();
    public TableColumn<Borrow, String> authorColumn;
    public TableColumn<Borrow, String> titleColumn;
    public TableColumn<Borrow, String> startDate;
    public TableColumn<Borrow, String> endDate;
    public Button reference;
    public TableView<Reader> tableEmailView = new TableView<>();
    public TableColumn emailColumn;
    public TableColumn firstNameColumn;
    public TableColumn surnameColumn;
    public Label emailReader;
    public Label invalidEmail;


    public void initialize() {
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        List<Reader> allReaders = FXCollections.observableArrayList(ReaderRepository.allReaders());

        tableEmailView.getItems().addAll(allReaders);

        tableEmailView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Reader>() {
            @Override
            public void changed(ObservableValue<? extends Reader> observable, Reader oldValue, Reader newValue) {
                reference();
            }
        });
    }


    public void reference() {

        String readerEmail = tableEmailView.getSelectionModel().getSelectedItem().getEmail();
        Reader reader = null;
        try {
            reader = ReaderRepository.findByEmail(readerEmail);
            emailReader.setText(reader.getEmail());
        } catch (NoResultException nre) {
            invalidEmail.setVisible(true);
        }
        if (reader != null) {

            borrows = ReaderRepository.readerBorrows(readerEmail);

            titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getTitle()));
            authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBook().getAuthor().getName()));
            startDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getStartDate())));
            endDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getEndDate())));

            refreshTable();
            tableView.getItems().addAll((borrows));
        }
    }

    private void refreshTable() {
        List<Borrow> returned = tableView.getItems();
        tableView.getItems().removeAll(returned);
        tableView.refresh();
    }

    public void filterEmails(ActionEvent actionEvent) {

        String emailReader = email.getText();

        List<Reader> returned = tableEmailView.getItems();
        tableEmailView.getItems().removeAll(returned);
        tableEmailView.refresh();
        List<Reader> findByEmail = ReaderRepository.findByLetter(emailReader);
        tableEmailView.getItems().addAll((findByEmail));
    }
}

