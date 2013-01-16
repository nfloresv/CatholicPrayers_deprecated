package cl.flores.catholicprayers.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import cl.flores.catholicprayers.Util;
import cl.flores.catholicprayers.R;
import cl.flores.catholicprayers.clases.ExpandableListChild;
import cl.flores.catholicprayers.clases.ExpandableListGroup;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<ExpandableListGroup> groups;

	/**
	 * Creates a new expandable list adapter
	 * 
	 * @param context
	 *            The context where the expandable list exist
	 * @param groups
	 *            The groups thas has the expandable list
	 */
	public ExpandableListAdapter(Context context,
			ArrayList<ExpandableListGroup> groups) {
		this.context = context;
		this.groups = groups;
	}

	/**
	 * Adds a new item to the expandable list
	 * 
	 * @param item
	 *            The new item to add
	 * @param group
	 *            The group where the new item is going to be added
	 */
	public void addItem(ExpandableListChild item, ExpandableListGroup group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<ExpandableListChild> child = groups.get(index).getItems();
		child.add(item);
		// groups.get(index).setItems(child);
	}

	/**
	 * Return the selected child
	 * 
	 * @param groupPosition
	 *            The group where the child is selected
	 * @param childPosition
	 *            The position of the child in the group
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ExpandableListChild> childs = groups.get(groupPosition)
				.getItems();
		return childs.get(childPosition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * Return the view representation of the child
	 * 
	 * @param groupPosition
	 *            The group where the child is
	 * @param childPosition
	 *            The position of the child in the group
	 * @param isLastChild
	 *            Says if the child is the las one
	 * @param view
	 *            The view that use the child
	 * @param parent
	 *            The view parent of the child
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		ExpandableListChild child = (ExpandableListChild) getChild(
				groupPosition, childPosition);
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.expandablelist_child, null);
		}
		Util util = Util.getInstance();
		view.setBackgroundColor(Color.parseColor(util.getNextColorChild(
				groupPosition, childPosition)));
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		TextView tv = (TextView) view.findViewById(R.id.tvChild);
		tv.setText(child.getName().toString());
		tv.setTextSize(Integer.parseInt(pref.getString("child_size",
				util.getChildSize())));
		return view;

	}

	/**
	 * Return the number of child present in the group
	 * 
	 * @param groupPosition
	 *            The group we want to know the number of child
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<ExpandableListChild> chList = groups.get(groupPosition)
				.getItems();
		return chList.size();
	}

	/**
	 * Return the selected group
	 * 
	 * @param groupPosition
	 *            The group we want
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	/**
	 * Return the number of groups that has the expandable lis
	 */
	@Override
	public int getGroupCount() {
		return groups.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * Return the view representation of the group
	 * 
	 * @param groupPosition
	 *            The group we want to represent
	 * @param isLastChild
	 * @param view
	 *            The view that use the group
	 * @param parent
	 *            The parent view of the gorup
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ExpandableListGroup group = (ExpandableListGroup) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.expandablelist_group, null);
		}
		Util util = Util.getInstance();
		view.setBackgroundColor(Color.parseColor(util
				.getNextColorGroup(groupPosition)));
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		TextView tv = (TextView) view.findViewById(R.id.tvGroup);
		tv.setText(group.getName());
		tv.setTextSize(Integer.parseInt(pref.getString("group_size",
				util.getGroupSize())));
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
