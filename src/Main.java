public class Main {
    public static void main(String[] args) {
        Graph graph = GraphLoader.loadGraph("src/graph.csv");

        GraphVisualizer visualizer = new GraphVisualizer(graph);
    }
}