package cl.flores.catholicprayers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPrayer extends Activity {
	private boolean sdWritable;
	private boolean sdReadable;
	private File directory;
	private Util util;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_prayer);
		sdStatus();
		directory = find_directory();
		if (!sdReadable) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Error accediendo a la tarjeta SD", Toast.LENGTH_SHORT);
			toast.show();
		}
		util = Util.getInstance();
		Button save = (Button) this.findViewById(R.id.btn_save);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				savePrayer();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		menu.setGroupVisible(R.id.menu_main, false);
		menu.findItem(R.id.menu_addPrayer).setVisible(false);
		menu.findItem(R.id.submenu_export).setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_back) {
			finish();
			return true;
		} else if (item.getItemId() == R.id.menu_save) {
			savePrayer();
			return true;
		} else if (item.getItemId() == R.id.menu_info) {
			Intent about = new Intent(AddPrayer.this, About.class);
			try {
				startActivity(about);
				return true;
			} catch (ActivityNotFoundException e) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Error Inesperado.", Toast.LENGTH_SHORT);
				toast.show();
				return false;
			}
		} else if (item.getItemId() == R.id.menu_preference) {
			Intent preference = new Intent(AddPrayer.this, Preferences.class);
			try {
				startActivity(preference);
				return true;
			} catch (ActivityNotFoundException e) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Error Inesperado.", Toast.LENGTH_SHORT);
				toast.show();
				return false;
			}
		} else if (item.getItemId() == R.id.submenu_web) {
			String url = "http://nfloresv.github.com/CatholicPrayers/";
			Intent web = new Intent(Intent.ACTION_VIEW);
			web.setData(Uri.parse(url));
			try {
				startActivity(web);
				return true;
			} catch (ActivityNotFoundException e) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Error Inesperado.", Toast.LENGTH_SHORT);
				toast.show();
				return false;
			}
		} else if (item.getItemId() == R.id.submenu_faq) {
			String url = "http://nfloresv.github.com/CatholicPrayers/faq.html";
			Intent faq = new Intent(Intent.ACTION_VIEW);
			faq.setData(Uri.parse(url));
			try {
				startActivity(faq);
				return true;
			} catch (ActivityNotFoundException e) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Error Inesperado.", Toast.LENGTH_SHORT);
				toast.show();
				return false;
			}
		} else if (item.getItemId() == R.id.submenu_mail) {
			Intent mail = new Intent(Intent.ACTION_SEND);
			mail.setType("message/rfc822");
			mail.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "nicolas.floresv@gmail.com" });
			mail.putExtra(Intent.EXTRA_SUBJECT, "Catholic Prayers");
			try {
				startActivity(Intent.createChooser(mail, "Send mail..."));
				return true;
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(getApplicationContext(),
						"No hay aplicac'ion de email instalada.",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		} else if (item.getItemId() == R.id.submenu_update) {
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					Bundle bundle = msg.getData();
					boolean result = bundle.getBoolean("result");
					String message = bundle.getString("message");
					String link = bundle.getString("link");
					Util util = Util.getInstance();
					int dialogId = util.update(getApplicationContext(), result,
							message, link);
					showDialog(dialogId);
				}
			};
			UpdateThread update = new UpdateThread(handler);
			update.start();
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		if (id == util.getUpdateDialog()) {
			dialog = util.updateDialog(this);
		} else if (id == util.getUpToDateDialog()) {
			dialog = util.upToDateDialog(this);
		}
		return dialog;
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
	 * Search for "Catholic Prayers" directory in the SD card and return it.
	 * 
	 * @return The directory "Catholic Prayers"
	 */
	private File find_directory() {
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
		} catch (Exception e) {
			directory = null;
		}
		return directory;
	}

	/**
	 * Writes to the SD directory "Catholic Prayer" the prayer that the user
	 * define. The name of the file is the title of the prayer.
	 */
	private void savePrayer() {
		sdStatus();
		String message = "";
		if (sdWritable && directory != null) {
			EditText title = (EditText) this.findViewById(R.id.prayer_title);
			if (title.getText().length() != 0) {
				File prayer = new File(directory.getAbsolutePath(), title
						.getText().toString() + ".txt");
				EditText body = (EditText) this.findViewById(R.id.prayer_body);
				if (body.getText().length() != 0) {
					try {
						FileWriter fw = new FileWriter(prayer);
						fw.write(body.getText().toString());
						fw.close();
						message = "Oración guardada";
						finish();
					} catch (IOException e) {
						message = "Error inesperado guardando oración";
						finish();
					}
				} else {
					message = "Debe escribir un cuerpo a la oración";
				}
			} else {
				message = "Debe escribir un titulo a la oración";
			}
		} else {
			message = "Error guardando oración";
			finish();
		}
		Toast toast = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_SHORT);
		toast.show();
		util.setGroupsExpanded(null);
	}
}
