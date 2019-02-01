package org.unindra.cobazxing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class App implements Initializable {

    @FXML
    private Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleButton(@SuppressWarnings("unused") ActionEvent actionEvent) {
        ScannerQR scannerQR = new ScannerQR();
        Thread thread = new Thread(scannerQR);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (scannerQR.getResult() != null)
            label.setText(scannerQR.getResult().getText());
    }
}
