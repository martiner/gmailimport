package cz.geek;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

@Data
@AllArgsConstructor
public class Options {
    private String host;
    private String user;
    private String password;
    private String label;
    private List<String> args;

    public static Options parse(PrintStream out, String... args) throws IOException {
        OptionParser parser = new OptionParser();
        @SuppressWarnings("unchecked")
        OptionSpec<String> host = parser.accepts("h", "host").withRequiredArg().ofType(String.class).defaultsTo("imap.gmail.com");
        OptionSpec<String> user = parser.accepts("u", "username").withRequiredArg().required().ofType(String.class);
        OptionSpec<String> pass = parser.accepts("p", "password").withRequiredArg().required().ofType(String.class);
        OptionSpec<String> label = parser.accepts("l", "label").withRequiredArg().ofType(String.class);
        OptionSet options;
        try {
            options = parser.parse(args);
        } catch (OptionException e) {
            parser.printHelpOn(out);
            throw e;
        }
        String l = options.hasArgument(label) ? options.valueOf(label) : null;
        return new Options(options.valueOf(host), options.valueOf(user), options.valueOf(pass), l, options.nonOptionArguments());
    }
}
