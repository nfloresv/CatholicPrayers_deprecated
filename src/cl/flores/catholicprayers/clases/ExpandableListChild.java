package cl.flores.catholicprayers.clases;

import java.io.File;
import java.io.Serializable;

public class ExpandableListChild implements Serializable,
		Comparable<ExpandableListChild> {
	private static final long serialVersionUID = -2115226550300573299L;
	private String name;
	private int id;
	private File path;

	/**
	 * Creates a new Child for the expandable list, and sets the path null
	 * because it is a system file.
	 * 
	 * @param name
	 *            the name of the child
	 * @param id
	 *            the ID of the element in R. If it has no ID, then the value of
	 *            ID is 0.
	 * @param color
	 *            The background color of the child
	 * @param image
	 *            The id of the background image
	 */
	public ExpandableListChild(String name, int id) {
		this.name = name;
		this.id = id;
		path = null;
	}

	/**
	 * Creates a new Child for the expandable list, the name of the Child is the
	 * file name. The id is 0 because it isn't a file system.
	 * 
	 * @param path
	 *            The path of the prayer
	 * @param image
	 *            the id of the background image
	 */
	public ExpandableListChild(File path) {
		this.path = path;
		name = path.getName();
		id = 0;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		if (name.lastIndexOf('.') != -1) {
			return name.substring(0, name.lastIndexOf('.')).replace('_', ' ');
		}
		return name.replace('_', ' ');
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the path
	 */
	public File getPath() {
		return path;
	}

	@Override
	public int compareTo(ExpandableListChild another) {
		String otherName = another.getName();
		return name.compareToIgnoreCase(otherName);
	}

}
