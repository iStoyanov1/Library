package services;

import domain.entities.Reader;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import repositories.ReaderRepository;

import javax.persistence.NoResultException;

public class AddReader {
    public TextField nameText;
    public TextField familyText;
    public TextField emailText;
    public Label successAdd;
    public Label notSuccess;


    public void addReader(ActionEvent actionEvent) {

        String name = nameText.getText();
        String family = familyText.getText();
        String email = emailText.getText();
        Reader reader = null;

        try{
            reader = ReaderRepository.findByEmail(email);
            successAdd.setVisible(false);
            notSuccess.setVisible(true);

        }catch (NoResultException nre) {
            ReaderRepository.saveReader(name, family, email);
            notSuccess.setVisible(false);
            successAdd.setVisible(true);
        }

    }
}
