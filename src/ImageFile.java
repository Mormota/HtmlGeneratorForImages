import java.io.File;

public class ImageFile {
    private File file;
    private String fileName;

    public ImageFile(File file) {
        this.file = file;
        this.fileName = file.getName();
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }
}
