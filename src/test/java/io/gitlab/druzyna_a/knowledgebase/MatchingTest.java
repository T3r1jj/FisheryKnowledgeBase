package io.gitlab.druzyna_a.knowledgebase;

import java.time.Instant;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Damian Terlecki
 */
public class MatchingTest {

    @Test
    public void testContains() {
        assertTrue("www.google.com".contains("google"));
    }

    @Test
    public void testMatches() {
        assertFalse("www.google.com".matches("google"));
    }

    @Test
    public void testMatches2() {
        assertTrue("www.google.com".matches(".*google.*"));
    }

    @Test
    public void testTimeMatches() {
        assertTrue(Instant.now().getEpochSecond() == System.currentTimeMillis() / 1000);
    }

}
