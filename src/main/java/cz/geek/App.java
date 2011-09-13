package cz.geek;


import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;

public class App {

	private Session session;
	private Store store;
	private File root;
	private static final String ALL_MAIL = "[Gmail]/All Mail";

	public App(String host, String user, String password, File root) throws MessagingException {
		Properties prop = new Properties();
		prop.setProperty("mail.store.protocol", "imaps");
		session = Session.getInstance(prop);
		store = session.getStore();
		store.connect(host, user, password);
		if (!root.exists())
			throw new IllegalArgumentException("Root dir must exist: " + root);
		this.root = root;
	}

	private static final FileFilter FILE_FILTER = new FileFilter() {
		public boolean accept(File file) {
			return file.isFile() && file.getName().endsWith(".eml") && file.length() > 0;
		}
	};

	private static final FileFilter DIR_FILTER = new FileFilter() {
		public boolean accept(File file) {
			return file.isDirectory();
		}
	};

	public static void main( String[] args ) throws Exception {
		File root = new File("todo/Inbox");
		App app = new App("imap.gmail.com",
                "", "",
				root
        );
		//app.listFolder("[Gmail]/Sent Mail");
		//app.listFolder("[Gmail]/All Mail");
		//app.listFolder("[Gmail]/Drafts");

        app.doImport(root, true);
	}

	public void doImport(File dir, String folder, boolean recursive) {
	    if (folder == null)
		    folder = folderName(dir);
	    Folder fldr = getFolder(folder);
        for (File f: listFiles(dir)) {
            System.out.println(f.getName());
            try {
                importMessage(fldr, f);
            } catch (FileNotFoundException e) {
                // should not happen
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
		if (recursive)
			for (File d: listDirs(dir))
				doImport(d, recursive);
    }

	public void doImport(File dir, boolean recursive) {
		doImport(dir, null, recursive);
	}

	public void doImport(File dir) {
		doImport(dir, null, false);
	}

	public void importMessage(Folder folder, File file) throws FileNotFoundException, MessagingException {
		FileInputStream is = new FileInputStream(file);
		MimeMessage msg = new MimeMessage(session, is);
		folder.appendMessages(new Message[]{msg});
	}

	private String folderName(File dir) {
		if (root.equals(dir))
			return ALL_MAIL;
		return dir.getAbsolutePath().substring(root.getAbsolutePath().length() + 1);
	}

	private void listFolder(String name) throws MessagingException {
		Folder folder = getFolder(name);
		for (Message m: folder.getMessages()) {
			System.out.println(m.getSubject());
		}
	}

	private Folder getFolder(String name) {
		try {
			Folder folder = store.getFolder(name);
			if (!folder.exists())
				folder.create(Folder.HOLDS_MESSAGES);
			folder.open(Folder.READ_WRITE);
			return folder;
		} catch (MessagingException e) {
			throw new RuntimeException("Can not get folder: " + name, e);
		}
	}

	private File[] listFiles(File dir) {
		File[] files = dir.listFiles(FILE_FILTER);
		Arrays.sort(files);
		return files;
	}

	private File[] listDirs(File dir) {
		File[] files = dir.listFiles(DIR_FILTER);
		Arrays.sort(files);
		return files;
	}

}
