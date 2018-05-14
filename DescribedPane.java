import javax.swing.*;
import java.awt.event.*;
import javax.swing.JOptionPane.*;

public class DescribedPane extends JPanel {

	private JTextField nameInput;
	private JTextField descriptionInput;

	public DescribedPane() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel firstRow = new JPanel();
		JLabel name = new JLabel("Name: ");
		nameInput = new JTextField(10);
		firstRow.add(name);
		firstRow.add(nameInput);
		add(firstRow);
		JPanel secondRow = new JPanel();
		JLabel description = new JLabel("Description: ");
		descriptionInput = new JTextField(20);
		secondRow.add(description);
		secondRow.add(descriptionInput);
		add(secondRow);
	}

	public String getName() {
		return nameInput.getText();
	}

	public String getDescription() {
		return descriptionInput.getText();
	}
}