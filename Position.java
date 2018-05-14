
public class Position {
	
	private int x, y;
	
	public Position (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getY() {
		return y-20;
	}
	
	public int getX() {
		return x-10;
	}
	
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}
}
