package world;

public class GameMap {
    private final char[][] tiles;
    private final int tileSize=40;
    private final int portalC=13,portalR=11;
    public GameMap() {
        String[] rows= {
            "###############","#.............#","#.B..B.B.B.B..#","#.B..B.B.B.B..#","#.B..B.B..B...#","#.BB..B...B...#","#.............#","#.............#","#.#.#.#.#.#.#.#","#B.B.B.B.B.B.B#","#.#...#...#.#.#","#............G#","###############"
        }
        ;
        tiles=new char[rows.length][rows[0].length()];
        for(int r=0;r<rows.length;r++)tiles[r]=rows[r].toCharArray();
        tiles[1][1]='.';
    }
    public char get(int c,int r) {
        if(r<0||r>=tiles.length||c<0||c>=tiles[0].length)return '#';
        return tiles[r][c];
    }
    public void set(int c,int r,char v) {
        if(r>=0&&r<tiles.length&&c>=0&&c<tiles[0].length)tiles[r][c]=v;
    }
    public boolean isWalkable(int c,int r) {
        char t=get(c,r);
        return t=='.'||t=='G';
    }
    public int rows() {
        return tiles.length;
    }
    public int cols() {
        return tiles[0].length;
    }
    public int tileSize() {
        return tileSize;
    }
    public int pixelWidth() {
        return cols()*tileSize;
    }
    public int pixelHeight() {
        return rows()*tileSize;
    }
    public int portalCol() {
        return portalC;
    }
    public int portalRow() {
        return portalR;
    }
}
