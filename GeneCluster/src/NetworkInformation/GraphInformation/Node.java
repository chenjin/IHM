package NetworkInformation.GraphInformation;

public class Node {
	public String $color;
	public String $type;
	public int $dim;

	public Node() {
		this.$color = "#83548B";
		this.$type = "circle";
		this.$dim = 10;
	}

	public String get$color() {
		return $color;
	}

	public void set$color(String $color) {
		this.$color = $color;
	}

	public String get$type() {
		return $type;
	}

	public void set$type(String $type) {
		this.$type = $type;
	}

	public int get$dim() {
		return $dim;
	}

	public void set$dim(int $dim) {
		this.$dim = $dim;
	}

	public String toString() {
		return "\"$color\":" + addQuote(this.get$color()) + "," + "\"$type\":"
				+ addQuote(this.get$type()) + "," + "\"$dim\":"
				+ addQuote(Integer.toString(this.get$dim()));
	}

	public String addQuote(String in) {
		return "\"" + in + "\"";
	}
}