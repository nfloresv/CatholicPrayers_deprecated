package cl.flores.catholicprayers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cl.flores.catholicprayers.clases.ExpandableListChild;

public class ViewPrayers extends Activity {
	private ExpandableListChild child;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_prayers);
		Bundle bundle = getIntent().getExtras();
		child = (ExpandableListChild) bundle.getSerializable("child");
		getPrayerName();
		getPrayer();
		TextView prayer = (TextView) this.findViewById(R.id.prayer);
		Util util = Util.getInstance();
		prayer.setTextSize(util.textSize);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		menu.setGroupVisible(R.id.menu_main, false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_back) {
			finish();
			return true;
		} else if (item.getItemId() == R.id.menu_info) {
			Intent about = new Intent(ViewPrayers.this, About.class);
			startActivity(about);
			return true;
		} else if (item.getItemId() == R.id.menu_preference) {
			Intent preference = new Intent(ViewPrayers.this, Preferences.class);
			startActivity(preference);
			return true;
		} else if (item.getItemId() == R.id.submenu_web) {
			String url = "http://nfloresv.github.com/CatholicPrayers/";
			Intent web = new Intent(Intent.ACTION_VIEW);
			web.setData(Uri.parse(url));
			startActivity(web);
			return true;
		} else if (item.getItemId() == R.id.submenu_faq) {
			String url = "http://nfloresv.github.com/CatholicPrayers/faq.html";
			Intent faq = new Intent(Intent.ACTION_VIEW);
			faq.setData(Uri.parse(url));
			startActivity(faq);
			return true;
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
			Util util = Util.getInstance();
			int dialogId = util
					.update(getApplicationContext(),
							"http://nfloresv.github.com/CatholicPrayers/changelog.html");
			showDialog(dialogId);
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Intent intent = new Intent(ViewPrayers.this, ViewPrayers.class);
		intent.putExtra("child", child);
		finish();
		startActivity(intent);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Util util = Util.getInstance();
		Dialog dialog = null;
		if (id == util.getUpdateDialog()) {
			dialog = util.updateDialog(this);
		} else if (id == util.getUpToDateDialog()) {
			dialog = util.upToDateDialog(this);
		}
		return dialog;
	}

	/**
	 * Get the name of the prayer to use its as a title
	 */
	private void getPrayerName() {
		TextView prayer_title = (TextView) this.findViewById(R.id.prayer_title);
		prayer_title.setText(child.getName());
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Util util = Util.getInstance();
		prayer_title.setTextSize(Integer.parseInt(pref.getString("title_size",
				util.getTitleSize())));
	}

	/**
	 * Search for the prayer, and post it on the text view
	 */
	private void getPrayer() {
		TextView prayer = (TextView) this.findViewById(R.id.prayer);
		String pray = "";
		Scanner scan;
		if (child.getId() != 0) {
			InputStream internal_prayer = getResources().openRawResource(
					child.getId());
			scan = new Scanner(internal_prayer);
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			Util util = Util.getInstance();
			int length = Integer.parseInt(pref.getString("max_length",
					util.getTitleSize()));
			while (scan.hasNextLine() && length > 0) {
				pray += scan.nextLine() + "\n";
				if (pref.getBoolean("prayer_length", util.isUsePrayerSize())) {
					--length;
				}
			}
			scan.close();
			try {
				internal_prayer.close();
			} catch (IOException e) {
			}
			prayer.setText(pray);
			prayer.setTextSize(util.textSize);
		} else {
			String state = Environment.getExternalStorageState();
			if (state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)
					|| state.equalsIgnoreCase(Environment.MEDIA_MOUNTED_READ_ONLY)) {
				File user_prayer = child.getPath();
				try {
					scan = new Scanner(user_prayer);
					SharedPreferences pref = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					Util util = Util.getInstance();
					int length = Integer.parseInt(pref.getString("max_length",
							util.getTitleSize()));
					while (scan.hasNextLine() && length > 0) {
						pray += scan.nextLine() + "\n";
						if (pref.getBoolean("prayer_length",
								util.isUsePrayerSize())) {
							--length;
						}
					}
					prayer.setText(pray);
					prayer.setTextSize(util.textSize);
				} catch (FileNotFoundException e) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Error leyendo oracion.", Toast.LENGTH_SHORT);
					toast.show();
				}
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Error leyendo oracion.", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

	/**
	 * Generate the method onClick for the back button
	 */
	public void onBackClick(View v) {
		finish();
	}

	/**
	 * Increase the size of the text of the prayer
	 */
	public void onZoomInClick(View v) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Util util = Util.getInstance();
		if (pref.getBoolean("prayer_size", util.isUsePrayerSize())) {
			String size = pref.getString("max_size", util.getTitleSize());
			if (Integer.parseInt(size.replace("sp", "")) >= (util.textSize + 2)) {
				util.textSize += 2;
				TextView prayer = (TextView) this.findViewById(R.id.prayer);
				prayer.setTextSize(util.textSize);
			}
		} else {
			util.textSize += 2;
			TextView prayer = (TextView) this.findViewById(R.id.prayer);
			prayer.setTextSize(util.textSize);
		}
	}

	/**
	 * Decrease the size of the text of the prayer
	 */
	public void onZoomOutClick(View v) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Util util = Util.getInstance();
		if (pref.getBoolean("prayer_size", util.isUsePrayerSize())) {
			String size = pref.getString("min_size", util.getTitleSize());
			if (Integer.parseInt(size.replace("sp", "")) <= (util.textSize - 2)) {
				util.textSize -= 2;
				TextView prayer = (TextView) this.findViewById(R.id.prayer);
				prayer.setTextSize(util.textSize);
			}
		} else {
			util.textSize -= 2;
			TextView prayer = (TextView) this.findViewById(R.id.prayer);
			prayer.setTextSize(util.textSize);
		}
	}

}
