package pkgUT;

import static org.junit.Assert.*;

import app.LoanCalcHelper;
import org.apache.poi.ss.formula.functions.*;
import org.junit.Test;

import java.time.LocalDate;

public class TestLoanCalcHelper {

	@Test
	public void testExample() {
		double PMT;
		double r = 0.07 / 12;
		double n = 20 * 12;
		double p = 150000;
		double f = 0;
		boolean t = false;
		PMT = Math.abs(FinanceLib.pmt(r, n, p, f, t));
		
		double PMTExpected = 1162.95;
		assertEquals(PMTExpected, PMT, 0.01);
		
	}

	@Test
	public void testGetMonthlyPayment() {
		double PMT;
		double r = 0.07 / 12;
		int n = 15 * 12;
		double p = 200000;
		double f = 0;
		boolean t = false;
		PMT = Math.abs(FinanceLib.pmt(r, n, p, f, t));

		LoanCalcHelper loanCalcHelper = new LoanCalcHelper();
		loanCalcHelper.calculateLoan(p, n / 12, r * 12, LocalDate.now(), 0);

		double totalPayment = loanCalcHelper.getMonthlyPayment();

		double PMTExpected = 1797.66;
		assertEquals(PMTExpected, PMT, 0.01);
		assertEquals(PMTExpected, totalPayment, 0.01);
	}

	@Test
	public void testGetTotalPayment() {
		double r = 0.07 / 12;
		int n = 15 * 12;
		double p = 200000;

		LoanCalcHelper loanCalcHelper = new LoanCalcHelper();
		loanCalcHelper.calculateLoan(p, n / 12, r * 12, LocalDate.now(), 0);

		double totalPayment = loanCalcHelper.getTotalPayment();

		double totalExpected =  323577.84;
		assertEquals(totalExpected, totalPayment, 0.01);
	}

}

 

