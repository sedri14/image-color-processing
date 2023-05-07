import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String OUTPUT_FILE = "./out/many-flowers.jpg";

    public static void main(String[] args) throws IOException {

        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();
        //recolorSingleThreaded(originalImage, resultImage);
        int numberOfThreads = 6;
        recolorMultithreaded(originalImage, resultImage, numberOfThreads);

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        File outputFile = new File(OUTPUT_FILE);
        ImageIO.write(resultImage, "jpg", outputFile);

        System.out.println(duration);

    }

    public static void recolorMultithreaded(BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>(numberOfThreads);
        int chunkWidth = originalImage.getWidth();
        int chunkHeight = originalImage.getHeight() / numberOfThreads;

        //image partitioning
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;
            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = chunkHeight * threadMultiplier;
                recolorImage(originalImage, resultImage,leftCorner, topCorner, chunkWidth, chunkHeight);
            });
            threads.add(thread);
        }

        for(Thread thread : threads) {
            thread.start();
        }

        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner,
                                    int width, int height) {
        for(int x = leftCorner ; x < leftCorner + width && x < originalImage.getWidth() ; x++) {
            for(int y = topCorner ; y < topCorner + height && y < originalImage.getHeight() ; y++) {
                recolorPixel(originalImage, resultImage, x , y);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        Pixel pixel = new Pixel(originalImage.getRGB(x, y));

        if(isShadeOfGray(pixel)) {
            pixel.setRed(Math.min(255, pixel.getRed() + 10));
            pixel.setGreen(Math.max(0, pixel.getGreen() - 80));
            pixel.setBlue(Math.max(0, pixel.getBlue() - 20));
        }
        setRGB(resultImage, x, y, Pixel.asRGB(pixel));
    }

    public static boolean isShadeOfGray(Pixel pixel) {
        return Math.abs(pixel.getRed()) - pixel.getGreen() < 30 && Math.abs(pixel.getRed() - pixel.getBlue()) < 30 && Math.abs(pixel.getGreen()) - pixel.getBlue() < 30;
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }
}
