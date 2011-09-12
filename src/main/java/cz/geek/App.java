package cz.geek;


import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class App {

	private Session session;
	private Store store;

	public App(String host, String user, String password) throws MessagingException {
		Properties prop = new Properties();
		prop.setProperty("mail.store.protocol", "imaps");
		session = Session.getInstance(prop);
		store = session.getStore();
		store.connect(host, user, password);
	}

	private static final FileFilter FILE_FILTER = new FileFilter() {
		public boolean accept(File pathname) {
			return pathname.isFile();
		}
	};

	private static final FileFilter DIR_FILTER = new FileFilter() {
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	};

	public static void main( String[] args ) throws Exception {
		App app = new App("imap.gmail.com", "", "");
		for (File d: app.listDirs(new File(".")))
			System.out.println(d.getName());
		//app.listFolder();
		//app.importMessage(app.getFolder("xxx"), new File("email.eml"));
	}

	private void importMessage(Folder folder, File file) throws FileNotFoundException, MessagingException {
		FileInputStream is = new FileInputStream(file);
		MimeMessage msg = new MimeMessage(session, is);
		folder.appendMessages(new Message[]{msg});
	}

	private void listFolder() throws MessagingException {
		Folder folder = getFolder("[Gmail]/All Mail");
		for (Message m: folder.getMessages()) {
			System.out.println(m.getSubject());
		}
	}

	private Folder getFolder(String name) throws MessagingException {
		Folder folder = store.getFolder(name);
		if (!folder.exists())
			folder.create(Folder.HOLDS_MESSAGES);
		folder.open(Folder.READ_WRITE);
		return folder;
	}

	private File[] listFiles(File dir) {
		return dir.listFiles(FILE_FILTER);
	}

	private File[] listDirs(File dir) {
		return dir.listFiles(DIR_FILTER);
	}

}
