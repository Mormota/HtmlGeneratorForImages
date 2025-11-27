import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyFileUtils {
    public static void deleteHtmlFiles(String path){
        //deletes all html files in a given path
        File rootPath = new File(path);
        File[] files =  rootPath.listFiles();
        try{
        for(File file : files){
            if(file.getName().endsWith(".html")){
                boolean delete = file.delete();
                if(delete){
                    System.out.println(file.getName() + " is deleted.");
                }else{
                    System.out.println(file.getName() + " couldn't be deleted.");
                }

            }
        }} catch(Exception ex){
            System.out.println("Hiba a fájl feldolgozása közben: " + ex.getMessage());
            throw ex;
        }
    }
    public static void generateImageHtml(ImageFolder folder) throws IOException {
        List<ImageFile> images = folder.getImages();

        for (int i = 0; i < images.size(); i++) {
            ImageFile img = images.get(i);
            String htmlName = img.getFileName() + ".html";
            File htmlFile = new File(folder.getFolder(), htmlName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(htmlFile))) {
                writer.write("<!DOCTYPE html>\n<html lang=\"hu\">\n<head>\n");
                writer.write("<meta charset=\"UTF-8\">\n");
                writer.write("<title>" + img.getFileName() + "</title>\n");
                writer.write("</head>\n<body>\n");

                // felül navigáció
                writer.write("<div>\n");
                writer.write("<a href=\"index.html\">↑ Start page</a> | ");

                if (i > 0) {
                    writer.write("<a href=\"" + images.get(i-1).getFileName() + ".html\">← Előző</a>");
                } else {
                    writer.write("← Előző");
                }
                writer.write(" | ");
                if (i < images.size()-1) {
                    writer.write("<a href=\"" + images.get(i+1).getFileName() + ".html\">Következő →</a>");
                } else {
                    writer.write("Következő →");
                }
                writer.write("</div>\n");

                // kép és neve
                writer.write("<h1>" + img.getFileName() + "</h1>\n");
                writer.write("<img src=\"" + img.getFileName() + "\" alt=\"" + img.getFileName() + "\">\n");

                writer.write("</body>\n</html>\n");
            }
        }
    }
    //creates an index.html file that combines the html files created from the images
    public static void createIndexFile(String path) {
        File folder = new File(path);
        //Creating an array of html files
        File[] htmlFilesArray = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".html") && !name.equalsIgnoreCase("index.html"));
        if(folder.exists() && folder.isDirectory()){
            File htmlFile = new File(path, "index.html");
            if (htmlFilesArray == null || htmlFilesArray.length == 0) {
                System.out.println("Nincs html fájl a mappában.");
                return;
            }
            //Creating a list of the names of the html files
            List<String> htmlFileNames = new ArrayList<>();
            for (File f : htmlFilesArray) {
                htmlFileNames.add(f.getName());
            }
            //writes html code
            File indexFile = new File(folder, "index.html");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(htmlFile))){
                writer.write("<!DOCTYPE html>\n");
                writer.write("<!DOCTYPE html>\n<html lang=\"hu\">\n<head>\n");
                writer.write("    <meta charset=\"UTF-8\">\n");
                writer.write("    <title>Index</title>\n");
                writer.write("</head>\n<body>\n");
                writer.write("    <h1>Képek indexe</h1>\n<ul>\n");

                for (String htmlName : htmlFileNames) {
                    writer.write("        <li><a href=\"" + htmlName + "\">" + htmlName + "</a></li>\n");
                }

                writer.write("</ul>\n</body>\n</html>");
                System.out.println("index.html létrehozva: " + indexFile.getAbsolutePath());

            } catch (IOException e) {
                System.out.println("Hiba a fájl írásakor: " + e.getMessage());
            }
        } else {
            System.out.println("A megadott út nem létezik vagy nem mappa.");
        }





        }
    //Scans the content of Path into a ImageFolder object
    public static void scanFiles(ImageFolder imageFolder) {
        File folder = imageFolder.getFolder();
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
        for (File f : files) {
            if (f.getName().endsWith("jpg") || f.getName().endsWith("jpeg") || f.getName().endsWith("png")) {
                ImageFile img = new ImageFile(f);
                imageFolder.addImage(img);
                System.out.println(img.getFileName() + " was created");
            }else if(f.isDirectory()){
                ImageFolder subFolder = new ImageFolder(f);
                scanFiles(subFolder);
                System.out.println(f.getName() + "  subfolder was processed");}
        }
        }

        }


}





