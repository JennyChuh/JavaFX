package application;
	
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @quthor junjieZhu
 */
public class Main extends Application {
	static Stage stage = null;
	static Pane currPane=null;
	static fillTableController p3=null;
	static Main proxy=null;
	@Override
	public void start(Stage primaryStage) {
		//使用代理引用指向当前对象
		proxy = this;
		stage=primaryStage;
		try {
			primaryStage.setTitle("Message Retrieval");
			gotoIndex();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void gotoIndex(){
		try {
			FileChooseController index = (FileChooseController) replaceSceneContent("index.fxml");
			index.setApp(this);
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	public void gotoP3(){
		try {
			p3 = (fillTableController) replaceSceneContent("PageDesign3.fxml");
			p3.setApp(this);
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private static Initializable replaceSceneContent(String fxml) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		InputStream in = Main.class.getResourceAsStream(fxml);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(Main.class.getResource(fxml));
		AnchorPane page;
		try {
			page = (AnchorPane) loader.load(in);
		} finally {
			in.close();
		}
		Scene scene = new Scene(page, 800, 600);
		stage.setScene(scene);
		stage.sizeToScene();
		return (Initializable) loader.getController();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
