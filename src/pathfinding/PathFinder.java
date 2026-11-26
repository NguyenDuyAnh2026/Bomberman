
package pathfinding;

import world.GameMap;

import java.util.*;

public class PathFinder {

    private static class Node {

        int col, row;
        int g, h;
        Node parent;

        Node(int col, int row, int g, int h, Node parent) {
            this.col = col;
            this.row = row;
            this.g = g;
            this.h = h;
            this.parent = parent;
        }

        int f() {
            return g + h;
        }
    }

    private static final int[][] DIRS = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };

    public static List<int[]> findPath(
            GameMap map,
            int startCol, int startRow,
            int targetCol, int targetRow
    ) {

        List<int[]> path = new ArrayList<>();

        PriorityQueue<Node> open = new PriorityQueue<>(
                Comparator.comparingInt(Node::f)
        );

        boolean[][] visited = new boolean[map.rows()][map.cols()];

        Node start = new Node(
                startCol, startRow,
                0,
                heuristic(startCol, startRow, targetCol, targetRow),
                null
        );

        open.add(start);

        while (!open.isEmpty()) {

            Node current = open.poll();

            if (visited[current.row][current.col]) {
                continue;
            }

            visited[current.row][current.col] = true;

            // Đã đến đích
            if (current.col == targetCol
                    && current.row == targetRow) {

                return reconstructPath(current);
            }

            // Xét 4 ô lân cận
            for (int[] d : DIRS) {

                int nc = current.col + d[0];
                int nr = current.row + d[1];

                if (!map.isWalkable(nc, nr)) {
                    continue;
                }

                if (visited[nr][nc]) {
                    continue;
                }

                int newG = current.g + 1;

                int newH = heuristic(
                        nc, nr,
                        targetCol, targetRow
                );

                Node neighbor = new Node(
                        nc, nr,
                        newG, newH,
                        current
                );

                open.add(neighbor);
            }
        }

        // Không tìm được đường
        return path;
    }

    private static int heuristic(
            int c1, int r1,
            int c2, int r2
    ) {
        return Math.abs(c1 - c2)
                + Math.abs(r1 - r2);
    }

    private static List<int[]> reconstructPath(Node node) {

        List<int[]> path = new ArrayList<>();

        while (node.parent != null) {

            path.add(new int[]{
                    node.col,
                    node.row
            });

            node = node.parent;
        }

        Collections.reverse(path);

        return path;
    }
}
