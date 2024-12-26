package com.redjujubetree.example.huawei;

public class IntersectArea {

	static Input input;
	static {
		input = new Input("1 6 4 4\n" +
				"3 5 3 4\n" +
				"0 3 7 3");
	}

	public static void main(String[] args) {
		Rectangle r1 = new Rectangle(input.nextLine().split(" "));
		Rectangle r2 = new Rectangle(input.nextLine().split(" "));
		Rectangle r3 = new Rectangle(input.nextLine().split(" "));

		int x1 = Math.max(r1.x1, r2.x1);
		int y1 = Math.min(r1.y1, r2.y1);
		int x2 = Math.min(r1.x2, r2.x2);
		int y2 = Math.max(r1.y2, r2.y2);
		if (x1 < x2 && y1 > y2) {
			int intersectX1 = Math.max(x1, r3.x1);
			int intersectY1 = Math.min(y1, r3.y1);
			int intersectX2 = Math.min(x2, r3.x2);
			int intersectY2 = Math.max(y2, r3.y2);
			if (intersectX1 < intersectX2 && intersectY1 > intersectY2) {
				System.out.println((intersectX2 - intersectX1) * (intersectY1 - intersectY2));
			} else {
				System.out.println("0");
			}
		} else {
			System.out.println(0);
		}
	}

	static class Rectangle{
		public int x1;
		public int y1;
		public int x2;
		public int y2;
		public Rectangle(String[] lines) {
			this.x1 = Integer.parseInt(lines[0]);
			this.y1 = Integer.parseInt(lines[1]);
			this.x2 = x1 + Integer.parseInt(lines[2]);
			this.y2 = y1 - Integer.parseInt(lines[3]);
		}

	}
}
