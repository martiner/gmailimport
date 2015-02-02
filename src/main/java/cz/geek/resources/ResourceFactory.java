package cz.geek.resources;

import cz.geek.GeekMailCopyException;
import cz.geek.imap.ImapResource;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceFactory {

    private static final Pattern PATTERN = Pattern.compile("(?<name>.+?)(:(?<pass>.*))?(!(?<host>.*))?(/(?<folder>.*))?");

    public Resource createResource(String resource) {
        final File file = new File(resource);
        if (file.exists()) {
            return new FileResource(file);
        }
        final Matcher matcher = PATTERN.matcher(resource);
        if (!matcher.matches()) {
            throw new GeekMailCopyException("Resource syntax not valid: " + resource);
        }
        final String name = matcher.group("name");
        final String pass = matcher.group("pass");
        final String host = matcher.group("host");
        final String folder = matcher.group("folder");

        if (pass == null && host == null && folder == null) {
            return new FileResource(new File(name));
        }
        return new ImapResource(host, name, pass, folder);

        //throw new GeekMailCopyException("Unknown resource: " + resource);
    }
}
