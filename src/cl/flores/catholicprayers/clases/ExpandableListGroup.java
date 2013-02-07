package cl.flores.catholicprayers.clases;

import java.util.ArrayList;

public class ExpandableListGroup implements Comparable<ExpandableListGroup> {
	private String name;
	private ArrayList<ExpandableListChild> items;

	/**
	 * Creat a new Group for the expandable list
	 * 
	 * @param name
	 *            The name o f the gorup
	 * @param items
	 *            The items that has the group
	 * @param color
	 *            The background clor of the group
	 */
	public ExpandableListGroup(String name, ArrayList<ExpandableListChild> items) {
		this.name = name;
		this.items = items;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the items
	 */
	public ArrayList<ExpandableListChild> getItems() {
		return items;
	}

	@Override
	public int compareTo(ExpandableListGroup another) {
		String otherName = another.getName();
		return name.compareToIgnoreCase(otherName);
	}

}
