import java.util.*;

class MinimumSwaps {
    public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        UnionFind uf = new UnionFind(s.length());

        // Iterate over the edges
        for (List<Integer> edge : pairs) {
            int source = edge.get(0);
            int destination = edge.get(1);

            // Perform the union of end points
            uf.union(source, destination);
        }

        Map<Integer, List<Integer>> rootToComponent = new HashMap<>();
        // Group the vertices that are in the same component
        for (int vertex = 0; vertex < s.length(); vertex++) {
            int root = uf.find(vertex);
            // Add the vertices corresponding to the component root
            rootToComponent.putIfAbsent(root, new ArrayList<>());
            rootToComponent.get(root).add(vertex);
        }

        // String to store the answer
        char[] smallestString = new char[s.length()];
        // Iterate over each component
        for (List<Integer> indices : rootToComponent.values()) {
            // Sort the characters in the group
            List<Character> characters = new ArrayList<>();
            for (int index : indices) {
                characters.add(s.charAt(index));
            }
            //db was at position at 1-10
            // sort - bd => take b and put at 1 and take d and put at 10
            Collections.sort(characters);
            // Store the sorted characters
            for (int index = 0; index < indices.size(); index++) {
                smallestString[indices.get(index)] = characters.get(index);
            }
        }

        return new String(smallestString);
    }

    public static void main(String[] args) {
        MinimumSwaps ms = new MinimumSwaps();
        String s = "mxzy";
        List<List<Integer>> pairs = new ArrayList<>();
        pairs.add(new ArrayList<Integer>() { {add(0);add(3);}});
        pairs.add(new ArrayList<Integer>() { { add(1);add(2);}});
        pairs.add(new ArrayList<Integer>() { { add(0);add(2);}});

        String smallestString = ms.smallestStringWithSwaps(s, pairs);
        System.out.println("My smallest string : " + smallestString);
    }
}
