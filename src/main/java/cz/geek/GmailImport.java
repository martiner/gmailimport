package cz.geek;

import joptsimple.OptionException;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Arrays;
import java.util.Properties;

import static java.util.Objects.requireNonNullElse;

public class GmailImport {

	private Session session;
	
	private Store store;

	private static final String ALL_MAIL = "[Gmail]/All Mail";

	public GmailImport(String host, String user, String password) throws MessagingException {
		Properties prop = new Properties();
		prop.setProperty("mail.store.protocol", "imaps");
		session = Session.getInstance(prop);
		store = session.getStore();
		store.connect(host, user, password);
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

	public static void main(String... args) throws Exception {
		Options options = null;
		try {
			options = Options.parse(System.out, args);
		} catch (OptionException e) {
			System.exit(1);
		}

		GmailImport app = new GmailImport(options.getHost(), options.getUser(), options.getPassword());
		for (String i: options.getArgs()) {
			File file = new File(i);
			if (file.isDirectory())
				app.doImport(file, file, options.getLabel(), true);
			else if (file.isFile()) {
				String folder = requireNonNullElse(options.getLabel(), "Inbox");
				app.importMessage(app.getFolder(folder), file);
			}
		}
		//app.listFolder("[Gmail]/Sent Mail");
		//app.listFolder("[Gmail]/All Mail");
		//app.listFolder("[Gmail]/Drafts");
	}

	public void doImport(File dir, File root, String folder, boolean recursive) {
		File[] files = listFiles(dir);
		File[] dirs = recursive ? listDirs(dir) : new File[]{};

		if (files.length > 0 || dirs.length > 0) {
			if (folder == null)
				folder = folderName(dir, root);
			Folder fldr = getFolder(folder);
			for (File f: files)
	            importMessage(fldr, f);
			try {
				fldr.close(false);
			} catch (Exception e) {
				// next time will be better :)
			}
		}
		if (recursive)
			for (File d: dirs)
				doImport(d, root, null, recursive);
    }

	public void importMessage(Folder folder, File file) {
		System.out.println(file.getName());
		try {
			FileInputStream is = new FileInputStream(file);
			importMessage(folder, is);
		} catch (FileNotFoundException e) {
		    // should not happen
		} catch (MessagingException e) {
		    e.printStackTrace();
		}
	}

	public void importMessage(Folder folder, InputStream is) throws MessagingException {
		MimeMessage msg = new MimeMessage(session, is);
		folder.appendMessages(new Message[]{msg});
	}

	private String folderName(File dir, File root) {
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
			if (!folder.exists()) {
				if (name.startsWith("[Gmail]"))
					throw new RuntimeException("Implicit folder should exist: " + name);
				folder.create(Folder.HOLDS_MESSAGES);
			}
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
