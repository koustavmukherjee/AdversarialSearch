import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class Node implements Comparable<Node> {
	char[][] state;
	int depth;
	int value;
	int move[] = new int[2];
	boolean type;
	int stars;
	int clusters_consumed;
	public static int GET_SUCCESSOR_CALLED = 0;
	int cluster_count;

	public boolean isCutOffState() {
		if (homework.MAX_DEPTH == -1)
			return this.stars == homework.SIZE * homework.SIZE;
		return this.stars == homework.SIZE * homework.SIZE || this.depth == homework.MAX_DEPTH;
	}

	public List<Node> get_successors() {
		GET_SUCCESSOR_CALLED += 1;
		List<Node> successors = new ArrayList<>();
		Set<Integer> visited = new HashSet<>();
		int cluster_count = 0;
		for (int i = 0; i < homework.SIZE; i++) {
			for (int j = 0; j < homework.SIZE; j++) {
				if (!visited.contains(i * homework.SIZE + j) && this.state[i][j] != '*') {
					Node successor = new Node();
					Set<Integer> cluster = new HashSet<>();
					successor.setState(Util.cloneArray(this.state, homework.SIZE));
					cluster = Util.get_cluster(this.state[i][j], i, j, cluster, successor.state, homework.SIZE);
					cluster_count ++;
					successor.setClusters_consumed(cluster.size());
					visited.addAll((cluster));
					successor.move[0] = i;
					successor.move[1] = j;
					successor.depth = this.depth + 1;
					int points = cluster.size() * cluster.size();
					successor.value = this.value + (this.type ? points : -1 * points);
					successor.type = !this.type;
					successor.stars = this.stars + cluster.size();
					successors.add(successor);
				}
			}
		}
		this.cluster_count = cluster_count;
		int limit = homework.MAX_BRANCHING_FACTOR == -1 ? cluster_count : homework.MAX_BRANCHING_FACTOR;
		Collections.sort(successors);
		List<Node> clusters_considered = new ArrayList<>();
		for(int i = 0; i < Math.min(limit, successors.size()); i++) {
			Node successor = successors.get(i);
			Util.gravitate(successor.state, homework.SIZE);
			clusters_considered.add(successor);
		}		
		return clusters_considered;
	}

	public char[][] getState() {
		return state;
	}

	public void setState(char[][] state) {
		this.state = state;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int[] getMove() {
		return move;
	}

	public void setMove(int[] move) {
		this.move = move;
	}

	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public int getClusters_consumed() {
		return clusters_consumed;
	}

	public void setClusters_consumed(int clusters_consumed) {
		this.clusters_consumed = clusters_consumed;
	}

	public int getCluster_count() {
		return cluster_count;
	}

	public void setCluster_count(int cluster_count) {
		this.cluster_count = cluster_count;
	}

	@Override
	public int compareTo(Node o) {
		return this.clusters_consumed > o.clusters_consumed ? -1
				: (this.clusters_consumed < o.clusters_consumed ? 1 : 0);
	}

}