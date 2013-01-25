package cl.flores.catholicprayers;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import cl.flores.catholicprayers.adapter.ExpandableListAdapter;
import cl.flores.catholicprayers.clases.ExpandableListChild;
import cl.flores.catholicprayers.clases.ExpandableListGroup;

public class MainActivity extends Activity {
	private ExpandableListView expandList;
	private boolean sdWritable;
	private boolean sdReadable;
	private int groups;
	private boolean[] groupsExpanded;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		expandList = (ExpandableListView) findViewById(R.id.ExpList);
		ArrayList<ExpandableListGroup> expListItems = getPrayers();
		ExpandableListAdapter expAdapter = new ExpandableListAdapter(
				MainActivity.this, expListItems);
		expandList.setAdapter(expAdapter);
		sdWritable = false;
		sdReadable = false;
		onChildClik(expandList);
		groups = expListItems.size();
		Util util = Util.getInstance();
		if (util.getGroupsExpanded() != null) {
			groupsExpanded = util.getGroupsExpanded();
			for (int i = 0; i < groups; ++i) {
				if (groupsExpanded[i]) {
					expandList.expandGroup(i);
				} else {
					expandList.collapseGroup(i);
				}
			}
		} else {
			groupsExpanded = new boolean[groups];
		}
		onGroupClik();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		menu.setGroupVisible(R.id.menu_prayer, false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_exit) {
			finish();
			return true;
		} else if (item.getItemId() == R.id.menu_collaps) {
			for (int i = 0; i < groups; ++i) {
				if (expandList.isGroupExpanded(i)) {
					expandList.collapseGroup(i);
				}
			}
			return true;
		} else if (item.getItemId() == R.id.menu_expand) {
			for (int i = 0; i < groups; ++i) {
				if (!expandList.isGroupExpanded(i)) {
					expandList.expandGroup(i);
				}
			}
			return true;
		} else if (item.getItemId() == R.id.menu_info) {
			Intent about = new Intent(MainActivity.this, About.class);
			startActivity(about);
			return true;
		} else if (item.getItemId() == R.id.menu_preference) {
			Intent preference = new Intent(MainActivity.this, Preferences.class);
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
						"No hay aplicación de email instalada.",
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
		Util util = Util.getInstance();
		util.setGroupsExpanded(groupsExpanded);
		Intent intent = new Intent(MainActivity.this, MainActivity.class);
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
	 * Generate a Array list with all the groups of prayers
	 * 
	 * @return array list with the prayers
	 */
	private ArrayList<ExpandableListGroup> getPrayers() {
		ArrayList<ExpandableListGroup> prayers = new ArrayList<ExpandableListGroup>();
		prayers.add(getBasicPrayer());
		prayers.add(getMaryPrayer());
		prayers.add(getRosaryPrayer());
		prayers.add(getOfferingsPrayer());
		prayers.add(getSaintPrayer());
		prayers.add(getCommunionPrayer());
		prayers.add(getOthersPrayer());
		ExpandableListGroup userPrayers = getUserPrayers();
		if (userPrayers != null) {
			prayers.add(userPrayers);
		}
		return prayers;
	}

	/**
	 * Return group with the basics prayers
	 * 
	 * @return basics prayers
	 */
	private ExpandableListGroup getBasicPrayer() {
		ArrayList<ExpandableListChild> items = new ArrayList<ExpandableListChild>();
		items.add(new ExpandableListChild("Ave Maria", R.raw.ave_maria));
		items.add(new ExpandableListChild("Credo", R.raw.credo));
		items.add(new ExpandableListChild("Gloria", R.raw.gloria));
		items.add(new ExpandableListChild("Oracion al Espiritu Santo",
				R.raw.oracion_al_espiritu_santo));
		items.add(new ExpandableListChild("Padre Nuestro", R.raw.padre_nuestro));
		ExpandableListGroup group = new ExpandableListGroup(
				"Oraciones Basicas", items);
		return group;
	}

	/**
	 * Return a group with the offerings prayers
	 * 
	 * @return offerings prayers
	 */
	private ExpandableListGroup getOfferingsPrayer() {
		ArrayList<ExpandableListChild> items = new ArrayList<ExpandableListChild>();
		items.add(new ExpandableListChild("Ofrecimiento a la Virgen",
				R.raw.ofrecimiento_a_la_virgen));
		items.add(new ExpandableListChild("Ofrecimiento del Dia",
				R.raw.ofrecimiento_del_dia));
		items.add(new ExpandableListChild("Oracion antes de un Viaje",
				R.raw.oracion_antes_de_un_viaje));
		items.add(new ExpandableListChild("Oracion de Ofrecimiento",
				R.raw.oracion_de_ofrecimiento));
		ExpandableListGroup group = new ExpandableListGroup(
				"Oraciones de Ofrecimiento", items);
		return group;
	}

	/**
	 * Return a group with the others prayers
	 * 
	 * @return others prayers
	 */
	private ExpandableListGroup getOthersPrayer() {
		ArrayList<ExpandableListChild> items = new ArrayList<ExpandableListChild>();
		items.add(new ExpandableListChild("Acto de Contriccion",
				R.raw.acto_de_contriccion));
		items.add(new ExpandableListChild("Acto Penitencial",
				R.raw.acto_penitencial));
		items.add(new ExpandableListChild("Credo Largo", R.raw.credo_largo));
		items.add(new ExpandableListChild("Comunion Espiritual",
				R.raw.comunion_espiritual));
		items.add(new ExpandableListChild("Oracion por los Enfermos",
				R.raw.oracion_por_los_enfermos));
		items.add(new ExpandableListChild("Oracion a la Mano Poderosa",
				R.raw.oracion_a_la_mano_poderosa));
		ExpandableListGroup group = new ExpandableListGroup("Otras Oraciones",
				items);
		return group;
	}

	/**
	 * Return a group with the prayers of saints
	 * 
	 * @return prayers of saint
	 */
	private ExpandableListGroup getSaintPrayer() {
		ArrayList<ExpandableListChild> items = new ArrayList<ExpandableListChild>();
		items.add(new ExpandableListChild("Oracion a San Alberto Hurtado",
				R.raw.oracion_a_san_alberto_hurtado));
		items.add(new ExpandableListChild("Oracion a San Francisco de Asis",
				R.raw.oracion_a_san_francisco_de_asis));
		items.add(new ExpandableListChild("Oracion a Santa Rita",
				R.raw.oracion_a_santa_rita));
		items.add(new ExpandableListChild("Oracion a la Santisima Trinidad",
				R.raw.oracion_a_la_santisima_trinidad));
		ExpandableListGroup group = new ExpandableListGroup(
				"Oraciones a los Santos", items);
		return group;
	}

	/**
	 * Return a group with the Mary prayers
	 * 
	 * @return Mary prayers
	 */
	private ExpandableListGroup getMaryPrayer() {
		ArrayList<ExpandableListChild> items = new ArrayList<ExpandableListChild>();
		items.add(new ExpandableListChild("Angelus", R.raw.angelus));
		items.add(new ExpandableListChild("Ave Maria", R.raw.ave_maria));
		items.add(new ExpandableListChild("Misterios", R.raw.misterios));
		items.add(new ExpandableListChild("Ofrecimiento a la Virgen",
				R.raw.ofrecimiento_a_la_virgen));
		items.add(new ExpandableListChild("Salve", R.raw.salve));
		items.add(new ExpandableListChild("Regina Caeli", R.raw.regina_caeli));
		items.add(new ExpandableListChild("Bendita sea tu Pureza",
				R.raw.bendita_sea_tu_pureza));
		ExpandableListGroup group = new ExpandableListGroup(
				"Oraciones a la Virgen", items);
		return group;
	}

	/**
	 * Return a group with the communion prayers
	 * 
	 * @return communion prayers
	 */
	private ExpandableListGroup getCommunionPrayer() {
		ArrayList<ExpandableListChild> items = new ArrayList<ExpandableListChild>();
		items.add(new ExpandableListChild("Alma de Cristo",
				R.raw.alma_de_cristo));
		items.add(new ExpandableListChild("Oracion a Jesus Crucificado",
				R.raw.oracion_a_jesus_crucificado));
		items.add(new ExpandableListChild("Oracion por el Papa",
				R.raw.oracion_por_el_papa));
		items.add(new ExpandableListChild("Oracion por las Vocaciones",
				R.raw.oracion_por_las_vocaciones));
		ExpandableListGroup group = new ExpandableListGroup(
				"Oraciones post Comunion", items);
		return group;
	}

	/**
	 * Return a group with the prayers of a rosary
	 * 
	 * @return rosary prayers
	 */
	private ExpandableListGroup getRosaryPrayer() {
		ArrayList<ExpandableListChild> items = new ArrayList<ExpandableListChild>();
		items.add(new ExpandableListChild("Como rezar el Rosario",
				R.raw.como_rezar_el_rosario));
		items.add(new ExpandableListChild("Credo", R.raw.credo));
		items.add(new ExpandableListChild("Acto de Contricción",
				R.raw.acto_de_contriccion));
		items.add(new ExpandableListChild("Padre Nuestro", R.raw.padre_nuestro));
		items.add(new ExpandableListChild("Ave Maria", R.raw.ave_maria));
		items.add(new ExpandableListChild("Gloria", R.raw.gloria));
		items.add(new ExpandableListChild("Salve", R.raw.salve));
		items.add(new ExpandableListChild("Misterios", R.raw.misterios));
		items.add(new ExpandableListChild("Letanias", R.raw.letanias));
		ExpandableListGroup group = new ExpandableListGroup("Rosario", items);
		return group;
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
	 * Search for "Catholic Prayers" directory in the SD card and return it. If
	 * the directory doesn't exist this method create the directory and return
	 * it.
	 * 
	 * @return The directory "Catholic Prayers"
	 */
	private File find_createDirectory() {
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
					Toast toast = Toast.makeText(getApplicationContext(),
							String.format("Carpeta %s creada.", dirName),
							Toast.LENGTH_SHORT);
					toast.show();
				} else {
					Toast toast = Toast
							.makeText(getApplicationContext(), String.format(
									"Error creando carpeta %s.", dirName),
									Toast.LENGTH_SHORT);
					toast.show();
					directory = null;
				}
			}
		} catch (Exception e) {
			directory = null;
		}
		return directory;
	}

	/**
	 * This method search for the prayers that the user put on the
	 * "Catholic Prayers" directory. If the directory doesn't have prayers the
	 * method return null
	 * 
	 * @return The user prayers if they exist, null otherwise.
	 */
	private ExpandableListGroup getUserPrayers() {
		File directory = find_createDirectory();
		if (directory != null) {
			ArrayList<ExpandableListChild> items = new ArrayList<ExpandableListChild>();
			for (File file : directory.listFiles()) {
				if (file.isFile()) {
					items.add(new ExpandableListChild(file));
				}
			}
			if (!items.isEmpty()) {
				ExpandableListGroup group = new ExpandableListGroup(
						"Oraciones del Usuario", items);
				return group;
			} else {
				Util util = Util.getInstance();
				if (util.isShowMessage()) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"No hay oraciones de Usuario.", Toast.LENGTH_SHORT);
					toast.show();
					util.setShowMessage(false);
				}
				return null;
			}
		} else {
			Util util = Util.getInstance();
			if (util.isShowMessage()) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Error cargando oraciones de Usuario.",
						Toast.LENGTH_SHORT);
				toast.show();
				util.setShowMessage(false);
			}
			return null;
		}
	}

	/**
	 * Set the listener for the click on a child element. It also manage the
	 * action.
	 * 
	 * @param expList
	 *            The expandable list to set the click
	 */
	private void onChildClik(final ExpandableListView expList) {
		expList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Intent prayer = new Intent(MainActivity.this, ViewPrayers.class);
				ExpandableListAdapter list = (ExpandableListAdapter) parent
						.getExpandableListAdapter();
				ExpandableListChild child = (ExpandableListChild) list
						.getChild(groupPosition, childPosition);
				prayer.putExtra("child", child);
				startActivity(prayer);
				return true;
			}
		});
	}

	/**
	 * Save which group is expanded, so in refresh it's remain open
	 */
	private void onGroupClik() {
		expandList
				.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						groupsExpanded[groupPosition] = true;
					}
				});
		expandList
				.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {
						groupsExpanded[groupPosition] = false;
					}
				});
	}

}
