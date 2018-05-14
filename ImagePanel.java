import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel{
	
	private ImageIcon image;

	public ImagePanel(String fileName) {
		setLayout(null);
		image = new ImageIcon(fileName);
		int w = image.getIconWidth();
		int h = image.getIconHeight();
		setPreferredSize(new Dimension(w, h));
		setMaximumSize(new Dimension(w, h));
		setMinimumSize(new Dimension(w, h));
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image.getImage(), 0, 0, this);
	}
}
