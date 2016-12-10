package NetworkInformation.DataTransformation;


import java.util.ArrayList;
import java.util.List;

import NetworkInformation.GraphInformation.*;


//make a color list with a fixed size
public class ToJson {

	public Edge makeEdge(int start, int end, int[] degree) {
		Edge temp = new Edge();
		temp.setNodeFrom(Integer.toString(start + 1));
		temp.setNodeTO(Integer.toString(end + 1));
		String ccc = checkColor(end, degree);
		// String color = "{ \"$color\":" + Colors.color[degree[start]] + "}";
		String color = "{ \"$color\":" + "\"" + ccc + "\"" + "}";
		temp.setData(color);
		return temp;
	}

	public String checkColor(int node, int[] degree) {
		return Colors.color[(degree[node] / 10)];
	}

	// int[] row is the start th row of matrix
	public List<Edge> makeEdgeList(int start, int[] row, int[] degree) {
		List<Edge> edgeList = new ArrayList<Edge>();
		for (int i = 0; i < row.length; i++) {
			if (row[i] == 1) {
				edgeList.add(makeEdge(start, i, degree));
			}
		}
		return edgeList;
	}

	//
	public Rows makeRow(int start, int[] row, int[] degree) {
		Rows rows = new Rows();
		List<Edge> edgeList = makeEdgeList(start, row, degree);
		String adj = edgeListToString(edgeList);
		rows.setAdjacencies(adj);
		String data = setNode(start, degree);
		rows.setData(data);
		rows.setId(Integer.toString(start + 1));
		rows.setName(Integer.toString(start + 1));
		return rows;
	}

	public String makeJson(int[][] matrix, int[] degree) {
		List<String> rows = new ArrayList<String>();
		for (int i = 0; i < matrix.length; i++) {
			Rows temp = makeRow(i, matrix[i], degree);
			rows.add(rowToString(temp));
		}
		String result = rowListTo(rows);
		return addSE2(result);
	}

	// {adj: id: name:}
	public String rowToString(Rows row) {
		String rowTemp = "\"adjacencies\": [" + row.getAdjacencies();
		rowTemp += "],";
		rowTemp += "\"data\":" + row.getData() + ",";
		rowTemp += "\"id\":" + addQuote(row.getId()) + ",";
		rowTemp += "\"name\":" + addQuote(row.getName());

		return rowTemp;
	}

	public String rowListTo(List<String> edges) {
		String result = "";
		if (edges.size() == 1)
			return addSE(edges.get(0).toString());
		else if (edges.size() > 1) {
			for (int i = 0; i < edges.size() - 1; i++) {
				result += addSE(edges.get(i).toString()) + ",";
			}
			result += addSE(edges.get(edges.size() - 1).toString());
			return result;
		} else
			return null;
	}

	// {},{},{}
	public String edgeListToString(List<Edge> edges) {
		String result = "";
		if (edges.size() == 1)
			return addSE(edges.get(0).toString());
		else if (edges.size() > 1) {
			for (int i = 0; i < edges.size() - 1; i++) {
				result += addSE(edges.get(i).toString()) + ",";
			}
			result += addSE(edges.get(edges.size() - 1).toString());
			return result;
		} else
			return null;
	}

	// {"$color" : --}
	public String setNode(int start, int[] degree) {
		Node node = new Node();
		// node.set$color(Colors.color[degree[start]]);
		String ccc = checkColor(start, degree);
		node.set$color(ccc);
		node.set$dim(5);
		// 鏄惁瑕佸墠10澶у害鏀瑰彉褰㈢姸 鎴栬�绗竴澶т负鏄熷瀷
		if (start == maxLoc(degree))
			node.set$type("star");
		else
			node.set$type("circle");
		return addSE(node.toString());
	}

	public int maxLoc(int[] degree) {
		int maxLoc = 0;
		for (int i = 1; i < degree.length; i++) {
			if (degree[i] > degree[maxLoc])
				maxLoc = i;
		}
		return maxLoc;
	}

	//
	public String addQuote(String in) {
		return "\"" + in + "\"";
	}

	// add { }
	public String addSE(String in) {
		return "{" + in + "}";
	}

	// add []
	public String addSE2(String in) {
		return "[" + in + "]";
	}

	public static void main(String[] args) throws Exception {
		ToJson tj = new ToJson();
		DataMatrix dm = new DataMatrix();
		int[][][] matrix = dm.XlsToMatrix("F://data.xls");
		int[][] m = { { 0, 1, 1, 0, 1 }, { 1, 0, 1, 1, 0 }, { 1, 1, 0, 0, 0 },
				{ 0, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0 } };

		Compute c = new Compute();
		int[] degree = c.ComputeNodeDegree(matrix[0]);
		String json = tj.makeJson(m, degree);
		System.out.println(json);
	}
}
