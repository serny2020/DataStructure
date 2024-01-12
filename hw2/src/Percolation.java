import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/*
 * CS61B Fall 2023 homework 2 -- percolation
 * Author: Xiaocheng Sun
 * Date: Jan 11th, 2024
 * Percolation simulation of Monte Carlo Model
 */
public class Percolation {
    private final boolean[] board;
    private final int boardSide;
    private final int VIRTUAL_TOP;
    private final int VIRTUAL_BOTTOM;
    private final WeightedQuickUnionUF connectionBoard; //used to check if water goes through
    private final WeightedQuickUnionUF antiBackWash; //used to avoid water going backward
    private int openSites;

    public Percolation(int N) {
        boardSide = N;
        VIRTUAL_TOP = N * N;
        VIRTUAL_BOTTOM = N * N + 1;
        openSites = 0;
        //initialize an array of board to keep track if any tile is open
        board = new boolean[N * N];
        //connectionBoard connect both virtual top and button.
        connectionBoard = new WeightedQuickUnionUF(N * N + 2);
        //antiBackWash only connect to the virtual top
        antiBackWash = new WeightedQuickUnionUF(N * N + 1);
        //connect all the top tiles with the virtual top
        for (int col = 0; col < boardSide; col++) {
            connectionBoard.union(col, VIRTUAL_TOP);
            antiBackWash.union(col, VIRTUAL_TOP);
        }
        //connect all the bottom with the virtual bottom
        for (int col = xyTo1D(boardSide - 1, 0); col < xyTo1D(boardSide - 1, boardSide - 1); col++) {
            connectionBoard.union(col, VIRTUAL_BOTTOM);
        }

    }

    /**
     * convert 2D index to 1D representation
     *
     * @param x of row index
     * @param y of column index
     * @return 1D representation in the 2D board
     */
    private int xyTo1D(int x, int y) {
        return boardSide * x + y;
    }

    /**
     * Check if row or column is within the bound
     *
     * @param row index
     * @param col index
     */
    private void isValidIndex(int row, int col) {
        if (row < 0 || row > boardSide || col < 0 || col > boardSide) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    /**
     * Get the 1D representation of all the neighbors, -1 if there is no such neighbor.
     *
     * @param row index of the current tile
     * @param col index of the current tile
     * @return an int array of 1D representation of all the neighbors, -1 if no such neighbor exists.
     */
    private int[] getNeighbors(int row, int col) {
        int[] neighbors = {-1, -1, -1, -1};
        //if there is a top neighbor
        if (row > 0) {
            neighbors[0] = xyTo1D(row - 1, col);
        }
        //if there is a bottom neighbor
        if (row < boardSide - 1) {
            neighbors[1] = xyTo1D(row + 1, col);
        }
        //if there is a left neighbor
        if (col > 0) {
            neighbors[2] = xyTo1D(row, col - 1);
        }
        //if there is a right neighbor
        if (col < boardSide - 1) {
            neighbors[3] = xyTo1D(row, col + 1);
        }
        return neighbors;
    }

    /**
     * Check if any neighbors is open, and connect them if there is any.
     *
     * @param row index of the current tile
     * @param col index of the current tile
     */
    private void connectOpenNeighbors(int row, int col) {
        for (int tile : getNeighbors(row, col)) {
            //if any valid neighbor is open, connect this tile with neighbors
            if (tile != -1 && board[tile]) {
                connectionBoard.union(xyTo1D(row, col), tile);
                antiBackWash.union(xyTo1D(row, col), tile);
            }
        }
    }

    /**
     * Open the current tile and connect with its open neighbors if there is any exists
     *
     * @param row index of current tile
     * @param col index of current tile
     */
    public void open(int row, int col) {
        isValidIndex(row, col);
        if (!board[xyTo1D(row, col)]) {
            board[xyTo1D(row, col)] = true;
            openSites += 1;
            connectOpenNeighbors(row, col);
        }
    }

    /**
     * Check if a given tile is open.
     *
     * @param row index of current tile
     * @param col index of current tile
     * @return true if the tile is open; false otherwise
     */
    public boolean isOpen(int row, int col) {
        isValidIndex(row, col);
        return board[xyTo1D(row, col)];
    }

    /**
     * check if the given tile is filled with water
     *
     * @param row index of current tile
     * @param col index of current tile
     * @return true if the tile is full; false otherwise
     */
    public boolean isFull(int row, int col) {
        isValidIndex(row, col);
        //return true if current tile is open and connected to the top
        return (isOpen(row, col) && antiBackWash.connected(xyTo1D(row, col), VIRTUAL_TOP));
    }

    /**
     * Accessing the number of open space.
     * This method takes constant time since it simply returns the value of the global variable.
     *
     * @return the number of open tiles
     */
    public int numberOfOpenSites() {
        return openSites;
    }

    /**
     * Check if the system percolated
     *
     * @return true if the virtual top is connected with the virtual bottom
     */
    public boolean percolates() {
        if (numberOfOpenSites() < boardSide) {
            return false;
        }
        return (connectionBoard.connected(VIRTUAL_TOP, VIRTUAL_BOTTOM));
    }
}
