import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/*
 * CS61B Fall 2023 homework 2 -- percolation
 * Author: Xiaocheng Sun
 * Date: Jan 11th, 2024
 * Percolation simulation of Monte Carlo Model
 */
public class Percolation {
    private boolean[] board;
    private int boardSide;
    private final int VIRTUAL_TOP;
    private final int VIRTUAL_BUTTOM;
    private WeightedQuickUnionUF connectionBoard;

    public Percolation(int N) {
        boardSide = N;
        VIRTUAL_TOP = N * N;
        VIRTUAL_BUTTOM = N * N + 1;
        //initialize an array of board to keep track if any cell is open
        board = new boolean[N * N + 1];
        //initialize a connectionBoard with virtual top and button.
        connectionBoard = new WeightedQuickUnionUF(N * N + 2);
        //connect all the top squares with the virtual top
        for (int i = 0; i < boardSide; i++) {
            connectionBoard.union(i, VIRTUAL_TOP);
        }

    }

    private int xyTo1D(int x, int y) {
        return boardSide * x + y;
    }

    /**
     * Get the 1D representation of all the neighbors, -1 if there is no such neighbor.
     *
     * @param row index of the current cell
     * @param col index of the current cell
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
     * @param row index of the current cell
     * @param col index of the current cell
     */
    private void connectOpenNeighbors(int row, int col) {
        for (int cell : getNeighbors(row, col)) {
            //if any valid neighbor is open, connect this cell with neighbors
            if (cell != -1 && board[cell]) {
                connectionBoard.union(xyTo1D(row, col), cell);
            }
        }
    }

    public void open(int row, int col) {
        if (!board[xyTo1D(row, col)]) {
            board[xyTo1D(row, col)] = true;
            connectOpenNeighbors(row, col);
        }
    }

    public boolean isOpen(int row, int col) {
        return board[xyTo1D(row, col)];
    }

    /**
     * @param row
     * @param col
     * @return true if the cell is full; false otherwise
     */
    public boolean isFull(int row, int col) {
        //return true if current cell is open and connected to the top
        if (isOpen(row, col) && connectionBoard.connected(xyTo1D(row, col), VIRTUAL_TOP)) {
            return true;
        } else {
            return false;
        }
    }

    public int numberOfOpenSites() {
        // TODO: Fill in this method.
        return 0;
    }

    public boolean percolates() {
        // TODO: Fill in this method.
        return false;
    }

    // TODO: Add any useful helper methods (we highly recommend this!).
    // TODO: Remove all TODO comments before submitting.

}
