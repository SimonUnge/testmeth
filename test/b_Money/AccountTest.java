package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK, EUR;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("SEK", 0.2);
		EUR = new Currency("SEK", 1.5);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		assertFalse(testAccount.timedPaymentExists("rent"));
		
		testAccount.addTimedPayment("rent", 30, 0, new Money(10000, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("rent"));
		
		testAccount.removeTimedPayment("rent");
		assertFalse(testAccount.timedPaymentExists("rent"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		Integer a;
		
		a = testAccount.getBalance().getAmount();
		assertEquals(10000000, a);
		a = SweBank.getBalance("Alice");
		assertEquals(1000000, a);

		testAccount.addTimedPayment("rent", 0, 0, new Money(10000, SEK), SweBank, "Alice");
		for (int i = 0; i < 10; ++i)
		{
			testAccount.tick();
		}
		
		a = testAccount.getBalance().getAmount();
		// Expected 9900000 but got 9800000! Let's check the code for tick() too see if we can find some error!
		assertEquals(10000000 - 100000, a);
		a = SweBank.getBalance("Alice");
		assertEquals(1000000 + 100000, a);
		
		testAccount.addTimedPayment("rent", 0, 0, new Money(10000, SEK), SweBank, "AlasdfXXXice");
		a = testAccount.getBalance().getAmount();
		assertEquals(10000000 - 100000, a);
	}

	@Test
	public void testAddWithdraw() {
		Integer a;
		
		a = testAccount.getBalance().getAmount();
		assertEquals(10000000, a);

		testAccount.deposit(new Money(1000, SEK));
		a = testAccount.getBalance().getAmount();
		assertEquals(10001000, a);

		testAccount.withdraw(new Money(1000, SEK));
		a = testAccount.getBalance().getAmount();
		assertEquals(10000000, a);

		testAccount.withdraw(new Money(10000000, SEK));
		a = testAccount.getBalance().getAmount();
		assertEquals(0, a);

		testAccount.deposit(new Money(1000, DKK));
		a = testAccount.getBalance().getAmount();
		assertEquals(1333, a);
	}
	
	@Test
	public void testGetBalance() {
		assertTrue((new Money(10000000, SEK)).equals(testAccount.getBalance()));
	}
}
