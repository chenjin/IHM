package NetworkInformation.DataTransformation;

import NetworkInformation.GraphInformation.GraphData;
import NetworkInformation.GraphInformation.GraphforAllData;




public class GenerateData {

	public String graph(int i) throws Exception {
		DataMatrix dm = new DataMatrix();
		int[][][] matrix = dm.XlsToMatrix("F://data.xls");
		Compute cp = new Compute();
		int[] degree = cp.ComputeNodeDegree(matrix[i]);
		ToJson tj = new ToJson();
		String jsonGraph = "";
		jsonGraph = tj.makeJson(matrix[i], degree);
		System.out.println(jsonGraph);
		return jsonGraph;
	}

	public String core(int i) throws Exception {
		DataMatrix dm = new DataMatrix();
		int[][][] matrix = dm.XlsToMatrix("F://data.xls");
		Compute cp = new Compute();
		int[] core = cp.calculatecore(matrix[i]);
		String jsonCore = coreNodes(to2(core));
		System.out.println(jsonCore);
		return jsonCore;
	}

	public String y(int i) throws Exception {
		DataMatrix dm = new DataMatrix();
		int[][][] matrix = dm.XlsToMatrix("F://data.xls");
		Compute cp = new Compute();
		int[] degree = cp.ComputeNodeDegree(matrix[i]);
		int[][] fenbu = cp.degreeFenbu(degree);
		fenbu = sort(fenbu);
		String[] ddd = print(fenbu);
		String y = ddd[0];
		return addSE(y);
	}

	public String x(int i) throws Exception {
		DataMatrix dm = new DataMatrix();
		int[][][] matrix = dm.XlsToMatrix("F://data.xls");
		Compute cp = new Compute();
		int[] degree = cp.ComputeNodeDegree(matrix[i]);
		int[][] fenbu = cp.degreeFenbu(degree);
		fenbu = sort(fenbu);
		String[] ddd = print(fenbu);
		String x = ddd[1];
		return addSE(x);
	}

	public String asl(int i) throws Exception {
		DataMatrix dm = new DataMatrix();
		int[][][] matrix = dm.XlsToMatrix("F://data.xls");
		Compute cp = new Compute();
		double asl = cp.CalAspl(matrix[i]);
		return Double.toString(asl);
	}

	public String cc(int i) throws Exception {
		DataMatrix dm = new DataMatrix();
		int[][][] matrix = dm.XlsToMatrix("F://data.xls");
		Compute cp = new Compute();
		double cc = cp.ComputeCC(matrix[i]);
		return Double.toString(cc);
	}

	public GraphforAllData generateUIData() throws Exception {
		GraphforAllData ad = new GraphforAllData();
		DataMatrix dm = new DataMatrix();
		int[][][] matrix = dm.XlsToMatrix("F://data.xls");

		for (int i = 0; i < 3; i++) {
			GraphData temp = new GraphData();
			
			String jsonGraph = graph(i);
			String jsonCore = core(i);
			String asl = asl(i);
			String cc = cc(i);
			String x = x(i);
			String y = y(i);		
			
			temp.setCoreData(jsonCore);
			temp.setAvg_length(asl);
			temp.setCoffient(cc);
			temp.setXdegree(x);
			temp.setYdegree(y);

			temp.setGraphData(jsonGraph);
			if(i == 0)
				ad.setName(temp);
			else if (i == 1)
				ad.setHome(temp);
			else
				ad.setDialect(temp);
		}

		return ad;
	}

	public String[] print(int[][] m) {
		String[] result = new String[2];
		result[0] = "";
		result[1] = "";
		System.out.println(m.length);
		for (int i = 0; i < m.length - 1; i++) {
//			System.out.print(m[i][0] + ",");
			result[0] += m[i][0] + ",";
			result[1] += m[i][1] + ",";
			// System.out.println();
		}
		result[0] += m[m.length - 1][0];
		result[1] += m[m.length - 1][1];

//		System.out.println();
		return result;
	}

	// string[] core translate to core fenbu int[node][core]
	public int[][] to2(int[] core) {
		int[][] result = new int[core.length][2];
		for (int i = 0; i < core.length; i++) {
			result[i][0] = i;
			result[i][1] = core[i];
		}
		return result;
	}

	public String coreNodes(int[][] core) {
		if (core.length == 1)
			return addSE(toJSArray(core[0][1], core[0][0] + 1));
		else if (core.length > 1) {
			String temp = "";
			for (int i = 0; i < core.length - 1; i++) {
				temp += toJSArray(core[i][1], core[i][0] + 1) + ",";
			}
			temp += toJSArray(core[core.length - 1][1],
					core[core.length - 1][0] + 1);
			return addSE(temp);
		} else
			return null;
	}

	public String addSE(String in) {
		return "[" + in + "]";
	}

	public String toJSArray(int i, int j) {
		return addSE(Integer.toString(i) + "," + Integer.toString(j));
	}

	public int[][] sort(int[][] m) {
		int n = m.length;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (m[j][0] > m[j + 1][0]) {
					int temp = m[j + 1][0];
					int temp2 = m[j + 1][1];
					m[j + 1][0] = m[j][0];
					m[j + 1][1] = m[j][1];
					m[j][0] = temp;
					m[j][1] = temp2;

				}
			}
		}
		return m;
	}

	public static void main(String[] args) throws Exception {
		GenerateData gd = new GenerateData();
		// int[][] a = { { 1, 1 }, { 1, 1 }, { 1, 1 } };
		// System.out.println(gd.coreNodes(a));
//		AllData temp = gd.generateUIData();
		int [][] m = { { 0, 1, 1, 0, 1 }, { 1, 0, 1, 1, 0 },
				{ 1, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0 } };
		Compute cp = new Compute();
		int[] degree = cp.ComputeNodeDegree(m);
		int[][] fenbu = cp.degreeFenbu(degree);
		fenbu = gd.sort(fenbu);
		String[] ddd = gd.print(fenbu);
		String y = ddd[0];
		System.out.println(y);
	}
}
