//Jackson Lee

import java.io.*;
import java.util.*;

public class H2 {
    //Adjacency list representation of a direced graph
    //See the class discussion for the details of the representation
    
    private class VertexNode {
        private String name;
        private VertexNode nextV;
        private EdgeNode edges;
        
        private VertexNode(String n, VertexNode v) {
            name = n;
            nextV = v;
            edges = null;
        }
    }
    
    private class EdgeNode {
        private VertexNode vertex1;
        private VertexNode vertex2;
        private EdgeNode nextE;
        
        private EdgeNode(VertexNode v1, VertexNode v2, EdgeNode e) {
            vertex1 = v1;
            vertex2 = v2;
            nextE = e;
        }
    }
    
    private VertexNode vertices;
    private LinkedList<Integer> queue;
    private String[] vertexNames;
    private int[] indegrees;
    private int numVertices;
    
    public H2() {
        vertices = null;
        numVertices = 0;
    }
    
    public void addVertex(String s) {
        if(vertices == null || vertices.name.compareTo(s) > 0) {
            vertices = new VertexNode(s, vertices);
            return;
        }
        
        VertexNode temp = vertices;
        
        while(temp.nextV != null && temp.nextV.name.compareTo(s) < 0) {
            temp = temp.nextV;
        }
        
        temp.nextV = new VertexNode(s, temp.nextV);
    }
    
    public void addEdge(String n1, String n2) {
        VertexNode temp = vertices;
        
        while(temp.name.compareTo(n1) > 0 || temp.name.compareTo(n1) < 0) {
            temp = temp.nextV; 
        }
            
        VertexNode v1 = temp;
        
        temp = vertices;
        
        while(temp.name.compareTo(n2) > 0 || temp.name.compareTo(n2) < 0) {
            temp = temp.nextV; 
        }
            
        VertexNode v2 = temp;

        v1.edges = new EdgeNode(v1, v2, v1.edges);
        
        //Calculates indegrees every time an new edge is added
        for(int i = 0; i < numVertices; i++) {
            if(v2.name.equals(vertexNames[i])) {
                indegrees[i]++;
            }
        }
    }
    
    public String topoSort() {
        initQueue();
        String topo = "";
        int topoCount = 0;
        
        while(queue.size() != 0) {
            int i = queue.remove();
            topo = topo + vertexNames[i] + " ";
            updateIndegrees();
            topoCount++;
        }
        
        if(topoCount == numVertices) {
            return topo;
        }
        
        return "No topological ordering exists for the graph.";
    }
    
    //adds nodes with indegrees of zero to the queue
    private void initQueue() {
        for(int i = 0; i < numVertices; i++) {
            if(indegrees[i] == 0) {
                queue.add(i);
            }
        }
    }
    
    //looks through all the vertices and their edges and decrements the corresponding indegrees
    public void updateIndegrees() {
        VertexNode temp = vertices;
        
        for(int i = 0; i < numVertices; i++) {
            if(indegrees[i] == 0) {
                while(temp.edges != null) {
                    for(int j = 0; j < numVertices; j++) {
                        if(temp.edges.vertex2.name.compareTo(vertexNames[j]) == 0) {
                            indegrees[j]--;
                    
                            if(indegrees[j] == 0) {
                                queue.add(j);
                            }
                        }   
                    }
                    temp.edges = temp.edges.nextE;
                }
            }
            temp = temp.nextV;
        }
    }
    
    public static void main(String args[]) throws IOException {
        BufferedReader b = new BufferedReader(new FileReader(args[0]));
        String line = b.readLine();
        Scanner scanVertices = new Scanner(line);
        H2 h = new H2();
        h.queue = new LinkedList<Integer>();
        
        while(scanVertices.hasNext()) {
            String str = scanVertices.next();
            h.addVertex(str);
            h.numVertices++;
        }
        
        h.vertexNames = new String[h.numVertices];
        
        VertexNode temp = h.vertices;
        
        for(int i = 0; i < h.numVertices; i++) {
            h.vertexNames[i] = temp.name;
            temp = temp.nextV;
        }
        
        h.indegrees = new int[h.numVertices];
        
        for(int i  = 0; i < h.numVertices; i++) {
            h.indegrees[i] = 0;
        }
        
        line = b.readLine();
        
        while(line != null) {
            Scanner scanEdges = new Scanner(line);
            
            while(scanEdges.hasNext()) {
                String v1 = scanEdges.next();
                String v2 = scanEdges.next();
                h.addEdge(v1, v2);
            }
            line = b.readLine();
        }
        
        System.out.println(h.topoSort());
    }
}