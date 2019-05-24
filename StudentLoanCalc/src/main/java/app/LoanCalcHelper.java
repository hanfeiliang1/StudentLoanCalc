package app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class LoanCalcHelper {

    private double totalAmount, annualInterestRate, monthlyInterestRate, totalPayment, additionalPayment;
    private int years, numberOfMonths;
    private LocalDate startDate;
    private PaymentDetail[] paymentDetails;

    public void calculateLoan(double totalAmount, int years, double annualInterestRate, LocalDate startDate, double additionalPayment) {
        this.totalAmount = totalAmount;
        this.years = years;
        this.annualInterestRate = annualInterestRate;
        this.numberOfMonths = years * 12;
        this.monthlyInterestRate = annualInterestRate / 12;
        this.startDate = startDate;
        this.additionalPayment = additionalPayment;
        this.totalPayment = 0;

        this.calculateLoan();
    }


    public void reset() {
        this.totalPayment = 0;
        this.totalAmount = 0;
        this.numberOfMonths = 0;
        this.years = 0;
        this.annualInterestRate = 0;
        this.monthlyInterestRate = 0;
        this.startDate = null;
        this.additionalPayment = 0;
        this.paymentDetails = null;
    }

    public double getMonthlyPayment() {
        return roundNumber(totalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfMonths) /
                (Math.pow(1 + monthlyInterestRate, numberOfMonths) - 1), 2);
    }

    public double getTotalPayment() {
        return roundNumber(totalPayment, 2);
    }

    public double getTotalInterest() {
        return roundNumber(getTotalPayment() - totalAmount, 2);
    }

    public double getMonthlyRate() {
        return roundNumber(monthlyInterestRate, 5);
    }

    public PaymentDetail[] getMonthlyPaymentDetails() {
        return Arrays.copyOfRange(paymentDetails, 1, numberOfMonths + 2);
    }

    private double roundNumber(double number, int digits) {
        BigDecimal bd= new BigDecimal(number);
        return bd.setScale(digits, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private void calculateLoan() {
        double monthlyInterestRate = annualInterestRate / 12;
        int numberOfMonths = years * 12;
        paymentDetails = new PaymentDetail[numberOfMonths + 1];
        double monthlyPayment = getMonthlyPayment();

        double interestOfNthMonth, principle, balance;

        // dummy payment detail
        paymentDetails[0] = new PaymentDetail(0, startDate.minusMonths(1), monthlyPayment, 0, 0, 0, totalAmount);
        for (int count = 1; count <= numberOfMonths; count++) {
            double previousBalance = Double.parseDouble(paymentDetails[count - 1].balance.getValue());
            if (previousBalance == 0) {
                break;
            }
            interestOfNthMonth =  previousBalance * monthlyInterestRate;
            if (previousBalance * (1 + monthlyInterestRate) < monthlyPayment) {
                monthlyPayment = previousBalance * (1 + monthlyInterestRate);
            }
            principle = monthlyPayment - interestOfNthMonth + additionalPayment;
            if (previousBalance - principle <= 0) {
                additionalPayment = previousBalance - monthlyPayment + interestOfNthMonth;
                principle = previousBalance;
                balance = 0;
            } else {
                balance = previousBalance - principle;
            }
            this.totalPayment += monthlyPayment + additionalPayment;
            LocalDate dueDate = startDate.plusMonths(count);
            paymentDetails[count] = new PaymentDetail(count + 1, dueDate, roundNumber(monthlyPayment, 2), roundNumber(additionalPayment, 2), roundNumber(interestOfNthMonth, 2), roundNumber(principle, 2), roundNumber(balance, 2));
        }
    }
}
