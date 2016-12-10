package NetworkInformation.DataTransformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MaxSubGraph {
	static final int MAXNUM = 100;
	static final int MAXVEX = 100;
	static final int TRUE = 1;
	static final int FALSE = 0;
	static int vexnum;// 椤剁偣鏁扮洰
	static int pd[][] = new int[MAXVEX][MAXVEX];// 鐭╅樀
	static int category = 0;// 寮鸿繛閫氬垎鏀殑绫诲埆
	static int weight = 0;// 寮鸿繛閫氬垎鏀殑瀛愯妭鐐瑰悕绉�
	static int array[][] = new int[MAXVEX][MAXVEX];// 寮鸿繛閫氬垎閲忓垎绫诲瓨鍌�
	static int visited[] = new int[MAXVEX];// 璁块棶鏍囧織浣�
	static int finished[] = new int[MAXVEX];// 璁块棶鏍囧織浣�
	static int count = 0;// 璁℃暟
	static int maxweight = 0;// 鏈�ぇ寮鸿繛閫氬垎鏀殑瀛愯妭鐐瑰悕绉�
	static int maxcategory = 0;// 鏈�ぇ寮鸿繛閫氬垎鏀殑绫诲埆

	// 瀵瑰浘娣卞害浼樺厛閬嶅巻鍑芥暟
	static void DFSraverse() {
		int v;// 椤剁偣
		count = 0;// visited[v]鏁扮粍瀛樺偍璁℃暟
		for (v = 0; v < vexnum; v++)
			visited[v] = FALSE;
		for (v = 0; v < vexnum; ++v) {
			if (visited[v] == 0)
				DFS(v);// 娣卞害閫掑綊鎼滅储
		}
	}

	// 浠庣v涓《鐐瑰嚭鍙戦�褰掑湴娣卞害浼樺厛鎼滅储鍑芥暟
	static void DFS(int v) {
		int w;
		visited[v] = TRUE;// 琛ㄦ槑宸茶闂�
		for (w = 0; w < vexnum; w++)
			if (visited[w] == 0 && pd[v][w] == 1)// 鏈闂殑鐐瑰拰鎸囧悜鐨勮竟
			{
				DFS(w);// 鍐嶆娣卞害鎼滅储
			}
		finished[count++] = v;
	}

	// 瀵瑰浘閫嗘繁搴︿紭鍏堥亶鍘嗗嚱鏁�
	static void CDFSraverse() {
		int v;// 椤剁偣
		for (v = 0; v < vexnum; v++)
			visited[v] = FALSE;
		for (v = finished[0]; v < count; v++) {
			if (visited[v] == 0) {
				array[category][weight] = v;
				weight++;
				CDFS(v);
				if (weight > maxweight) {
					maxweight = weight;// 瀛樺偍缁撶偣鏁版渶澶у�
					maxcategory = category;// 瀛樺偍缁撶偣鏁版渶澶у�瀵瑰簲鐨勭被鍒悕绉�
				}
				category++;// 寮鸿繛閫氬垎閲忕被鍒姞1
				weight = 0;// 绫诲埆鍔�鍚庢竻闆�
			}
		}
	}

	// 浠庣v涓《鐐瑰嚭鍙戦�褰掑湴閫嗘繁搴︿紭鍏堟悳绱�
	static void CDFS(int v) {
		int w;
		visited[v] = TRUE;
		for (w = 0; w < vexnum; w++) {
			if (visited[w] == 0 && pd[v][w] == 1) {
				CDFS(w);
				array[category][weight] = w;
				weight++;

			}
		}
	}

	public int maxS(int[][] matrix) {
		int i, j;
		for (i = 0; i < MAXVEX; i++)
			for (j = 0; j < MAXVEX; j++)
				array[i][j] = -1;// 鍒濆鍖栬妭鐐圭被鍒煩闃�
		pd = matrix;
		vexnum = matrix.length;
//		System.out.println("start!");// 寮�
//		System.out.println("绗竴娆℃繁搴︽悳绱㈢粨鏋滐細");
		for (i = 0; i < vexnum; ++i)// 杈撳嚭finished鏁扮粍
		{
			visited[i] = 0;// 鍒濆鍖栧凡璁块棶鏍囧織浣�
			finished[i] = 0;// 鍒濆鍖栫敤鏉ュ瓨鍌ㄧ涓�娣卞害鎼滅储寰楀埌鐨勬暟鎹殑鏁扮粍
		}
		DFSraverse();// 绗竴娆℃繁搴︽悳绱�
		for (i = 0; i < vexnum; i++) {
//			System.out.println(finished[i]);// 鏄剧ず绗竴娆℃繁搴︽悳绱㈠緱鍒扮殑鏁版嵁
		}

//		System.out.println("\n");
		for (i = 0; i < vexnum; ++i)//
		{
			visited[i] = 0;
		}
		CDFSraverse();// 绗簩娆￠�鍚戞繁搴︽悳绱�

//		System.out.println("绗簩娆￠�鍚戞繁搴︽悳绱㈠悗锛�);
		for (i = 0; i < category; i++) {
//			System.out.println("\n寮鸿繛閫氱" + (i + 1) + "鍒嗘敮鏄�");
			for (j = 0; array[i][j] != -1; j++) {

//				System.out.println(array[i][j]);
			}
		}
		int numOfthis = 0;
		for (int ii = 0; array[maxcategory][ii] != -1; ii++) {
			numOfthis++;
		}
		//System.out.println(numOfthis + "涓�);
//		System.out.println("\n鏈�ぇ杩為�鍒嗘敮鏄細绗� + (maxcategory + 1) + "寮鸿繛閫氬垎鏀�);
//		System.out.println("over!");// 缁撴潫

		return numOfthis;
	}

	// 涓诲嚱鏁�
	public static void main(String[] args) {
		int[][] m = { { 0, 1, 1, 0, 1 }, { 1, 0, 1, 1, 0 }, { 1, 1, 0, 0, 0 },
				{ 0, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0 } };
		MaxSubGraph msg = new MaxSubGraph();
		msg.maxS(m);
	}
}
