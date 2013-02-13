package cl.flores.catholicprayers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ExpandableListView;
import cl.flores.catholicprayers.adapter.ExpandableListAdapter;
import cl.flores.catholicprayers.clases.ExpandableListChild;

public class ExportThread extends Thread {
	private ExpandableListAdapter expandList;
	private Context context;
	private File directory;
	private Handler handler;

	public ExportThread(Handler handler, ExpandableListView expandList,
			Context context, File dir) {
		this.handler = handler;
		this.expandList = (ExpandableListAdapter) expandList
				.getExpandableListAdapter();
		this.context = context;
		directory = dir;
	}

	@Override
	public void run() {
		Message m = handler.obtainMessage();
		String message = "";
		File dir = new File(directory.getAbsoluteFile(), "Export");
		if (dir.mkdir() || dir.isDirectory()) {
			message = " Oraciones exportadas con exito";
			directory = dir;
			for (int i = 0; i < expandList.getGroupCount(); ++i) {
				for (int j = 0; j < expandList.getChildrenCount(i); ++j) {
					ExpandableListChild child = (ExpandableListChild) expandList
							.getChild(i, j);
					String title = child.getName();
					String prayer = getPrayer(child);
					if (prayer.length() > 0) {
						File pray = new File(directory.getAbsolutePath(), title
								+ ".txt");
						try {
							prayer = removeTags(prayer);
							FileWriter fw = new FileWriter(pray);
							fw.write(prayer);
							fw.close();
						} catch (IOException e) {
							message = "No se pudo exportar algunas oraciones";
						}
					}
				}
			}
		} else {
			message = "Error exportando oraciones";
		}
		Bundle bundle = new Bundle();
		bundle.putString("message", message);
		m.setData(bundle);
		handler.sendMessage(m);
	}

	/**
	 * Search for the prayer, and return its texts.
	 */
	private String getPrayer(ExpandableListChild child) {
		String pray = "";
		Scanner scan;
		if (child.getId() != 0) {
			InputStream internal_prayer = context.getResources()
					.openRawResource(child.getId());
			scan = new Scanner(internal_prayer);
			while (scan.hasNextLine()) {
				pray += scan.nextLine() + "\n";
			}
			scan.close();
			try {
				internal_prayer.close();
			} catch (IOException e) {
			}
		} else {
			String state = Environment.getExternalStorageState();
			if (state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)
					|| state.equalsIgnoreCase(Environment.MEDIA_MOUNTED_READ_ONLY)) {
				File user_prayer = child.getPath();
				try {
					scan = new Scanner(user_prayer);
					while (scan.hasNextLine()) {
						pray += scan.nextLine() + "\n";
					}
				} catch (FileNotFoundException e) {
				}
			}
		}
		return pray;
	}
	
	/**
	 * Remove the HTML tags of the prayer
	 * 
	 * @param prayer
	 *            the prayer to remove the tags
	 * @return the prayer without html tags
	 */
	private String removeTags(String prayer) {
		// <p> </p> tags
		prayer = prayer.replace("<p>", "");
		prayer = prayer.replace("</p>", "");
		// <i> </i> tags
		prayer = prayer.replace("<i>", "");
		prayer = prayer.replace("</i>", "");
		// <b> </b> tags
		prayer = prayer.replace("<b>", "");
		prayer = prayer.replace("</b>", "");
		// <br> tag
		prayer = prayer.replace("<br>", "");
		return prayer;
	}
}
