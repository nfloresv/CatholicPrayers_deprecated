package cl.flores.catholicprayers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UpdateThread extends Thread {
	private Handler handler;

	public UpdateThread(Handler h) {
		handler = h;
	}

	@Override
	public void run() {
		Message m = handler.obtainMessage();
		try {
			URL uri = new URL(
					"http://nfloresv.github.com/CatholicPrayers/changelog.html");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uri.openStream()));
			String inLine = "";
			while ((inLine = in.readLine()) != null) {
				if (inLine.indexOf("<td>") != -1) {
					inLine = inLine.replace("<td>", "").replace("</td>", "")
							.trim();
					break;
				}
			}
			in.close();
			if (inLine.length() > 0) {
				int pos = inLine.indexOf('"');
				String link = "http://nfloresv.github.com/CatholicPrayers/"
						+ inLine.substring(pos + 1,
								inLine.indexOf('"', pos + 1));
				String version = inLine.substring(inLine.lastIndexOf(' ') + 1,
						inLine.lastIndexOf('<'));
				Bundle bundle = new Bundle();
				bundle.putBoolean("result", true);
				bundle.putString("message", version);
				bundle.putString("link", link);
				m.setData(bundle);
			}
		} catch (Exception e) {
			Bundle bundle = new Bundle();
			bundle.putBoolean("result", false);
			bundle.putString("message", "Error buscando actualizaciones.");
			m.setData(bundle);
		}
		handler.sendMessage(m);
	}
}
