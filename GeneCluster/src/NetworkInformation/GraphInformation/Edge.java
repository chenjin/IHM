package NetworkInformation.GraphInformation;

public class Edge {
	public String nodeTO;
	public String nodeFrom;
	public String data;

	public String getNodeTO() {
		return nodeTO;
	}

	public void setNodeTO(String nodeTO) {
		this.nodeTO = nodeTO;
	}

	public String getNodeFrom() {
		return nodeFrom;
	}

	public void setNodeFrom(String nodeFrom) {
		this.nodeFrom = nodeFrom;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String toString() {
		return "\"nodeTo\":" + addQuote(this.getNodeTO()) + ","
				+ "\"nodeFrom\":" + addQuote(this.getNodeFrom()) + ","
				+ "\"data\":" + this.getData();
	}

	public String addQuote(String in) {
		return "\"" + in + "\"";
	}
}
