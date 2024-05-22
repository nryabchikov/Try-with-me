package by.ryabchikov.coursework.util;

import by.ryabchikov.coursework.model.user.User;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageGenerator {

    private static final String[] COLORS = {"#2196F3", "#32c787", "#00BCD4", "#ff5652", "#ffc107", "#ff85af", "#FF9800", "#39bbb0"};

    public static String generateAvatar(String initials, String savePath, String login) throws IOException {
        int width = 500;
        int height = 500;

        Files.createDirectories(Paths.get(savePath));

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        Color color = Color.decode(getRandomColor());
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);

        g2d.setFont(new Font("Arial", Font.BOLD, 200));
        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(initials)) / 2;
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(initials, x, y);

        g2d.dispose();

        String fileName = login + "_avatar.png";
        ImageIO.write(bufferedImage, "png", new File(savePath + "/" + fileName));

        return fileName;
    }

    private static String getRandomColor() {
        Random random = new Random();
        int index = random.nextInt(COLORS.length);
        return COLORS[index];
    }

    public static String getInitials(User user) {
        if (user.getSurname() != null && !user.getSurname().isEmpty()) {
            return String.valueOf(user.getSurname().charAt(0)).toUpperCase();
        } else {
            return String.valueOf(user.getLogin().charAt(0)).toUpperCase();
        }
    }
}

