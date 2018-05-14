
public class NamedPlace extends Place{
	
	public NamedPlace(String name, Position pos, Category category) {
		super(name, pos, category);
	}
	
	public String toString() {
		return getName() + " " + getPos().toString();
	}

}
