import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Scanner;
import java.util.stream.Stream;

public class GetLockScreenImages {
    public static void main(String[] args) {
        final String srcFolderPath = System.getProperty("user.home") +
                "\\AppData\\Local\\Packages" +
                "\\Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy\\LocalState\\Assets";
        final String folderToSave = ".\\imgs";
        try (Stream<Path> stream = Files.list(Paths.get(srcFolderPath))) {
            Files.createDirectories(Paths.get(folderToSave));
            AtomicInteger copiedAmount = new AtomicInteger();
            stream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> file.length() > 12000)
                    .map(File::getName)
                    .filter(filename -> !filename.contains("."))
                    .forEach(filename -> {
                        try {
                            Path newFilePath = Paths.get(folderToSave + "\\" + filename + ".jpg");
                            if (!Files.exists(newFilePath)) {
                                Files.copy(Paths.get(srcFolderPath + "\\" + filename), newFilePath);
                                copiedAmount.getAndIncrement();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            System.out.println("Successfully copied " + copiedAmount + " files");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner s = new Scanner(System.in);
        System.out.println("Press enter to exit . . .");
        s.nextLine();
    }
}
