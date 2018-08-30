package com.SV.timezone;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
/**
 * 
 * @author Saurabh Vaidya
 *
 */
public class ApplicationTest extends TestCase {

	@Test
	public void testConvertToFinalOutput() {
		try {
			Application application = new Application();
			final List<String> input = new ArrayList();
			input.add("2013-07-10 02:52:49,-44.490947,171.220966");
			final List<String> output = new ArrayList();
			output.add("2013-07-10 02:52:49,-44.490947,171.220966,Pacific/Auckland,2013-07-10T14:52:49");
			assertEquals(output,application.findTimeZone(input));

		} catch (Exception e) {
			assertTrue(false);
		}
	}
	@Test
	public void testConvertToUTC() {
		try {
			Application application = new Application();
			assertEquals("2013-07-10T14:52:49",application.convertToUtc("2013-07-10 02:52:49", "Pacific/Auckland").toString());

		} catch (Exception e) {
			assertTrue(false);
		}
	}
}
