package app;

import java.io.IOException;

import app.controller.LoanCalcViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StudentCalc extends Application {

	private Stage primaryStage = null;
	
	private BorderPane LoanScreen = null;
	
	private LoanCalcViewController LCVC = null;

	private LoanCalcHelper loanCalcHelper = null;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		ShowScreen();
		
	}
	
	public void ShowScreen() {
		// Parent root;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader = new FXMLLoader(getClass()
					.getResource("/app/view/LoanCalcView.fxml"));
			LoanScreen = loader.load();
			Scene scene = new Scene(LoanScreen);
			primaryStage.setTitle("Student Loan Calculator");
			primaryStage.setScene(scene);
			LCVC = loader.getController();
			LCVC.setMainApp(this);
			LCVC.setCalcHelper(new LoanCalcHelper());
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
