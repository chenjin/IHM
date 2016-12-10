package NetworkInformation.DataTransformation;


import java.util.ArrayList;
import java.util.List;


public class Compute {

	// compute node-degree distribution
	//@param matrix[][] ͼ����ֲ�
	//@return result[] �ڵ����
	public int[] ComputeNodeDegree(int[][] matrix) {
		//��ÿ���ڵ�Ķ�
		int size = matrix[0].length;
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			int degree = 0;
			for (int j = 0; j < size; j++) {
				if (matrix[i][j] != 0) {
					degree++;
				}

			}
			result[i] = degree;
		}
		return result;
	}
	//ƽ���Ƚڵ�
	//@param matrix[][] ͼ�ڽӾ���
	public void avgdegree(int[][] matrix) {
		double avgdeg = 0;
		double sum = 0;
		int size = matrix[0].length;
		Compute s = new Compute();
		int[] res = s.ComputeNodeDegree(matrix);
		for (int i = 0; i < size; i++) {
			sum += res[i];

		}
		avgdeg = sum / size;
		System.out.println("average node_degree:  " + avgdeg);

	}
	
	//����ĳ���ض������Ľڵ����
	//@param data[] �ڵ��
	//@return count[] ĳ��������Ƶ��
	public int find(int[] data, int dest) {
		int count = 0;
		for (int i = 0; i < data.length; i++) {
			if (data[i] == dest)
				count++;
		}
		return count;
	}
	//�����ж��в�ͬ�Ķȳ��ֵ�Ƶ��
	//@param result[] ÿ���ڵ��Ӧ�Ķ���
	//@return out[][] ���нڵ��г��ֲ�ͬ�ĶȵĶ�Ӧ��Ƶ��
	public int[][] degreeFenbu(int[] result) {
		List<Integer> degrees = new ArrayList<Integer>();
		int size = result.length;
		//degree[] ���治ͬ�Ķ� ��Ӧ��ͬ�Ķ�
		for (int i = 0; i < size; i++) {
			if (!degrees.contains(result[i])) {
				degrees.add(result[i]);
			}
		}
		//fenbu ���治ͬ�ĸ��� 1 -> 2; ��Ϊ1����2��
		int[] fenbu = new int[degrees.size()];
		for (int j = 0; j < degrees.size(); j++) {
			fenbu[j] = find(result, degrees.get(j));
		}
		//out[][] ��ͬ�ȳ��ֵ�Ƶ��
		int[][] out = new int[fenbu.length][2];
		for (int m = 0; m < fenbu.length; m++) {
			out[m][0] = degrees.get(m);
			out[m][1] = fenbu[m];
		}
//		System.out.println("node-degree distribution:");
		for (int k = 0; k < degrees.size(); k++) {
//			System.out.println("degree:" + degrees.get(k) + "\t"
//					+ "distribution:" + fenbu[k]);
		}
		return out;
	}
	//����ͼ��ƽ��·������
	public double CalAspl(int[][] matrix) {
		int sum = 0;
		int size2 = matrix[0].length;
		int size = matrix[0].length;
		int[][] dis = new int[size][size];
		for (int q = 0; q < size; q++) {
			for (int w = 0; w < size; w++) {
				dis[q][w] = matrix[q][w];
			}
		}
		//Ϊ������� dis[][]��ֵ
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (dis[i][j] == 0 && i != j)
					dis[i][j] = 999;
			}
		}
		//dis[][] �����С�������
		for (int k = 0; k < size; k++) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (dis[i][j] > dis[i][k] + dis[k][j]) {
						dis[i][j] = dis[i][k] + dis[k][j];
					}

				}
			}
		}
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				sum += dis[i][j];
			}

		}
		return ((double) (2 * sum)) / ((double) (size2 * (size - 1)));

	}

	// divide and conquer ���η����ϵ��
	//@param matrix ͼ�ڽӾ���
	public double ComputeClustringCoefficient(int[][] matrix) {
		int size = matrix[0].length;
		double[] c = new double[size];
		int[] e = new int[size];
		int[] k = new int[size];
		double C1 = 0;
		double C = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (matrix[i][j] != 0) {
					k[i]++;
					for (int t = j; t < size; t++) {
						if (matrix[i][t] != 0 && matrix[j][t] != 0) {
							e[i]++;
						}
					}

				}

			}
			if (k[i] * (k[i] - 1) == 0)
				c[i] = 0.0;
			else
				c[i] = (double) (2 * e[i]) / (double) (k[i] * (k[i] - 1));
			System.out.println(c[i]);
		}
		for (int i = 0; i < size; i++) {
			C1 += c[i];
		}
		C = (double) C1 / (double) size;
		return C;
	}
	//�ҵ�ĳһ��id��ǰsize������
	//@param size ??????????
	//@param row ��ĳһ���˵ĳ���
	//@return out ��row���˵�ǰsize����ʶ���˵�id
	public int[] findNodes(int row, int size, int[][] matrix) {
		int[] out = new int[size];
		int k = 0;
		for (int j = 0; j < matrix[0].length; j++) {
			if (matrix[row][j] != 0) {
				out[k] = j;
				k++;
			}
		}
		return out;
	}
	//���������
	//@param nodes ĳ������ʶ���˵�id
	
	public int checkConnection(int[] nodes, int[][] matrix) {
		//total number of edge times
		int times = nodes.length * (nodes.length - 1) / 2;
		int count = 0;
		for (int i = 0; i < nodes.length - 1; i++) {
			for (int j = i + 1; j < nodes.length; j++) {
				if (matrix[nodes[i]][nodes[j]] != 0) {
					count++;
				}
			}
		}
		return count;
	}
	//@param list ??????
	public double sum(double[] list) {
		double sum = 0.0;
		for (double temp : list) {
			sum += temp;
		}
		return sum;
	}
	//����ÿ��node��C
	public double ComputeCC(int[][] matrix) {
		int[] degrees = ComputeNodeDegree(matrix);
		double[] ci = new double[degrees.length];
		for (int i = 0; i < degrees.length; i++) {
			int[] nodes = new int[degrees[i]];
			nodes = findNodes(i, degrees[i], matrix);
			int ei = checkConnection(nodes, matrix);
			int fenmu = (degrees[i] * (degrees[i] - 1));
			if (fenmu != 0)
				ci[i] = (double) (2 * ei) / (double) fenmu;
			else
				ci[i] = 0;
		}
		return (sum(ci) / (double) degrees.length);
	}

	public int[][] degreeloc(int matrix[][]){
		int size = matrix[0].length;
		int [][]degloc = new int [size][2];
		for (int i = 0; i < size; i++) {
			int degree = 0;
			for (int j = 0; j < size; j++) {
				if (matrix[i][j] != 0) {
					degree++;
				}

			}
			degloc[i][0] = degree;
			degloc[i][1] = i;
		}
		return degloc;
	}
	//���� ?????
	//@param d[][] ?????
	public boolean corefinish(int d[][]){
		boolean flag = true;
		for(int i = 0;i< d.length;i++){
			if(d[i][0]!= 0){
				flag = false;
			}
		}
		return flag;
	}
	//�ȱȽ�
	public int degreecompare(int matrix[][],int d[][],int m){
		int size = matrix[0].length;
		for(int j= 0;j < size;j++){
			if((d[j][0] <m)&&(d[j][0]) !=0)
				return j;
		}
		
		return -1;
	}
	//�������Ķ�
	public int[] calculatecore(int matrix[][]){
		int size = matrix[0].length;
		int []core =new int [size];
		int count = 1;
		int [][]d =degreeloc(matrix);
		while(!corefinish(d)){
			while(degreecompare(matrix,d,count) != -1){
				int loc = degreecompare(matrix,d,count);
				for(int i =0; i< size;i++){
					matrix[loc][i] = 0;
					matrix[i][loc] = 0;
				}			
				core[loc] = count-1;
//				System.out.println("the core of node  "+(loc+1)+"  is  "+core[loc]);
				d =degreeloc(matrix);
				
			}
			count++;
		}
		return core;
		
	}

	// find smallest and same degree node
	// remove smallest degree node
	// update graph
	// record this coreness
	// to step 1
	// findSmallest location not in list
	public int findMin(int[] degree, List<Integer> done) {
		for (int i = 0; i < degree.length; i++) {
			if (!done.contains(degree[i]))
				return i;
		}

		return degree.length - 1;

	}

	public List<Integer> findSmallLocation(int[] degree, List<Integer> done) {
		List<Integer> result = new ArrayList<Integer>();
		int loc = findMin(degree, done);
		int min = degree[loc];
		// int loc = 0;
		for (int i = loc + 1; i < degree.length; i++) {
			if (!done.contains(i)) {
				if (degree[i] < min) {
					min = degree[i];
					loc = i;
				}
			}

		}
		int small = degree[loc];
		result.add(loc);
		for (int i = 0; i < degree.length; i++) {
			if ((!done.contains(i)) && (i != loc)) {
				if (degree[i] == small)
					result.add(i);
			}
		}
		return result;
	}

	public List<Integer> findSmallest(int[][] matrix, List<Integer> done) {
		int[] degrees = ComputeNodeDegree(matrix);
		List<Integer> locs = findSmallLocation(degrees, done);
		return locs;
	}

	public int[][] updateMatrix(List<Integer> nodes, int[][] matrix) {
		for (int node : nodes) {
			for (int i = 0; i < matrix.length; i++) {
				matrix[node][i] = 0;
				matrix[i][node] = 0;
			}
		}
		return matrix;
	}

	public int degreeAtLoc(int loc, int[][] matrix) {
		int count = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (matrix[loc][i] != 0)
				count++;
		}
		return count;
	}

	public List<Integer> remove(int value, List<Integer> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == value) {
				list.remove(i);
			}
		}
		return list;
	}

	public int[][] ComputeCore(int[][] matrix) {
		String[] cores = new String[matrix.length];
		int[][] result = new int[matrix.length][2];
		int count = 0;
		List<Integer> done = new ArrayList<Integer>();
		List<Integer> nodes = new ArrayList<Integer>();
		for (int i = 0; i < matrix.length; i++) {
			nodes.add(i);
		}
		while (nodes.size() > 0) {

			List<Integer> loc = findSmallest(matrix, done);
			for (int temp : loc) {
				done.add(temp);
				int degree = degreeAtLoc(temp, matrix);
				cores[temp] = Integer.toString(temp + 1) + "\t"
						+ Integer.toString(degree);
				result[temp][0] = temp;
				result[temp][1] = degree;
				nodes = remove(temp, nodes);

				System.out.println("node has been proceeded:" + temp);
				 count++;

			}

			matrix = updateMatrix(loc, matrix);

		}
		System.out.println(count);
		return result;
	}

	public static void main(String[] args) throws Exception {
		 int [][] matrix = { { 0, 1, 1, 0, 1 }, { 1, 0, 1, 1, 0 },
		 { 1, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0 } };
//		 System.out.println(matrix[0].length);
		DataMatrix dm = new DataMatrix();
		int[][] data = dm.XlsToMatrix("F://data.xls")[0];

		Compute cp = new Compute();
		
		int[] core = cp.calculatecore(data);
		 for(int i=0;i<core.length;i++){
		 System.out.println(core[i]);
		 }
		
	}

}
