package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public final class FileChooseController implements Initializable {
    static File targetfile = null;
    private Main application;
    public void setApp(Main application){
        this.application = application;
    }
    @FXML
    public void chooseFile() throws Exception {
        final FileChooser fileChooser = new FileChooser();
        targetfile = fileChooser.showOpenDialog(Main.stage);
        if(targetfile==null) return;
        Main.proxy.gotoP3();
        fillTableController tableHandler=Main.p3;
        tableHandler.handle();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }
}