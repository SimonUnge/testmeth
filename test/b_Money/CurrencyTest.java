package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals("SEK", SEK.getName());
	}
	
	@Test
	public void testGetRate() {
		assertEquals(0.15, SEK.getRate(), 0.0001);
	}
	
	@Test
	public void testSetRate() {
		SEK.setRate(0.3);
		assertEquals(0.3, SEK.getRate(), 0.0001);		
	}
	
	@Test
	public void testGlobalValue() {
		assertEquals((Integer)15, SEK.universalValue(100));
	}
	
	@Test
	public void testValueInThisCurrency() {
		assertEquals(133 ,(int)SEK.valueInThisCurrency(100, DKK));
	}

}
