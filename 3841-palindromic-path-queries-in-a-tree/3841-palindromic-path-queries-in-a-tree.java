import java.util.*;

class Solution {
    int n, log, timer;
    List<Integer>[] graph;
    int[] tin, tout, depth, bit;
    int[][] up;

    public List<Boolean> palindromePath(
            int n, int[][] edges, String s, String[] queries) {

        this.n = n;
        timer = 0;

        graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            graph[u].add(v);
            graph[v].add(u);
        }

        log = 1;
        while ((1 << log) <= n) {
            log++;
        }

        tin = new int[n];
        tout = new int[n];
        depth = new int[n];
        up = new int[log][n];
        bit = new int[n + 2];

        buildTree();

        char[] chars = s.toCharArray();

        for (int i = 0; i < n; i++) {
            updateRange(tin[i], tout[i], 1 << (chars[i] - 'a'));
        }

        List<Boolean> result = new ArrayList<>();

        for (int i = 0; i < queries.length; i++) {
            String[] q = queries[i].split(" ");

            if (q[0].equals("update")) {
                int u = Integer.parseInt(q[1]);
                char c = q[2].charAt(0);

                int oldMask = 1 << (chars[u] - 'a');
                int newMask = 1 << (c - 'a');

                if (oldMask != newMask) {
                    updateRange(tin[u], tout[u], oldMask ^ newMask);
                    chars[u] = c;
                }
            } else {
                int u = Integer.parseInt(q[1]);
                int v = Integer.parseInt(q[2]);

                int ancestor = lca(u, v);

                int mask = prefixXor(tin[u])
                         ^ prefixXor(tin[v])
                         ^ (1 << (chars[ancestor] - 'a'));

                result.add((mask & (mask - 1)) == 0);
            }
        }

        return result;
    }

    private void buildTree() {
        int[] stack = new int[n];
        int[] parent = new int[n];
        int top = 0;

        Arrays.fill(parent, -1);
        stack[top++] = 0;
        parent[0] = 0;

        while (top > 0) {
            int u = stack[--top];

            tin[u] = ++timer;
            up[0][u] = parent[u];

            for (int j = 1; j < log; j++) {
                up[j][u] = up[j - 1][up[j - 1][u]];
            }

            for (int i = 0; i < graph[u].size(); i++) {
                int v = graph[u].get(i);

                if (v == parent[u]) {
                    continue;
                }

                parent[v] = u;
                depth[v] = depth[u] + 1;
                stack[top++] = v;
            }
        }

        for (int u = 0; u < n; u++) {
            tout[u] = tin[u];
        }

        // Compute subtree end times in reverse traversal order.
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }

        Arrays.sort(order, (a, b) -> Integer.compare(tin[b], tin[a]));

        for (int i = 0; i < n; i++) {
            int u = order[i];

            for (int j = 0; j < graph[u].size(); j++) {
                int v = graph[u].get(j);

                if (up[0][v] == u) {
                    tout[u] = Math.max(tout[u], tout[v]);
                }
            }
        }
    }

    private void updateRange(int left, int right, int mask) {
        update(left, mask);
        update(right + 1, mask);
    }

    private void update(int index, int mask) {
        while (index < bit.length) {
            bit[index] ^= mask;
            index += index & -index;
        }
    }

    private int prefixXor(int index) {
        int mask = 0;

        while (index > 0) {
            mask ^= bit[index];
            index -= index & -index;
        }

        return mask;
    }

    private boolean isAncestor(int u, int v) {
        return tin[u] <= tin[v] && tout[v] <= tout[u];
    }

    private int lca(int u, int v) {
        if (isAncestor(u, v)) {
            return u;
        }

        if (isAncestor(v, u)) {
            return v;
        }

        for (int j = log - 1; j >= 0; j--) {
            if (!isAncestor(up[j][u], v)) {
                u = up[j][u];
            }
        }

        return up[0][u];
    }
}