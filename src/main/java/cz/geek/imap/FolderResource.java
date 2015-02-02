package cz.geek.imap;

import cz.geek.resources.Resource;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

public class FolderResource implements Resource {

    private final ImapHelper imapHelper;
    private final Folder folder;

    public FolderResource(final ImapHelper imapHelper, final Folder folder) {
        this.imapHelper = imapHelper;
        this.folder = folder;
    }

    @Override
    public String name() {
        return folder.getName();
    }

    @Override
    public void check() {
    }

    @Override
    public List<Resource> listResources() {
        try {
            final Folder[] folders = folder.list();
            final List<Resource> result = new ArrayList<Resource>();
            for (Folder folder: folders) {
                result.add(new FolderResource(imapHelper, folder));
            }
            return result;
        } catch (MessagingException e) {
            throw new ImapException("Unable to list folders of " + name(), e);
        }
    }

    @Override
    public List<Message> list() {
        final List<Message> result = new ArrayList<Message>();
        try {
            for (Message message: folder.getMessages()) {
                result.add(message);
            }
        } catch (MessagingException e) {
            throw new ImapException("Unable to list messages in  " + name(), e);
        }

        return result;
    }

    @Override
    public Resource createResource(final String name) {
        return new FolderResource(imapHelper, imapHelper.getFolder(folder, name));
    }

    @Override
    public void copy(final Message msg) {
        Channels.writa
        msg.writeTo();
        imapHelper.importMessage();
    }


}
