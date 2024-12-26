import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Boss {
	public static Line line = new Line("");
	static {
		Line num = new Line("5");
		line.nextLine = num;
		Line line1 = new Line("1 0 100");
		num.nextLine = line1;
		Line line2 = new Line("2 0 199");
		line1.nextLine = line2;
		Line line3 = new Line("3 0 200");
		line2.nextLine = line3;
		Line line4 = new Line("4 0 200");
		line3.nextLine = line4;
		Line line5 = new Line("5 0 200");
		line4.nextLine = line5;
	}
	public static void main(String[] args) {
		line = line.nextLine;
		Integer num = Integer.valueOf(line.lineString);
		HashMap<Integer, Node> emps = new HashMap<Integer, Node>();
		Node cur = null;
		for (int i = 0; i < num; i++) {
			line = line.nextLine;
			String[] p = line.lineString.split(" ");
			Node node = new Node();
			node.id = Integer.valueOf(p[0]);
			emps.put(node.id, node);
			Integer pid = Integer.valueOf(p[1]);
			node.parent = emps.get(pid);
			if (node.parent == null) {
				Node parent = new Node();
				node.parent = parent;
				cur = parent;
				parent.id = pid;
				emps.put(pid, parent);
			}
			node.parent.subNodes.add(node);
			node.income = Integer.valueOf(p[2]);
		}
		while (cur.parent != null) {
			cur = cur.parent;
		}

		System.out.println(cur.id);

		int income = getBossIncome(cur) ;
		System.out.println(income);
	}

	public static  int getBossIncome(Node cur) {
		int sum = 0;
		for (Node subNode : cur.subNodes) {
			sum += getBossIncome(subNode);
		}
		if (cur.parent == null) {
			return sum + cur.income;
		}
		return (sum + cur.income)/100*15;
	}
}
class Line{
	public String lineString;
	public Line nextLine;
	public Line(String lineString) {
		this.lineString = lineString;
	}
	public Line nextLine() {
		return nextLine;
	}
}
class Node{
	public int id;
	public int income;
	public Node parent;
	public List<Node> subNodes = new ArrayList<>();
}