import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageFolder {
    private File folder;
    private List<ImageFile> images;
    private List<ImageFolder> subfolders;

    public ImageFolder(File folder) {
        this.folder = folder;
        this.images = new ArrayList<>();
        this.subfolders = new ArrayList<>();
    }

    public File getFolder() {
        return folder;
    }

    public List<ImageFile> getImages() {
        return images;
    }

    public List<ImageFolder> getSubfolders() {
        return subfolders;
    }

    public void addImage(ImageFile img) {
        images.add(img);
    }

    public void addSubfolder(ImageFolder sub) {
        subfolders.add(sub);
    }
}
