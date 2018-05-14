
public class DescribedPlace extends Place{
	
	private String description;
	
	public DescribedPlace(String name, Position pos, Category category, String description) {
		super(name, pos, category);
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

}
