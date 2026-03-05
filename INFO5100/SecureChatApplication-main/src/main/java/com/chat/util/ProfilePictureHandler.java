package com.chat.util;

import javafx.scene.image.Image; // 用于处理图片
import javafx.embed.swing.SwingFXUtils; // 将Java Swing图片转为JavaFX图片

import javax.imageio.ImageIO; // 读取图片文件
import java.awt.image.BufferedImage; // 用于操作图片内容
import java.io.File; // 用于处理文件操作

public class ProfilePictureHandler {

    /**
     * 创建圆形头像并返回裁剪后的 Image 对象
     *
     * @param file 图像文件
     * @return 裁剪后的 Image 对象
     */
    public static Image createCircularProfilePicture(File file) {
        try {
            // 从文件读取图片并转换为 BufferedImage
            BufferedImage bufferedImage = ImageIO.read(file);

            // 将 BufferedImage 转为 JavaFX 的 Image
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            image = ImageCropUtil.cropToSquare(image);

            return image; // 返回处理后的圆形头像图像
        } catch (Exception e) {
            System.err.println("Load image error");
            e.printStackTrace(); // 输出异常信息
            return null; // 如果处理失败，返回 null
        }
    }
}
