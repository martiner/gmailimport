package cz.geek;

import cz.geek.resources.Msg;
import cz.geek.resources.Resource;
import cz.geek.resources.ResourceFactory;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GmailImport {

    private final Resource target;
    private final List<Resource> sources;

	public GmailImport(final Resource target, final List<Resource> sources) {
        this.target = target;
        this.sources = sources;
    }

    public GmailImport(final List<String> arguments) {
        if (arguments.size() < 2) {
            throw new IllegalArgumentException("at least two arguments");
        }
        final ResourceFactory factory = new ResourceFactory();
        target = factory.createResource(arguments.get(0));
        sources = new ArrayList<Resource>(arguments.size());
        for (String resource: arguments.subList(1, arguments.size())) {
            sources.add(factory.createResource(resource));
        }
    }

    public void checkResources() {
        target.check();
        for (Resource resource: sources) {
            resource.check();
        }
    }

    public void listResources() {
        checkResources();
        target.listResources();
        for (Resource resource: sources) {
            resource.listResources();
        }
    }

    public void copy() {
        for (Resource source: sources) {
            copy(source, target);
        }
    }

    private void copy(final Resource source, final Resource target) {
        for (Msg msg: source.list()) {
            target.copy(msg);
        }
        for (Resource resource: source.listResources()) {
            final Resource created = target.createResource(resource.name());
            copy(resource, created);
        }
    }

    public static void main(String... args) throws Exception {
        final OptionParser parser = new OptionParser();
		@SuppressWarnings("unchecked")
		final OptionSpec<Action> action = parser.accepts("a", "action").withRequiredArg().ofType(Action.class).defaultsTo(Action.check);
		try {
            final OptionSet options = parser.parse(args);
            final List<String> arguments = options.nonOptionArguments();

            try {
                final GmailImport app = new GmailImport(arguments);
                switch (options.valueOf(action)) {
                    case check:
                        app.checkResources();
                        System.out.println("OK");
                        break;
                    case list:
                        app.listResources();
                        System.out.println("Listed");
                        break;
                }
            } catch (IllegalArgumentException e) {
                throw new OptionException(Collections.<String>emptyList()) {
                    @Override
                    public String getMessage() {
                        return "Expected one target and at least one source";
                    }
                };
            }
		} catch (OptionException e) {
            System.err.println(e.getMessage());
			parser.printHelpOn(System.err);
			System.exit(1);
		}

        //app.listFolder("[Gmail]/Sent Mail");
		//app.listFolder("[Gmail]/All Mail");
		//app.listFolder("[Gmail]/Drafts");
	}

}
