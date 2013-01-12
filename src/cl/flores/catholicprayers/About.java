package cl.flores.catholicprayers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		InputStream internal_prayer = getResources().openRawResource(
				R.raw.about);
		String about = "";
		Scanner scan = new Scanner(internal_prayer);
		while (scan.hasNextLine()) {
			about += scan.nextLine() + "\n";
		}
		scan.close();
		try {
			internal_prayer.close();
		} catch (IOException e) {
		}
		Spanned spanned_about = Html.fromHtml(about);
		TextView info = (TextView) this.findViewById(R.id.info);
		info.setText(spanned_about);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Intent intent = new Intent(About.this, About.class);
		finish();
		startActivity(intent);
	}

	/**
	 * Generate the method onClick for the back button
	 */
	public void onBackClick(View v) {
		finish();
	}

}
