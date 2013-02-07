package cl.flores.catholicprayers;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class LoadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Intent intent = new Intent(LoadActivity.this,
						MainActivity.class);
				try {
					startActivity(intent);
					finish();
				} catch (ActivityNotFoundException e) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Error cargando aplicación.", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		};
		LoadThread load = new LoadThread(handler, getApplicationContext());
		load.start();
	}

}
