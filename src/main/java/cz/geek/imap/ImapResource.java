package cz.geek.imap;

import cz.geek.resources.BadResourceException;
import cz.geek.resources.Resource;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class ImapResource implements Resource {

    static final String ALL_MAIL = "[Gmail]/All Mail";
    private final String host;
    private final String user;
    private final String password;
    private final String folder;

    private ImapHelper imapHelper;

    public ImapResource(String host, String user, String password, String folder) {
        this.host = host != null ? host : "imap.gmail.com";
        this.user = user;
        this.password = password;
        this.folder = folder;
    }

    @Override
    public String name() {
        return folder;
    }

    @Override
    public void check() {
        try {
            imapHelper = new ImapHelper(host, user, password);
        } catch (ImapException e) {
            throw new BadResourceException("Unable to login user " + user + " to " + host, e);
        }
    }

    @Override
    public List<Resource> listResources() {
        final List<Resource> result = new ArrayList<Resource>();
        for (Folder folder: imapHelper.listFolder(this.folder)) {
            result.add(new FolderResource(imapHelper, folder));
        }
        return result;
    }


    /*public void importMessage(Folder folder, File file) {
        System.out.println(file.getName());
        try {
            FileInputStream is = new FileInputStream(file);
            importMessage(folder, is);
        } catch (FileNotFoundException e) {
            // should not happen
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }*/

}
