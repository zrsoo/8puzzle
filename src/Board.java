import java.util.ArrayList;
import java.util.Collection;

public class Board {
    private final int[][] tiles;
    private final int size;

    public Board(int[][] tiles)
    {
        this.tiles = tiles;
        size = this.tiles.length;
    }

    public String toString()
    {
        String result = "";

        result += size + "\n";

        for(int i = 0; i < size; ++i) {
            result += " ";
            for(int j = 0; j < size; ++j)
                result += tiles[i][j] + " ";
            result += "\n";
        }

        return result;
    }

    public int dimension()
    {
        return size;
    }

    public int hamming()
    {
        int cnt = 0;

        for(int i = 0; i < size; ++i)
            for(int j = 0; j < size; ++j)
                if(tiles[i][j] != 0 && tiles[i][j] != i * size + j + 1)
                    cnt++;

        return cnt;
    }

    public int manhattan()
    {
        int distance = 0;
        int properColumn;
        int properRow;

        for(int i = 0; i < size; ++i)
            for(int j = 0; j < size; ++j)
                if(tiles[i][j] != 0 && tiles[i][j] != i * size + j + 1)
                {
                    // Element is supposed to be on the last column
                    if(tiles[i][j] % size == 0)
                        properRow = tiles[i][j] / size - 1;

                    // Element on the first columns
                    else
                        properRow = tiles[i][j] / size;

                    properColumn = tiles[i][j] - (properRow * size + 1);

                    distance += Math.abs(i - properRow);
                    distance += Math.abs(j - properColumn);
                }

        return distance;
    }

    public boolean isGoal()
    {
        return (hamming() == 0);
    }

    public boolean equals(Object y)
    {
        if(y == null)
            return false;

        if(!(y instanceof Board))
            return false;

        Board boardY = (Board) y;

        if(this.size != boardY.size)
            return false;

        for(int i = 0; i < size; ++i)
            for(int j = 0; j < size; ++j)
                if(this.tiles[i][j] != boardY.tiles[i][j])
                    return false;

        return true;
    }

    public Iterable<Board> neighbors()
    {
        Collection<Board> result = new ArrayList<>();

        int[] di = {-1, 0, 1, 0};
        int[] dj = {0, 1, 0, -1};

        int i0 = 0;
        int j0 = 0;

        int newi;
        int newj;

        for(int i = 0; i < size; ++i)
            for(int j = 0; j < size; ++j)
                if(tiles[i][j] == 0)
                {
                    i0 = i;
                    j0 = j;
                }

        Board[] boards = new Board[4];
        
        for(int delta = 0; delta < 4; ++delta)
        {
            newi = i0 + di[delta];
            newj = j0 + dj[delta];

            if(newi >= 0 && newi < size &&
                    newj >= 0 && newj < size)
            {
                int[][] newTiles = new int[size][size];

                for(int i = 0; i < size; ++i)
                    System.arraycopy(tiles[i], 0, newTiles[i], 0, size);

                boards[delta] = new Board(newTiles);

                boards[delta].tiles[i0][j0] = boards[delta].tiles[newi][newj];
                boards[delta].tiles[newi][newj] = 0;

                result.add(boards[delta]);
            }
        }

        return result;
    }

    public Board twin()
    {
        int[][] newTiles = new int[size][size];

        for(int i = 0; i < size; ++i)
            System.arraycopy(tiles[i], 0, newTiles[i], 0, size);

        Board resultBoard = new Board(newTiles);
        int aux;

        for(int i = 0; i < size; ++i)
            for(int j = 0; j < size - 1; ++j)
                if(resultBoard.tiles[i][j] != 0 && resultBoard.tiles[i][j + 1] != 0)
                {
                    aux = resultBoard.tiles[i][j];
                    resultBoard.tiles[i][j] = resultBoard.tiles[i][j + 1];
                    resultBoard.tiles[i][j + 1] = aux;

                    i = size;
                    break;
                }

        return resultBoard;
    }

    public static void main(String[] args)
    {
        int[][] tiles = {
                {1, 0, 3},
                {4, 8, 2,},
                {7, 6, 5}
        };

        Board b = new Board(tiles);

        System.out.println(b);
        System.out.println(b.hamming());
        System.out.println(b.manhattan());

        for(Board board : b.neighbors())
            System.out.println(board);

        System.out.println(b.dimension());
        System.out.println(b.isGoal());
        System.out.println(b);
        System.out.println(b.twin());
    }
}
