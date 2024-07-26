package cz.geek;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class OptionsTest {

    @Test
    void shouldAcceptRequiredArguments() throws Exception {
        Options options = Options.parse(System.out, "-u", "user", "-p", "pass");
        assertEquals("user", options.getUser());
        assertEquals("pass", options.getPassword());
        assertEquals("imap.gmail.com", options.getHost());
        assertNull(options.getLabel());
        assertTrue(options.getArgs().isEmpty());
    }

    @Test
    void shouldAcceptArguments() throws Exception {
        Options options = Options.parse(System.out, "-u", "user", "-p", "pass", "-h", "host", "-l", "label", "file1", "file2");
        assertEquals("user", options.getUser());
        assertEquals("pass", options.getPassword());
        assertEquals("host", options.getHost());
        assertEquals("label", options.getLabel());
        assertEquals(asList("file1", "file2"), options.getArgs());
    }
}
