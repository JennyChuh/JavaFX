package application;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class utilController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }
    public void maxWindow(){
        //这里可以写具有的窗口最大最小化动作
        Main.stage.setMaximized(true);
    }
    public void minWindow(){
        Main.stage.setIconified(true);
    }
}
