package cz.geek;

import cz.geek.resources.FileResource;
import cz.geek.imap.ImapResource;
import cz.geek.resources.Resource;
import cz.geek.resources.ResourceFactory;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ResourceFactoryTest {

    private final ResourceFactory factory = new ResourceFactory();

    @Test
    public void shouldReturnFileResourceForDirectory() {
        final Resource resource = factory.createResource("src");
        assertThat(resource.getClass(), typeCompatibleWith(FileResource.class));
    }

    @Test
    public void shouldReturnFileResourceForNonExistingDirectory() throws Exception {
        final Resource resource = factory.createResource("fooo");
        assertThat(resource.getClass(), typeCompatibleWith(FileResource.class));
    }

    @Test
    public void shouldReturnImapResource() throws Exception {
        final Resource resource = factory.createResource("martin@example.com:password!imap.gmail.com/myfolder");
        assertThat(resource.getClass(), typeCompatibleWith(ImapResource.class));
    }
}