package world;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    // Đọc map từ file text trong thư mục resources (ví dụ: resources/maps/level1.txt)
    public static Tile[][] loadMapFromFile(String filePath) {
        List<String> lines = new ArrayList<>();

        try (InputStream is = MapLoader.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                System.err.println("Không tìm thấy file map: " + filePath);
                return null;
            }

            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        Tile[][] tiles = new Tile[rows][cols];

        for (int y = 0; y < rows; y++) {
            String line = lines.get(y);
            for (int x = 0; x < cols; x++) {
                char symbol = line.charAt(x);
                tiles[y][x] = TileFactory.createTile(symbol);
            }
        }

        return tiles;
    }
}
