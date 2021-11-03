package src;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
	// write your code here
        GameProgress gameResults1 = new GameProgress(500,10,10,100);
        GameProgress gameResults2 = new GameProgress(50,2,1,10);
        GameProgress gameResults3 = new GameProgress(5000,100,500,26.5);

        saveGame("D:\\Games\\savegames\\save1.dat",gameResults1);
        saveGame("D:\\Games\\savegames\\save2.dat",gameResults2);
        saveGame("D:\\Games\\savegames\\save3.dat",gameResults3);

        File root = new File("D:\\Games\\savegames");
        List<File> rootContent = Arrays.asList(root.listFiles(x -> x.getName().contains("dat")));
        zipFiles("D:\\Games\\savegames\\zip.zip", rootContent);

        for (File file:rootContent) {
            file.delete();
        }
    }

    public static void saveGame(String outputPath,GameProgress gameResults) {
        try (FileOutputStream fos = new FileOutputStream(outputPath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameResults);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void zipFiles(String outputPath, List<File> filesToZip) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(outputPath));
             FileInputStream fis = new FileInputStream(outputPath)) {
            for (File file : filesToZip) {
                ZipEntry entry = new ZipEntry(file.getName());
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
