package edu.unimelb.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartApplication extends Application
{
	
	private Stage stage;
	
	public static StartApplication instance;
	
	public static MainController mainController;

	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		stage = primaryStage;
        stage.setTitle("Share Whiteboard");
        stage.setResizable(false);
        gotoMain();
        stage.show();
	}
	

	
	public StartApplication() {
		instance = this;
	}
	
	public static MainController getController() {
		return mainController;
	}
	
	public static StartApplication getInstance() {
		return instance;
	}
	
	
	
	public void gotoMain() {
		try {
			replaceSceneContent("MAIN.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private Parent replaceSceneContent(String fxml) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml), null, new JavaFXBuilderFactory());
		Parent page = loader.load();
		mainController = (MainController)loader.getController();
		//System.out.println(mainController);
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setScene(scene);
        } 
        else {
        	scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        return page;
	}
	
	public static void main(String[] args) {
        launch(args);
    }

}
