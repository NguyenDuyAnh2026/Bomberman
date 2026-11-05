package world;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MapLoader {
    private MapLoader() {
    }
    public static GameMap loadDefault() {
        return new GameMap(loadMap("resources/map/map1.txt"));
    }
    public static char[][] loadMap(String filePath) {
        List<char[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {
                rows.add(line.toCharArray());
            }

        } catch (IOException e) {
            throw new RuntimeException("Lỗi đọc map: " + filePath, e);
        }

        return rows.toArray(new char[0][]);
    }
}
