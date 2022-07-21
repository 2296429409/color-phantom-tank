package Utils;

import config.InsideConfig;
import config.OutsideConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 生产内外彩色幻影坦克图
 */
public class ColoredTankUtils {

    /**
     * 生产内外彩色幻影坦克图
     *
     * @param outsideImg 表
     * @param insideImg  里
     * @param path       生成路径
     */
    public static void run(File outsideImg, File insideImg, String path) {
        try {
            Color[][] outside = ImgRgbUtils.getPixels(ImageIO.read(outsideImg));
            Color[][] inside = ImgRgbUtils.getPixels(ImageIO.read(insideImg));
            //拉伸表图成统一大小
            int hl = inside.length;
            int wl = inside[0].length;
            if (hl!=outside.length||wl != outside[0].length){
                outside = ImgRgbUtils.getPixels(ImgRgbUtils.resizeImage(ImageIO.read(outsideImg), wl, hl));
            }
            GetHandlePNG(outside, OutsideConfig.A, OutsideConfig.R, OutsideConfig.G, OutsideConfig.B, OutsideConfig.light, OutsideConfig.colour);
            GetHandlePNG(inside, InsideConfig.A, InsideConfig.R, InsideConfig.G, InsideConfig.B, InsideConfig.light, InsideConfig.colour);
            Color[][] colors = ColourMerge(outside, inside);
            BufferedImage bufferedImage = ImgRgbUtils.toImage(colors);
            ImageIO.write(bufferedImage, "png", new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成彩色幻影坦克
     *
     * @param colorsF 表图
     * @param colorsB 里图
     * @return
     */
    public static Color[][] ColourMerge(Color[][] colorsF, Color[][] colorsB) {
        int h = colorsF.length;
        int w = colorsF[0].length;
        Color[][] colors = new Color[h][w];
        for (int x = 0; x < colorsF.length; x++) {
            for (int y = 0; y < colorsF[x].length; y++) {

                int alphaR = 255 - colorsF[x][y].getRed() + colorsB[x][y].getRed();
                int alphaG = 255 - colorsF[x][y].getGreen() + colorsB[x][y].getGreen();
                int alphaB = 255 - colorsF[x][y].getBlue() + colorsB[x][y].getBlue();

                int newAlpha = (int) ((alphaR + alphaG + alphaB) / 3);
                newAlpha = newAlpha > 255 ? 255 : newAlpha;
                newAlpha = newAlpha <= 0 ? 1 : newAlpha;

                alphaR = alphaR <= 0 ? 1 : alphaR;
                int newR = 255 * colorsB[x][y].getRed() / alphaR;
                newR = newR > 255 ? 255 : newR;

                alphaG = alphaG <= 0 ? 1 : alphaG;
                int newG = 255 * colorsB[x][y].getGreen() / alphaG;
                newG = newG > 255 ? 255 : newG;

                alphaB = alphaB <= 0 ? 1 : alphaB;
                int newB = 255 * colorsB[x][y].getBlue() / alphaB;
                newB = newB > 255 ? 255 : newB;

                colors[x][y] = new Color(newR, newG, newB, newAlpha);

            }
        }
        return colors;
    }

    /**
     * 彩色幻影坦克的图片处理
     *
     * @param colors
     * @param A      Alpha倍数
     * @param R      Red比重
     * @param G      Green比重
     * @param B      Blue比重
     * @param Light  亮度
     * @return
     */
    private static void GetHandlePNG(Color[][] colors, float A, float R, float G, float B, float Light, float colour) {
        float r = R / (R + G + B);
        float g = G / (R + G + B);
        float b = B / (R + G + B);
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[x].length; y++) {
                Color color = colors[x][y];
                int gray = (int) (color.getRed() * r + color.getGreen() * g + color.getBlue() * b);
                int alpha = (int) (color.getAlpha() * A);
                alpha = alpha > 255 ? 255 : alpha;
                int newR = (int) (gray + colour * (color.getRed() - gray));
                int newG = (int) (gray + colour * (color.getGreen() - gray));
                int newB = (int) (gray + colour * (color.getBlue() - gray));
                colors[x][y] = new Color(newR, newG, newB, alpha);
            }
        }
        //修改图片亮度
        ImgRgbUtils.GetGrayScaleHalf(colors, Light);
    }

}
