import java.util.List;
import java.util.Set;

public class Util {
	public static long SEARCH_START_TIME;
	public static Set<Integer> get_cluster(char fruit, int x, int y, Set<Integer> cluster, char state[][], int size) {

		if (cluster.contains(x * size + y))
			return cluster;

		if (state[x][y] != fruit)
			return cluster;

		cluster.add(x * size + y);
		state[x][y] = '*';

		/* left */
		if (x - 1 >= 0)
			get_cluster(fruit, x - 1, y, cluster, state, size);

		/* right */
		if (x + 1 < size)
			get_cluster(fruit, x + 1, y, cluster, state, size);

		/* up */
		if (y - 1 >= 0)
			get_cluster(fruit, x, y - 1, cluster, state, size);

		/* down */
		if (y + 1 < size)
			get_cluster(fruit, x, y + 1, cluster, state, size);

		return cluster;
	}

	public static char[][] gravitate(char state[][], int size) {
		for (int i = 0; i < size; i++) {
			int end = size - 1;
			int start = size - 1;
			while (end >= 0) {
				if (state[end][i] == '*') {
					end -= 1;
				} else {
					if (start == end) {
						end -= 1;
						start -= 1;
					} else {
						state[start][i] = state[end][i];
						state[end][i] = '*';
						end -= 1;
						start -= 1;
					}
				}
			}
		}
		return state;
	}

	public static char[][] cloneArray(char old[][], int size) {
		char[][] current = new char[size][size];
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				current[i][j] = old[i][j];
		return current;
	}

	public static int maxValue(Node node, int alpha, int beta) {
		long current_time = System.nanoTime();
		float duration_elapsed = (current_time - SEARCH_START_TIME) / 1000000000;
		if(duration_elapsed > homework.REMAINING_TIME_FOR_SEARCH) {
			homework.SEARCH_TERMINATED = true;
			return node.getValue();
		}
		if (node.isCutOffState())
			return node.getValue();
		int v = Integer.MIN_VALUE;
		for (Node successor : node.get_successors()) {
			v = Math.max(v, Util.minValue(successor, alpha, beta));
			if (v >= beta)
				return v;
			alpha = Math.max(alpha, v);
		}
		return v;
	}

	public static int minValue(Node node, int alpha, int beta) {
		long current_time = System.nanoTime();
		float duration_elapsed = (current_time - SEARCH_START_TIME) / 1000000000;
		if(duration_elapsed > homework.REMAINING_TIME_FOR_SEARCH) {
			homework.SEARCH_TERMINATED = true;
			return node.getValue();
		}
		if (node.isCutOffState())
			return node.getValue();
		int v = Integer.MAX_VALUE;
		for (Node successor : node.get_successors()) {
			v = Math.min(v, Util.maxValue(successor, alpha, beta));
			if (v <= alpha)
				return v;
			beta = Math.min(beta, v);
		}
		return v;
	}

	public static int minimax_value(Node node, int alpha, int beta) {
		if (node.type)
			return Util.maxValue(node, alpha, beta);
		else
			return Util.minValue(node, alpha, beta);
	}

	public static Node minimaxDecision(List<Node> successors) {
		SEARCH_START_TIME = System.nanoTime();
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < successors.size(); i++) {
			Node successor = successors.get(i);
			int value = Util.minimax_value(successor, alpha, beta);
			if (value > alpha) {
				alpha = value;
				index = i;
			}
		}
		return successors.get(index);
	}

	public static int get_star_count(char state[][], int size) {
		int star_count = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (state[i][j] == '*') {
					star_count += 1;
				}
			}
		}
		return star_count;
	}
}