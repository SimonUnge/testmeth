package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SweBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		try
		{
			SweBank.openAccount("Bob");
			fail("Should not get here.");
		}
		catch (AccountExistsException e)
		{
		}
		
		SweBank.openAccount("babababa");
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		SweBank.deposit("Bob", new Money(10000, SEK));
		Integer bal = SweBank.getBalance("Bob");
		assertEquals(10000, bal);

		try
		{
			SweBank.deposit("Bobbbbbbbbb", new Money(10000, SEK));
			fail("Should not get here.");
		}
		catch (AccountDoesNotExistException e)
		{
		}
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		SweBank.deposit("Bob", new Money(10000, SEK));
		SweBank.withdraw("Bob", new Money(5000, SEK));
		Integer newBal = SweBank.getBalance("Bob");
		assertEquals(5000, newBal);

		try
		{
			SweBank.withdraw("Bobbbbbbbbb", new Money(10000, SEK));
			fail("Should not get here.");
		}
		catch (AccountDoesNotExistException e)
		{
		}
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		SweBank.deposit("Bob", new Money(10000, SEK));
		Integer b = SweBank.getBalance("Bob");
		assertEquals(10000, b);

		try
		{
			SweBank.getBalance("Bobbbbbbbb");
			fail("Should not get here.");
		}
		catch (AccountDoesNotExistException e)
		{
		}
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		Integer b;

		SweBank.deposit("Bob", new Money(10000, SEK));
		b = SweBank.getBalance("Bob");
		assertEquals(10000, b);
		b = SweBank.getBalance("Ulrika");
		assertEquals(0, b);
		
		SweBank.transfer("Bob", "Ulrika", new Money(5000, SEK));
		b = SweBank.getBalance("Bob");
		assertEquals(5000, b);
		b = SweBank.getBalance("Ulrika");
		assertEquals(5000, b);

		SweBank.deposit("Bob", new Money(5000, SEK));
		b = SweBank.getBalance("Bob");
		assertEquals(10000, b);
		b = DanskeBank.getBalance("Gertrud");
		assertEquals(0, b);
		
		SweBank.transfer("Bob", DanskeBank, "Gertrud", new Money(5000, SEK));
		b = SweBank.getBalance("Bob");
		assertEquals(5000, b);
		b = DanskeBank.getBalance("Gertrud");
		assertEquals(3750, b);
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		/*
		try
		{
			SweBank.addTimedPayment("Bobbbbbb", "rent", 30, 100, new Money(5000, SEK), DanskeBank, "Gertrud");
			fail("Should not get here.");
		}
		catch (AccountDoesNotExistException e)
		{
		}
		*/
		SweBank.addTimedPayment("Bob", "rent", 30, 100, new Money(5000, SEK), DanskeBank, "Gertrud");
		SweBank.removeTimedPayment("Bob", "rent");
	}
}
