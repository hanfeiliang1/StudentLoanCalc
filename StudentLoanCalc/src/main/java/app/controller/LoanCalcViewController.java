package app.controller;

import app.LoanCalcHelper;
import app.PaymentDetail;
import app.StudentCalc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.tuple.PropertyFactory;


public class LoanCalcViewController implements Initializable   {

	private StudentCalc SC = null;

	private LoanCalcHelper loanCalcHelper = null;
	
	@FXML
	private TextField LoanAmount;

	@FXML
	private TextField InterestRate;

	@FXML
	private TextField NbrOfYears;
	
	@FXML
	private DatePicker PaymentStartDate;

	@FXML
	private TextField AdditionalPayment;

	@FXML
	private Label lblTotalPayments;

	@FXML
	private Label lblTotalInterest;

	@FXML
	private Label ratePerMonth;

	@FXML
	private TableView<PaymentDetail> tableView;

	@FXML
	public TableColumn<PaymentDetail, String> paymentId, dueDate, payment, additionalPayment, interest, principle, balance;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		paymentId.setCellValueFactory(new PropertyValueFactory<>("PaymentId"));
		dueDate.setCellValueFactory(new PropertyValueFactory<>("DueDate"));
		payment.setCellValueFactory(new PropertyValueFactory<>("Payment"));
		additionalPayment.setCellValueFactory(new PropertyValueFactory<>("AdditionalPayment"));
		interest.setCellValueFactory(new PropertyValueFactory<>("Interest"));
		principle.setCellValueFactory(new PropertyValueFactory<>("Principle"));
		balance.setCellValueFactory(new PropertyValueFactory<>("Balance"));
	}

	public void setMainApp(StudentCalc sc) {
		this.SC = sc;
	}

	public void setCalcHelper(LoanCalcHelper lc) {
		this.loanCalcHelper = lc;
	}
	
	/**
	 * btnCalcLoan - Fire this event when the button clicks
	 * 
	 * @version 1.0
	 * @param event
	 */
	@FXML
	private void btnCalcLoan(ActionEvent event) {

		double dLoanAmount = Double.parseDouble(LoanAmount.getText());
		double annualInterestRate = Double.parseDouble(InterestRate.getText().replace('%', ' ').trim()) / 100.0;
		int numberOfYears = Integer.parseInt(NbrOfYears.getText());
		double additionalPayment = Double.parseDouble(AdditionalPayment.getText());
		LocalDate startDate = PaymentStartDate.getValue();

		loanCalcHelper.calculateLoan(dLoanAmount, numberOfYears, annualInterestRate, startDate, additionalPayment);

		PaymentDetail[] paymentDetails = loanCalcHelper.getMonthlyPaymentDetails();

		double totalPayments = loanCalcHelper.getTotalPayment();
		lblTotalPayments.setText("" + totalPayments);

		double totalInterest = loanCalcHelper.getTotalInterest();
		lblTotalInterest.setText("" + totalInterest);

		double monthlyRate = loanCalcHelper.getMonthlyRate();
		ratePerMonth.setText(monthlyRate + "%");

		ObservableList<PaymentDetail> paymentDetailsList = FXCollections.observableArrayList();
		 for (PaymentDetail pd: paymentDetails) {
		 	paymentDetailsList.addAll(pd);
		 }
		 tableView.setItems(paymentDetailsList);

		loanCalcHelper.reset();
	}

	@FXML
	private void btnReset(ActionEvent event) {
		LoanAmount.clear();
		InterestRate.clear();
		NbrOfYears.clear();
		PaymentStartDate.setValue(null);
		tableView.getItems().clear();
		loanCalcHelper.reset();
		lblTotalPayments.setText("");
		lblTotalInterest.setText("");
		ratePerMonth.setText("");
	}
}
