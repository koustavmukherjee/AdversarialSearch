import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class homework {
	public static int SIZE;
	public static int FRUIT_TYPES;
	public static float TIME_REMAINING;
	public static char INIT_STATE[][];
	public static int MAX_DEPTH = 4;
	public static int MAX_BRANCHING_FACTOR = -1;
	public static boolean SEARCH_TERMINATED = false;
	public static float REMAINING_TIME_FOR_SEARCH;

	public static void main(String[] args) {
		//long start = System.nanoTime();
		readInput();
		Node node = new Node();
		node.setState(Util.cloneArray(INIT_STATE, SIZE));
		node.setType(true);
		node.setValue(0);
		node.setStars(Util.get_star_count(INIT_STATE, SIZE));
		List<Node> successors = node.get_successors();
		int cluster_count = node.getCluster_count();
		
		if(TIME_REMAINING > 200) {
			REMAINING_TIME_FOR_SEARCH = (float) (0.07 * TIME_REMAINING);
		}
		else {
			REMAINING_TIME_FOR_SEARCH = TIME_REMAINING * 4 / cluster_count;
		}
		
		if ((TIME_REMAINING < 15) || (cluster_count <= 1) || REMAINING_TIME_FOR_SEARCH <= 1) {
			MAX_BRANCHING_FACTOR = -1;
			MAX_DEPTH = 1;
			Node decision = Util.minimaxDecision(successors);
			if (decision != null) {
				writeOutput(decision);
			}
		}
		else {
			MAX_DEPTH = 1;
			Node bestDecision = null;
			Node prevDecision = null;
			int same_decision_count = 0;
			while(REMAINING_TIME_FOR_SEARCH > 0) {
				//System.out.println("Remaining Time for search : " + REMAINING_TIME_FOR_SEARCH);
				long iteration_start_time = System.nanoTime();
				Node decision = Util.minimaxDecision(successors);
				long iteration_end_time = System.nanoTime();
				float duration = (iteration_end_time - iteration_start_time) / 1000000000;
				REMAINING_TIME_FOR_SEARCH -= duration;
				MAX_DEPTH += 1;
				if(!SEARCH_TERMINATED) {
					prevDecision = bestDecision;
					bestDecision = decision;
					//System.out.println("Best Decision Depth : " + bestDecision.depth);
					//System.out.println("Best Decision Move : " + bestDecision.move[0] + bestDecision.move[1]);
					if(prevDecision != null && prevDecision.move[0] == bestDecision.move[0] && prevDecision.move[1] == bestDecision.move[1]) {
						same_decision_count++;
					}
					else {
						same_decision_count = 0;
					}
					if(same_decision_count == 5) {
						break;
					}
				}
				else {
					//System.out.println("Search Aborted at depth : " + MAX_DEPTH);
				}
				if(bestDecision == null) {
					bestDecision = decision;
				}
			}
			if (bestDecision != null) {
				writeOutput(bestDecision);
			}
		}
		
		
		//long end = System.nanoTime();
		//System.out.println("Total time : " + ((end - start) / 1000000000));
	}

	public static void readInput() {
		try {
			File f = new File("./input.txt");
			BufferedReader br = new BufferedReader(new FileReader(f));
			SIZE = Integer.parseInt(br.readLine().trim());
			FRUIT_TYPES = Integer.parseInt(br.readLine().trim());
			TIME_REMAINING = Float.parseFloat(br.readLine().trim());
			INIT_STATE = new char[SIZE][SIZE];
			for (int i = 0; i < SIZE; i++) {
				String line = br.readLine().trim();
				for (int j = 0; j < SIZE; j++) {
					char ch = line.charAt(j);
					INIT_STATE[i][j] = ch;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeOutput(Node decision) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		int[] move = decision.getMove();
		char state[][] = decision.getState();
		try {
			fw = new FileWriter("./output.txt");
			bw = new BufferedWriter(fw);
			bw.write((char) (((int) 'A') + move[1]) + "" + ((int) move[0] + 1));
			bw.write(System.getProperty("line.separator"));
			for (int i = 0; i < SIZE; i++) {
				bw.write(state[i]);
				if (i < SIZE - 1)
					bw.write(System.getProperty("line.separator"));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}