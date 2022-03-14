import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class Solver {
    private final Board initialBoard;
    private int nrMoves;
    private final Collection<Board> solutionSequence;
    private MinPQ<Node> priorityQueue = new MinPQ<>(new sortByManhattan());
    private boolean solvable;

    // Find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if(initial == null)
            throw new IllegalArgumentException();

        initialBoard = initial;
        nrMoves = 0;
        solutionSequence = new ArrayList<>();

        priorityQueue.insert(new Node(0, initialBoard, initialBoard.manhattan(), null));

        MinPQ<Node> priorityQueueTwin = new MinPQ<>(new sortByManhattan());
        priorityQueueTwin.insert(new Node(0, initialBoard.twin(), initialBoard.twin().manhattan(), null));

        Node currentNode;
        Node currentNodeTwin;

        boolean solved = false;
        boolean solvedTwin = false;

        while(!solved && !solvedTwin)
        {
            currentNode = priorityQueue.min();
            priorityQueue.delMin();
            solutionSequence.add(currentNode.board);

            if(currentNode.board.isGoal())
            {
                solvable = true;
                solved = true;
                break;
            }

            nrMoves++;

            for(Board board : currentNode.board.neighbors())
            {
                if(board.isGoal())
                {
                    solutionSequence.add(board);
                    solvable = true;
                    solved = true;
                    nrMoves = currentNode.moves + 1;
                    break;
                }

                if(currentNode.prevNode == null || !board.equals(currentNode.prevNode.board)) {
                    priorityQueue.insert(new Node(currentNode.moves + 1, board, board.manhattan(), currentNode));
                }
            }

            currentNodeTwin = priorityQueueTwin.min();
            priorityQueueTwin.delMin();

            for(Board boardTwin : currentNodeTwin.board.neighbors())
            {
                if(boardTwin.isGoal())
                {
                    solvable = false;
                    solvedTwin = true;
                    break;
                }

                if(currentNodeTwin.prevNode == null || !boardTwin.equals(currentNodeTwin.prevNode.board)) {
                    priorityQueueTwin.insert(new Node(currentNodeTwin.moves + 1, boardTwin, boardTwin.manhattan(), currentNodeTwin));
                }
            }
        }
    }

    // Is the initial board solvable?
    public boolean isSolvable()
    {
        return solvable;
    }

    // Minimum number of moves to solve initial board, -1 if unsolvable
    public int moves()
    {
        if(!isSolvable())
            return -1;

        return nrMoves;
    }

    // Sequence of boards in the shortest solution , null if unsolvable
    public Iterable<Board> solution()
    {
        if(!isSolvable())
            return null;

        return solutionSequence;
    }

    private class Node
    {
        public int moves;
        public Board board;
        public int manhattan;
        public Node prevNode;

        Node(int moves, Board board, int manhattan ,Node prevNode)
        {
            this.moves = moves;
            this.board = board;
            this.prevNode = prevNode;
            this.manhattan = manhattan;
        }
    }

    private static class sortByManhattan implements Comparator<Node>
    {
        public int compare(Node a, Node b)
        {
            return (a.manhattan + a.moves) - (b.manhattan + b.moves);
        }
    }

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];

        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        Solver solve = new Solver(initial);

        //ArrayList<Board> sol = (ArrayList<Board>) solve.solution();

        if(!solve.isSolvable())
            StdOut.println("No solution possible");
        else
        {
            StdOut.println("Minimum number of moves = " + solve.moves() + "\n");
            for(Board b : solve.solution())
                StdOut.println(b);
        }
    }
}
