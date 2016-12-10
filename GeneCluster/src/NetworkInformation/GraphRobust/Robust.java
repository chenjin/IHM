package NetworkInformation.GraphRobust;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import NetworkInformation.DataTransformation.Compute;
import NetworkInformation.DataTransformation.MaxSubGraph;


public class Robust {
	public int[] randomList(int num, List<Integer> done) {
		int i = 0;
		int[] result = new int[num];
		while (i < num) {
			int temp = new Random().nextInt(50);
			if (!done.contains(temp)) {
				done.add(temp);
				result[i] = temp;
				i++;
			}
		}
		return result;

	}

	public int[] calcDegree(int[][] m) {
		Compute cp = new Compute();
		return cp.ComputeNodeDegree(m);
	}

	public int[] findMaxK(int k, int[][] m, List<Integer> done) {
		int[] result = new int[k];
		int[] degree = calcDegree(m);
		for (int i = 0; i < k; i++) {
			int temp = findMaxWithoutDone(degree, done);
			result[i] = temp;
			done.add(temp);
		}
		return result;
	}

	public int findMaxWithoutDone(int[] degree, List<Integer> done) {
		int maxLoc = 0;
		for (int i = 0; i < degree.length; i++) {
			if (!done.contains(i)) {
				maxLoc = i;
			}
		}
		int max = degree[maxLoc];
		for (int i = 0; i < degree.length; i++) {
			if (!done.contains(i) && degree[i] > max) {
				maxLoc = i;
				max = degree[i];
			}
		}
		return maxLoc;
	}

	public int[][] deleteNodes(int[][] m, int[] dels) {
		for (int i = 0; i < dels.length; i++) {
			for (int j = 0; j < m.length; j++) {
				m[i][j] = 0;
				m[j][i] = 0;
			}
		}
		return m;
	}

	public double calcAsl(int[][] m) {
		Compute cp = new Compute();
		return cp.CalAspl(m);
	}

	public int calcMsg(int[][] m) {
		MaxSubGraph msg = new MaxSubGraph();
		return msg.maxS(m);
	}

	public double[][] randomAttack(int[][] m) {
		int[][] temp = m;
		double[][] result = new double[m.length][2];
		List<Integer> done = new ArrayList<Integer>();
		for (int i = 1; i < m.length + 1; i++) {
			int[] dels = randomList(i, done);
			temp = deleteNodes(temp, dels);
			result[i - 1][0] = (double) (calcMsg(temp));
			result[i - 1][1] = calcAsl(temp);
		}
		return result;
	}

	public double[][] intendAttack(int[][] m) {
		int[][] temp = m;
		double[][] result = new double[m.length][2];
		List<Integer> done = new ArrayList<Integer>();
		for (int i = 1; i < m.length + 1; i++) {
			int[] dels = findMaxK(i, m, done);
			temp = deleteNodes(temp, dels);
			result[i - 1][0] = (double) (calcMsg(temp));
			result[i - 1][1] = calcAsl(temp);
		}
		return result;
	}

	public double[] x(int size) {
		double[] result = new double[size];
		for (int i = 0; i < size; i++) {
			result[i] = (double) (i + 1) / (double) size;
		}
		return result;
	}

	public double[] s(double[][] random) {
		double[] result = new double[random.length];
		for (int i = 0; i < random.length; i++) {
			result[i] = random[i][0];
		}
		return result;
	}

	public double[] l(double[][] random) {
		double[] result = new double[random.length];
		for (int i = 0; i < random.length; i++) {
			result[i] = random[i][1];
		}
		return result;
	}

	public String doubleToString(double[] x, double[] y) {
		String result = "";
		for (int i = 0; i < x.length - 1; i++) {
			result += addSE(String.format("%.2f", x[i]) + ","
					+ String.format("%.2f", y[i]))
					+ ",";
		}
		result = result
				+ addSE(String.format("%.2f", x[x.length - 1]) + ","
						+ String.format("%.2f", y[x.length - 1]));
		return addSE(result);
	}

	public String addSE(String in) {
		return "[" + in + "]";
	}

	public double[] fakeX() {
		double[] result = new double[20];
		double start = 0.0;
		for (int i = 0; i < 20; i++) {
			result[i] = start;
			start += 0.04;
		}
		return result;
	}

	public double[] fakeY(double[] x) {
		double[] result = new double[20];
		for (int i = 0; i < 20; i++) {
			result[i] = 1.0 - ((double) 5 / (double) 4) * x[i];
		}
		return result;
	}

	public String Out(double[] x, double[] y) {
		return doubleToString(x, y);
	}
}
