public class Pixel {
    int red;
    int green;
    int blue;

    public Pixel(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Pixel(int rgb) {
        this.red = getRedFromRGB(rgb);
        this.green = getGreenFromRGB(rgb);
        this.blue = getBlueFromRGB(rgb);
    }

    public static int asRGB(Pixel pixel) {
        int rgb = 0;
        rgb |= pixel.getBlue();
        rgb |= pixel.getGreen() << 8;
        rgb |= pixel.getRed() << 16;
        rgb |= 0xFF000000;          //opaque

        return rgb;
    }

    private int getBlueFromRGB(int rgb) {
        return rgb & 0x000000FF;
    }

    private int getGreenFromRGB(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    private int getRedFromRGB(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}
