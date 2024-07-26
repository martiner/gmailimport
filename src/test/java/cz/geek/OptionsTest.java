package cz.geek;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OptionsTest {

    @Test
    void shouldAcceptRequiredArguments() throws Exception {
        Options options = Options.parse(System.out, "-u", "user", "-p", "pass");
        assertThat(options.getUser()).isEqualTo("user");
        assertThat(options.getPassword()).isEqualTo("pass");
        assertThat(options.getHost()).isEqualTo("imap.gmail.com");
        assertThat(options.getLabel()).isNull();
        assertThat(options.getArgs()).isEmpty();
    }

    @Test
    void shouldAcceptArguments() throws Exception {
        Options options = Options.parse(System.out, "-u", "user", "-p", "pass", "-h", "host", "-l", "label", "file1", "file2");
        assertThat(options.getUser()).isEqualTo("user");
        assertThat(options.getPassword()).isEqualTo("pass");
        assertThat(options.getHost()).isEqualTo("host");
        assertThat(options.getLabel()).isEqualTo("label");
        assertThat(options.getArgs()).containsExactly("file1", "file2");
    }
}
