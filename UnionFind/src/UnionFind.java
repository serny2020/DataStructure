public class UnionFind {
    /**
     * DO NOT DELETE OR MODIFY THIS, OTHERWISE THE TESTS WILL NOT PASS.
     * You can assume that we are only working with non-negative integers as the items
     * in our disjoint sets.
     */
    private int[] data;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        data = new int[N];
        for (int i = 0; i < N; i++) {
            data[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        //return the negative value inside the root element.
        return -data[find(v)];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return data[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        //connected vertex should have the same root.
        return find(v1) == find(v2);
    }

    private void checkValidation(int v) {
        if (v < 0 || v >= data.length) {
            throw new IllegalArgumentException("Invalid index.");
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        //if its negative, then return the index
        checkValidation(v);
        if (data[v] < 0) { //root value is the subtree size, so return the index which is the node #.
            return v;
        } else {
            //find out the root of v's parent, data compression by making the root x's parent.
            data[v] = find(parent(v));
            return data[v]; //return the root.
        }
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        //find the root of v1 and v2
        int v1_root = find(v1);
        int v2_root = find(v2);
        //return if v1 and v2 is connected
        if (v1_root == v2_root) {
            return;
        }
        if (sizeOf(v1_root) > sizeOf(v2_root)) {
            //if v1 is larger, make v2 its child.
            data[v1_root] += data[v2_root];
            data[v2_root] = v1_root;
        } else {
            //if v2 is larger, make v1 its child.
            data[v2_root] += data[v1_root];
            data[v1_root] = v2_root;
        }
    }

    /**
     * DO NOT DELETE OR MODIFY THIS, OTHERWISE THE TESTS WILL NOT PASS.
     */
    public int[] returnData() {
        return data;
    }
}
