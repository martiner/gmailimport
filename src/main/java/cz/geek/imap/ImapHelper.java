package cz.geek.imap;

import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class ImapHelper {

    public static final String PROTOCOL = "imaps";
    private Session session;

	private Store store;

    public ImapHelper(String host, String user, String password) {
        Properties prop = new Properties();
		prop.setProperty("mail.store.protocol", PROTOCOL);
		session = Session.getInstance(prop);
        try {
            store = session.getStore();
            store.connect(host, user, password);
        } catch (NoSuchProviderException e) {
            throw new ImapException("Protocol " + PROTOCOL + " is not supported", e);
        } catch (AuthenticationFailedException e) {
            throw new ImapException("Bad username or password", e);
        } catch (MessagingException e) {
            throw new ImapException("Messaging exception", e);
        }
    }

	public void importMessage(Folder folder, File file) {
		System.out.println(file.getName());
		try {
			FileInputStream is = new FileInputStream(file);
			importMessage(folder, is);
		} catch (FileNotFoundException e) {
		    // should not happen
		}
	}

	public void importMessage(Folder folder, InputStream is) {
        try {
            MimeMessage msg = new MimeMessage(session, is);
            folder.appendMessages(new Message[]{msg});
        } catch (MessagingException e) {
            throw new ImapException("Unable to copy message");
        }
    }

	private String folderName(File dir, File root) {
		if (root.equals(dir))
			return ImapResource.ALL_MAIL;
		return dir.getAbsolutePath().substring(root.getAbsolutePath().length() + 1);
	}

	public Folder[] listFolder(String name) {
        try {
            return (name != null ? store.getFolder(name) : store.getDefaultFolder()).list();
        } catch (MessagingException e) {
            throw new ImapException("Unable to list folders of " + (name != null ? name : "default folder"), e);
        }
		/*Folder folder = getFolder(name);
        try {
            for (Message m: folder.getMessages()) {
            //m.writeTo();
                System.out.println(m.getSubject());
            }
        } catch (MessagingException e) {
            throw new ImapException("Unable to list folder " + folder, e);
        }*/
    }

    public Folder getFolder(String name) {
        try {
            Folder folder = store.getFolder(name);
            if (!folder.exists()) {
                if (name.startsWith("[Gmail]"))
                    throw new RuntimeException("Implicit folder should exist: " + name);
                folder.create(Folder.HOLDS_MESSAGES);
            }
            folder.open(Folder.READ_WRITE);
            return folder;
        } catch (MessagingException e) {
            throw new ImapException("Can not get folder: " + name, e);
        }
    }

    public Folder getFolder(Folder parent, String name) {
        try {
            Folder folder = parent.getFolder(name);
            if (!folder.exists()) {
                folder.create(Folder.HOLDS_MESSAGES);
            }
            folder.open(Folder.READ_WRITE);
            return folder;
        } catch (MessagingException e) {
            throw new ImapException("Can not get folder: " + name, e);
        }
    }

}
