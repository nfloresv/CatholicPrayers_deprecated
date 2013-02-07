package cl.flores.catholicprayers;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class LoadThread extends Thread {
	private Handler handler;
	private boolean sdWritable;
	private boolean sdReadable;
	private Context context;

	public LoadThread(Handler h, Context context) {
		handler = h;
		this.context = context;
		sdWritable = false;
		sdReadable = false;
	}

	@Override
	public void run() {
		Message m = handler.obtainMessage();
		for (int i = 0; i < m.hashCode(); i+=1000);
		find_createDirectory();
		handler.sendMessage(m);
	}

	/**
	 * Read the status of the SD card and sets the variables sdWritable and
	 * sdReadable
	 */
	private void sdStatus() {
		String status = Environment.getExternalStorageState();
		if (status.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			sdReadable = true;
			sdWritable = true;
		} else if (status.equalsIgnoreCase(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			sdReadable = true;
			sdWritable = false;
		} else {
			sdReadable = false;
			sdWritable = false;
		}
	}

	/**
	 * Search for "Catholic Prayers" directory in the SD and create it if it
	 * doesn't exist.
	 * 
	 * @return The directory "Catholic Prayers"
	 */
	private void find_createDirectory() {
		sdStatus();
		File directory = null;
		String dirName = "Catholic Prayers";
		try {
			if (sdReadable) {
				File sdPath = Environment.getExternalStorageDirectory();
				for (File dir : sdPath.listFiles()) {
					if (dir.isDirectory()) {
						if (dirName.equals(dir.getName())) {
							directory = dir;
							break;
						}
					}
				}
			}
			if (sdWritable && directory == null) {
				File sdPath = Environment.getExternalStorageDirectory();
				directory = new File(sdPath.getAbsolutePath(), dirName);
				if (directory.mkdirs()) {
					Toast toast = Toast.makeText(context,
							String.format("Carpeta %s creada.", dirName),
							Toast.LENGTH_SHORT);
					toast.show();
				} else {
					Toast toast = Toast
							.makeText(context, String.format(
									"Error creando carpeta %s.", dirName),
									Toast.LENGTH_SHORT);
					toast.show();
					directory = null;
				}
			}
		} catch (Exception e) {
			directory = null;
		}
	}

}
