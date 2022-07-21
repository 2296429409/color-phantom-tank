import Utils.ColoredTankUtils;
import Utils.InsideColoredTankUtils;
import Utils.WhiteAndBlackTankUtils;

import java.io.File;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

        //黑白
        WhiteAndBlackTankUtils.run(
                new File("1.jpg"),
                new File("2.png"),
                "3.png"
                );

        //内彩色
        InsideColoredTankUtils.run(
                new File("1.jpg"),
                new File("2.png"),
                "4.png"
                );
        //内外彩色
        ColoredTankUtils.run(
                new File("1.jpg"),
                new File("2.png"),
                "5.png"
                );

    }
}
