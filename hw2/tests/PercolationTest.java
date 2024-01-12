import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class PercolationTest {
    private int[][] getSmallState(int N, Percolation p) {
        int[][] state = new int[3][3];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int open = p.isOpen(r, c) ? 1 : 0;
                int full = p.isFull(r, c) ? 2 : 0;
                state[r][c] = open + full;
            }
        }
        return state;
    }

    private int[][] getState(int N, Percolation p) {
        int[][] state = new int[5][5];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int open = p.isOpen(r, c) ? 1 : 0;
                int full = p.isFull(r, c) ? 2 : 0;
                state[r][c] = open + full;
            }
        }
        return state;
    }

    @Test
    public void basicTest() {
        int N = 5;
        Percolation p = new Percolation(5);
        int[][] openSites = {
                {0, 1},
                {2, 0},
                {3, 1},
                {4, 1},
                {1, 0},
                {1, 1}
        };
        int[][] expectedState = {
                {0, 3, 0, 0, 0},
                {3, 3, 0, 0, 0},
                {3, 0, 0, 0, 0},
                {0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0}
        };
        for (int[] site : openSites) {
            p.open(site[0], site[1]);
        }
        assertThat(getState(N, p)).isEqualTo(expectedState);
        assertThat(p.percolates()).isFalse();
    }

    @Test
    public void fullTileTest() {
        int N = 3;
        Percolation p = new Percolation(3);
        int[][] openSites = {
                {0, 0},
                {0, 1},
                {0, 2},
                {1, 0},
                {1, 1},
                {1, 2},
                {2, 0},
                {2, 1},
                {2, 2}
        };
        int[][] expectedState = {
                {3, 3, 3},
                {3, 3, 3},
                {3, 3, 3},
        };
        for (int[] site : openSites) {
            p.open(site[0], site[1]);
        }
        assertThat(getSmallState(N, p)).isEqualTo(expectedState);
        assertThat(p.percolates()).isTrue();
    }


    @Test
    public void percolatesTest() {
        int N = 5;
        Percolation p = new Percolation(5);
        int[][] openSites = {
                {0, 1},
                {2, 0},
                {3, 1},
                {4, 1},
                {1, 0},
                {1, 1},
                {2, 1}
        };
        int[][] expectedState = {
                {0, 3, 0, 0, 0},
                {3, 3, 0, 0, 0},
                {3, 3, 0, 0, 0},
                {0, 3, 0, 0, 0},
                {0, 3, 0, 0, 0}
        };
        for (int[] site : openSites) {
            p.open(site[0], site[1]);
        }
        assertThat(getState(N, p)).isEqualTo(expectedState);
        assertThat(p.percolates()).isTrue();
    }

    @Test
    public void backwashTest() {
        int N = 5;
        Percolation p = new Percolation(5);
        int[][] openSites = {
                {0, 1},
                {2, 0},
                {3, 1},
                {4, 1},
                {1, 0},
                {1, 1},
                {2, 1},
                {4, 3},
                {3, 3},
                {2, 3}
        };
        int[][] expectedState = {
                {0, 3, 0, 0, 0},
                {3, 3, 0, 0, 0},
                {3, 3, 0, 1, 0},
                {0, 3, 0, 1, 0},
                {0, 3, 0, 1, 0}
        };
        for (int[] site : openSites) {
            p.open(site[0], site[1]);
        }
        assertThat(getState(N, p)).isEqualTo(expectedState);
    }


}
