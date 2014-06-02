package awong.impatient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

import scala.impatient.Exercises15.Deprecated;

public class Exercises15JUnitTest {
	@Ignore
	public void rigorous_test() {
		assertTrue(true);
		assertFalse(false);
		assertEquals("foo" , new String("foo"));
		assertNotNull("foo");
		assertNull(null);
	}
	@Test
	public void testScala() {
		Deprecated dep = new Deprecated("hello");
		List<Integer> list = Lists.newArrayList(1,2,3);
		assertEquals(new Integer(6), dep.sum(list));
		dep.linesOfFilename("my file");
	}

}


