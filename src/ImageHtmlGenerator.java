import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ImageHtmlGenerator {

    public static void main(String[] args) throws IOException {

        if(args.length < 1){
            System.out.println("Hiba adj meg egy elérési utat!");
            return;
        }
        String path = args[0];
        try{
            Paths.get(path);
        }catch(Exception ex){
            System.out.println("Hiba első parancssori argumentumnak egy érvényes elérési útvonalat adj meg!");
        }

        for(String s : args){
            if(s.equals("--clear")){
                MyFileUtils.deleteHtmlFiles(path); //deletes html files
                return;
            }
        }

        ImageFolder imageFolder = new ImageFolder(new File(path));
        MyFileUtils.scanFiles(imageFolder);
        MyFileUtils.generateImageHtml(imageFolder);
        MyFileUtils.createIndexFile(path);
    }



}