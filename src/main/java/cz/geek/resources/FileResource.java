package cz.geek.resources;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * File or directory resource
 */
public class FileResource implements Resource {

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

    private final File file;

    public FileResource(final File file) {
        this.file = file;
    }

    @Override
    public String name() {
        return file.getName();
    }

    @Override
    public void check() {
        if (!file.exists()) {
            throw new BadResourceException(file.getAbsoluteFile() + " doesn't exist");
        }
    }

    @Override
    public List<Resource> listResources() {
        final File[] files = file.listFiles(DIR_FILTER);
        Arrays.sort(files);
        final List<Resource> result = new ArrayList<Resource>();
        for (File dir: files) {
            result.add(new FileResource(dir));
        }
        return result;
    }

    /*
    			File file = new File(i);
			if (file.isDirectory())
				app.doImport(file, file, options.valueOf(target), true);
			else if (file.isFile()) {
				String folder = options.hasArgument(target) ? options.valueOf(target) : "Inbox";
				app.importMessage(app.getFolder(folder), file);
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
    */


    private File[] listFiles(File dir) {
        File[] files = dir.listFiles(FILE_FILTER);
        Arrays.sort(files);
        return files;
    }

}
