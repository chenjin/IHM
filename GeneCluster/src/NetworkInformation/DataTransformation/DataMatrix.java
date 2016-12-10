package NetworkInformation.DataTransformation;


import jxl.Sheet;

public class DataMatrix {

	// after read xls file, we got three adjacent of graph to calculate the
	// network properties
	public int toInt(String str) {
		if (str.toLowerCase().equals("y"))
			return 1;
		else
			return 0;
	}

	public int[][][] XlsToMatrix(String filename) throws Exception {
		XlsUtil xu = new XlsUtil(filename);
		Sheet[] s = xu.ReadXlss();
		int size = s[0].getRows() - 1;
		int[][] name = new int[size][size];
		int[][] hometown = new int[size][size];
		int[][] dialect = new int[size][size];
//		System.out.println("Start");
		for (int j = 0; j < size; j++) {
			for (int k = 0; k < size; k++) {
				name[j][k] = toInt(s[0].getCell(k + 1, j + 1).getContents());
//				System.out.print(name[j][k]);
			}
//			System.out.println();
		}
//		System.out.println("finish");
		for (int j = 0; j < size; j++) {
			for (int k = 0; k < size; k++) {
				hometown[j][k] = toInt(s[1].getCell(k+ 1, j + 1).getContents());
			}
		}
		for (int j = 0; j < size; j++) {
			for (int k = 0; k < size; k++) {
				dialect[j][k] = toInt(s[2].getCell(k + 1, j + 1).getContents());
				System.out.print(s[2].getCell(k + 1, j + 1).getContents());
			}
			System.out.println();
		}
		
		name = forceConnect(name);
		hometown = forceConnect(hometown);
		dialect = forceConnect(dialect);
//		for (int k = 0; k < dialect.length; k++) {
//		for (int j = 0; j < dialect.length; j++) {
//			System.out.print(dialect[k][j]);
//		}
//			System.out.println();
//		}
//		System.out.println();
		int[][][] resultMatrix = new int[3][size][size];
		resultMatrix[0] = name;
		resultMatrix[1] = hometown;
		resultMatrix[2] = dialect;
		return resultMatrix;
	}

	public int[][] forceConnect(int[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < i; j++) {
				if (m[i][j] != m[j][i]) {
					m[i][j] = 1;
					m[j][i] = 1;
				}
			}
			//m[i][j] to connected
			for(int k = 0;k<m.length;k++){
				if(isolate(k,m) == 0){
					m =know(k,m);
				}
			}
		}
		return m;
	}
	public int[][] know(int i,int[][] m){
		Compute c = new Compute();
		int[] degree = c.ComputeNodeDegree(m);
		int j = findMax(degree);
		m[i][j] = 1;
		m[j][i] = 1;
		return m;
	}
	public int findMax(int[] d){
		int maxLoc = 0;
		for(int i=1;i<d.length;i++){
			if(d[i] > d[maxLoc]){
				maxLoc = i;
			}
		}
		return maxLoc;
	}
	//problem
	public int isolate(int k,int[][] m){
		int count=0;
		for(int i=0;i<m.length;i++){
			if(m[k][i] != 0 || m[i][k] != 0)
				count++;
		}
		return count;
	}

	public static void main(String[] args) throws Exception {
		DataMatrix dm = new DataMatrix();
		int[][] i = dm.XlsToMatrix("F://data.xls")[2];
		System.out.println(i.length);
//		for (int k = 0; k < i.length; k++) {
//			for (int j = 0; j < i.length; j++) {
//				System.out.print(i[k][j]);
//			}
//			System.out.println();
//		}
	}
}
