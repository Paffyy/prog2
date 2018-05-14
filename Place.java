import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

    public class Place extends JComponent{

	protected String name;
	protected Position pos;
	protected Category category;
	protected Boolean marked = false;
	public enum Category {
		BUS, TRAIN, UNDERGROUND, NONE
	}
	
	public Place(String name, Position pos, Category category) {
		this.name = name;
		this.pos = pos;
		this.category = category;
		
		addMouseListener(new MarkedList());
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setPreferredSize(new Dimension(20, 20));
		setMaximumSize(new Dimension(20, 20));
		setMinimumSize(new Dimension(20, 20));
		setBounds(pos.getX(),pos.getY(), 20, 20);
		setVisible(true);
	}
	
	public String getName() {
		return name;
	}
	
	public Position getPos() {
		return pos;
	}
	
	public Category getCategory() {
		return category;
	}
	
	protected Color setColorByCategory() {
		if(category == Category.BUS) {
			return Color.RED;
		} else if (category == Category.TRAIN) {
			return Color.GREEN;
		} else if (category == Category.UNDERGROUND) {
			return Color.BLUE;
		} else return Color.BLACK;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int[] xPoints = {0, 10, 20};
		int[] yPoints = {0, 20, 0};
		int nPoints = 3;
		g.setColor(setColorByCategory());
		g.fillPolygon(xPoints, yPoints, nPoints);
		System.out.println(pos.toString());
		if(marked) {
			g.setColor(Color.ORANGE);
			g.drawRect(pos.getX(),pos.getY(), getWidth(), getHeight());
			System.out.println(pos.getX() +""+ pos.getY());
		}
	}
	
	class MarkedList extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {
			if(mev.getButton() == MouseEvent.BUTTON1) {
				marked = true;
				repaint();
			}
		}
	}
}
