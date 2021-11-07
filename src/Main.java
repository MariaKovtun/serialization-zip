import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
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
        //openZip("D:\\Games\\savegames\\zip.zip","D:\\Games\\savegames\\zip");
        //System.out.println(openProgress("D:\\Games\\savegames\\zip\\save1.dat").toString());
    }

    public static GameProgress openProgress(String serializedProgressPath) {
        GameProgress res = null;
        try (FileInputStream  fis = new FileInputStream(serializedProgressPath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            res = (GameProgress) ois.readObject();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public static void openZip(String zipPath,String dirPath) {
        File outputDir = new File(dirPath);
        outputDir.mkdir();

        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(dirPath +"\\"+name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(outputPath)))
        {
            for (File file : filesToZip) {
                byte[] buffer;
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zout.putNextEntry(entry);
                    buffer = new byte[fis.available()];
                    fis.read(buffer);
                }
                zout.write(buffer);
                zout.closeEntry();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
