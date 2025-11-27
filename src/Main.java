

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final List<String> VALID = Arrays.asList("jpg", "jpeg", "png");
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Hibas bemenet!");
            System.out.println("java htmlgenerator <path>");
            System.exit(1);
        }

        String foldername = args[0];
        File owner = null;
        try {
            owner = new File(foldername);
            if (!owner.exists()) {
                throw new IOException("Input not found!");
            }
        } catch (Exception e) {
            System.out.println("Nem talalhato utvonal, kerlek ellenorizd a parametert!");
            System.exit(1);
        }
        traverse(owner, 0);
    }

    public static void traverse(File owner, int index) {
        File[] children = owner.listFiles();
        if (children == null) {
            return;
        }
        List<File> directories = new ArrayList<>();
        List<File> images = new ArrayList<>();
        for (File child : children) {
            if (child.isDirectory()) {
                directories.add(child);
            } else {
                String[] tmp = child.getName().split("\\.");
                if (tmp.length == 2 && VALID.contains(tmp[1].toLowerCase())) {
                    images.add(child);
                }
            }
        }
        for (int i = 0; i < images.size(); i++) {
            File image = images.get(i);
            System.out.println(image.getName());
            File previous = i == 0 ? image : images.get(i - 1);
            File next = i + 1 == images.size() ? image : images.get(i + 1);
            String path = image.getPath().split("\\.")[0] + ".html";
            genImageHtml(
                    new File(path),
                    "Kep-" + i,
                    previous.getName(),
                    next.getName(),
                    image.getName(),
                    index
            );
        }
        genDirectoryHtml(owner, directories, images, index);
        for (File directory : directories) {
            System.out.println(directory.getName());
            traverse(directory, index + 1);
        }
    }

    public static void genImageHtml(File owner, String title, String previous, String next, String image, int index) {
        String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <title>Képgaléria</title>
                </head>
                <body>
                  <h1><a href="{path}.html">Fooldal</a></h1>
                  <h1><a href="index.html">Vissza</a></h1>
                  <h1>Képgaléria</h1>
                                
                  <div>
                    <a href="{prev_img}.html">Előző kép</a>
                   \s
                    <img src="{image}" alt="{title}" width="400" height="300">
                               
                    <a href="{next_img}.html">Következő kép</a>
                  </div>
                                
                  <p>{title}</p>
                </body>
                </html>
                """;
        html = getRootSection(index, html);
        html = html.replace("{prev_img}", previous.split("\\.")[0]);
        html = html.replace("{next_img}", next.split("\\.")[0]);
        html = html.replace("{title}", title);
        html = html.replace("{image}", image);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(owner))) {
            writer.append(html);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void genDirectoryHtml(File owner, List<File> directories, List<File> images, int index) {
        String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <title>Képgaléria</title>
                </head>
                <body>
                  
                \s
                """;
        StringBuilder builder = new StringBuilder(html);

        if (index > 0) {
            builder.append(getRootSection(index, "<h1><a href=\"{path}.html\">Fooldal</a></h1>"));
        }

        builder.append("<h1>Képgaléria</h1>");
        builder.append("<h2>Mappák</h2>").append("<ul>");

        if (index > 0) {
            builder.append("<li><a href=\"../index.html\">Vissza</a></li>");
        }

        for (File directory : directories) {
            builder.append("<li><a href=\"{directory}/index.html\">{directory}</a></li>"
                    .replace("{directory}", directory.getName())
            );
        }

        builder.append("</ul>");
        builder.append("<h2>Képek</h2>").append("<ul>");
        for (File image : images) {
            String name = image.getName().split("\\.")[0];
            builder.append(
                    "<li><a href=\"{image}.html\">{image}</a></li>".replace("{image}", name)
            );
        }
        builder.append("</ul>");
        builder.append("</body>");
        builder.append("</html>");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(owner, "index.html")))) {
            writer.append(builder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getRootSection(int index, String html) {
        if (index > 0) {
            String path = "../".repeat(index) + "index";
            html = html.replace("{path}", path);
        } else {
            html = html.replace("{path}", "index");
        }
        return html;
    }
}
