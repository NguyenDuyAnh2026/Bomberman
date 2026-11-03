package utils;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;
public final class ImageLoader {
    private static final Map<String,Image> CACHE=new HashMap<>();
    private ImageLoader() {
    }
    public static Image load(String path) {
        return CACHE.computeIfAbsent(path,p-> {
            var url=ImageLoader.class.getResource("/"+p);return url==null?null:new Image(url.toExternalForm());
        }
        );
    }
}
