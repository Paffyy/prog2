import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.*;
import java.util.*;
import java.util.List;
import javax.swing.event.*;

public class MapProgram extends JFrame {

	private SortedList sortedList = new SortedList();
	private JList categoryList = new JList<String>(sortedList);
	private JFileChooser jfc = new JFileChooser(".");
	private ImagePanel imgPanel = null;
	private JScrollPane scroll = null;
	private JRadioButton named;
	private JRadioButton described;
	private JButton newButton;
	private JTextField searchText;
	private MouseList mouseList = new MouseList();
	private Map<String, Map<Position, Place>> placeByName = new HashMap<>();
	private Map<Place.Category, Map<Position, Place>> placeByCategory = new HashMap<>();
	private Map<Position, Place> markedPlace = new HashMap<>();

	public MapProgram() {
		super("Inlupp 2");

		JMenuBar mbar = new JMenuBar();
		setJMenuBar(mbar);
		JMenu archive = new JMenu("Archive");
		mbar.add(archive);
		JMenuItem newMap = new JMenuItem("New Map");
		archive.add(newMap);
		newMap.addActionListener(new NewMapList());
		JMenuItem loadPlaces = new JMenuItem("Load Places");
		archive.add(loadPlaces);
		JMenuItem save = new JMenuItem("Save");
		archive.add(save);
		JMenuItem exit = new JMenuItem("Exit");
		archive.add(exit);

		setLayout(new BorderLayout());
		JPanel northPanel = new JPanel();
		add(northPanel, BorderLayout.NORTH);
		northPanel.setLayout(new FlowLayout());
		newButton = new JButton("New");
		newButton.addActionListener(new NewList());
		northPanel.add(newButton);
		JPanel radioButtons = new JPanel();
		radioButtons.setLayout(new BoxLayout(radioButtons, BoxLayout.Y_AXIS));
		northPanel.add(radioButtons);
		named = new JRadioButton("Named");
		named.setSelected(true);
		radioButtons.add(named);
		described = new JRadioButton("Described");
		radioButtons.add(described);
		ButtonGroup group = new ButtonGroup();
		group.add(named);
		group.add(described);
		searchText = new JTextField("Search", 10);
		northPanel.add(searchText);
		JButton search = new JButton("Search");
		northPanel.add(search);
		search.addActionListener(new SearchList());
		JButton hide = new JButton("Hide");
		northPanel.add(hide);
		JButton remove = new JButton("Remove");
		northPanel.add(remove);
		JButton coordinates = new JButton("Coordinates");
		northPanel.add(coordinates);

		JPanel eastPanel = new JPanel();
		add(eastPanel, BorderLayout.EAST);
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		JLabel categories = new JLabel("Categories");
		eastPanel.add(categories);
		eastPanel.add(new JScrollPane(categoryList));
		categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// check if correctly implemented
		JButton hideCategory = new JButton("Hide category");
		eastPanel.add(hideCategory);
		hideCategory.addActionListener(new HideCategoryList());

		FileFilter ff = new FileNameExtensionFilter("Bilder", "jpg", "gif", "png");
		jfc.setFileFilter(ff);

		addCategories();
		categoryList.addListSelectionListener(new JListList());

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(1000, 400);
		Point offsetCenter = new Point();
		offsetCenter.x = getLocation().x - (getWidth() / 2);
		offsetCenter.y = getLocation().y - (getHeight() / 2);
		setLocation(offsetCenter);
		setVisible(true);
	}
	
	public void markedPlace(Place p) {
		markedPlace.put(p.getPos(), p);
	}

	private Place.Category convertCategory(String category) {
		if (category.equals("Bus")) {
			return Place.Category.BUS;
		} else if (category.equals("Train")) {
			return Place.Category.TRAIN;
		} else if (category.equals("Underground")) {
			return Place.Category.UNDERGROUND;
		} else
			return Place.Category.NONE;
	}

	private void addCategories() {
		String[] categories = { "Train", "Bus", "Underground" };
		for (String c : categories) {
			sortedList.addSorted(c);
			Map<Position, Place> placeCategory = new HashMap<Position, Place>();
			placeByCategory.put(convertCategory(c), placeCategory);
		}
	}

	class SortedList extends DefaultListModel<String> {
		public void addSorted(String newStr) {
			int pos = 0;
			while (pos < size() && newStr.compareTo(get(pos)) > 0) {
				pos++;
			}
			add(pos, newStr);
		}
	}

	class NewMapList implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			int answer = jfc.showOpenDialog(MapProgram.this);
			if (answer != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File file = jfc.getSelectedFile();
			String fileName = file.getAbsolutePath();
			if (scroll != null) {
				remove(scroll);
			}
			imgPanel = new ImagePanel(fileName);
			scroll = new JScrollPane(imgPanel);
			add(scroll, BorderLayout.CENTER);
			// pack();
			validate();
			repaint();
		}
	}

	class HideCategoryList implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			if (categoryList.isSelectionEmpty()) {
				return;
			} else {
				Place.Category chosenCategory = convertCategory((String) categoryList.getSelectedValue());
				List<Place> placeCategory = placeByCategory.get(chosenCategory);
				for (Place p : placeCategory) {
					p.setVisible(false);
				}
			}
		}
	}

	class NewList implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			if (imgPanel != null) {
				imgPanel.addMouseListener(mouseList);
				newButton.setEnabled(false);
				imgPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
		}
	}

	class SearchList implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			// avmarkera eventuella platser som är markerade
			String name = searchText.getText();
			List<Place> placeName = placeByName.get(name);
			if (placeName == null) {
				return;
			} else {
				placeName.forEach(p -> p.setVisible(true));
				// markera dessa platser
			}
		}
	}

	class RemoveList implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			// if marked
		}
	}

	class JListList implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent lev) {
			if (!lev.getValueIsAdjusting()) {
				if (categoryList.getSelectedValue() != null) {
					List<Place> placeCategory = placeByCategory
							.get(convertCategory((String) categoryList.getSelectedValue()));
					placeCategory.forEach(p -> p.setVisible(true));
				}
			}
		}
	}
	
	// check for same positions error

	class MouseList extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {
			Position pos = new Position(mev.getX(), mev.getY());
			Place.Category chosenCategory;
			if (categoryList.isSelectionEmpty()) {
				chosenCategory = Place.Category.NONE;
			} else {
				chosenCategory = convertCategory((String) categoryList.getSelectedValue());
			}
			if (named.isSelected()) {
				String name = JOptionPane.showInputDialog("Name :");
				if (name == null) {
					resetPanel();
					return;
				} else if (name.trim().equals("")) {
					JOptionPane.showMessageDialog(null, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
					resetPanel();
					return;
				} else {
					NamedPlace np = new NamedPlace(name, pos, chosenCategory);
					imgPanel.add(np);
					List<Place> placeName = placeByName.get(name);
					if (placeName == null) {
						placeName = new ArrayList<Place>();
						placeByName.put(name, placeName);
					}
					placeName.add(np);
					if (chosenCategory != Place.Category.NONE) {
						List<Place> categoryPlace = placeByCategory.get(chosenCategory);
						categoryPlace.add(np);
					}
					// add to collections
				}
			} else if (described.isSelected()) {
				DescribedPane dPane = new DescribedPane();
				int answer = JOptionPane.showConfirmDialog(MapProgram.this, dPane, "Described place",
						JOptionPane.OK_CANCEL_OPTION);
				if (answer != JOptionPane.OK_OPTION) {
					resetPanel();
					return;
				} else if (dPane.getName().trim().equals("") || dPane.getDescription().trim().equals("")) {
					JOptionPane.showMessageDialog(null, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
					resetPanel();
					return;
				} else {
					String name = dPane.getName();
					String description = dPane.getDescription();
					DescribedPlace dp = new DescribedPlace(name, pos, chosenCategory, description);
					imgPanel.add(dp);
					List<Place> placeName = placeByName.get(name);
					if (placeName == null) {
						placeName = new ArrayList<Place>();
						placeByName.put(name, placeName);
					}
					placeName.add(dp);
					if (chosenCategory != Place.Category.NONE) {
						List<Place> categoryPlace = placeByCategory.get(chosenCategory);
						categoryPlace.add(dp);
					}
					// add to collections
				}
			}
			imgPanel.repaint();
			imgPanel.validate();
			resetPanel();
			categoryList.clearSelection();
		}
	}

	private void resetPanel() {
		imgPanel.removeMouseListener(mouseList);
		imgPanel.setCursor(Cursor.getDefaultCursor());
		newButton.setEnabled(true);
	}

	public static void main(String[] args) {
		new MapProgram();
	}
}