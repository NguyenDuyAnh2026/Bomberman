package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private static final Map<String, BufferedImage> cache = new HashMap<>();

    public static BufferedImage load(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }
        try {
            // Đảm bảo load từ thư mục resources
            BufferedImage img = ImageIO.read(ImageLoader.class.getClassLoader().getResource(path));
            if (img != null) {
                cache.put(path, img);
            }
            return img;
        } catch (IOException | IllegalArgumentException e) {
            // Trả về null để hiện hình vuông đỏ dự phòng nếu chưa có file ảnh
            return null;
        }
    }
}
