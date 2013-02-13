package cl.flores.catholicprayers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import cl.flores.catholicprayers.clases.ExpandableListChild;
import cl.flores.catholicprayers.clases.ExpandableListGroup;

import android.content.Context;
import android.os.Bundle;
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
		ArrayList<ExpandableListGroup> prayers = getPrayers();
		Bundle bundle = new Bundle();
		bundle.putSerializable("prayers", prayers);
		m.setData(bundle);
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
		Collections.sort(prayers);
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
		Collections.sort(items);
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
		Collections.sort(items);
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
		Collections.sort(items);
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
		Collections.sort(items);
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
		Collections.sort(items);
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
		Collections.sort(items);
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
		items.add(new ExpandableListChild("Acto de Contriccion",
				R.raw.acto_de_contriccion));
		items.add(new ExpandableListChild("Padre Nuestro", R.raw.padre_nuestro));
		items.add(new ExpandableListChild("Ave Maria", R.raw.ave_maria));
		items.add(new ExpandableListChild("Gloria", R.raw.gloria));
		items.add(new ExpandableListChild("Salve", R.raw.salve));
		items.add(new ExpandableListChild("Misterios", R.raw.misterios));
		items.add(new ExpandableListChild("Letanias", R.raw.letanias));
		Collections.sort(items);
		ExpandableListGroup group = new ExpandableListGroup("Rosario", items);
		return group;
	}

	/**
	 * Search for "Catholic Prayers" directory in the SD card and return it. If
	 * the directory doesn't exist return null
	 * 
	 * @return The directory "Catholic Prayers"
	 */
	private File find_directory() {
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
		File directory = find_directory();
		if (directory != null) {
			ArrayList<ExpandableListChild> items = new ArrayList<ExpandableListChild>();
			for (File file : directory.listFiles()) {
				if (file.isFile()) {
					items.add(new ExpandableListChild(file));
				}
			}
			if (!items.isEmpty()) {
				Collections.sort(items);
				ExpandableListGroup group = new ExpandableListGroup(
						"Oraciones del Usuario", items);
				return group;
			} else {
				Toast toast = Toast.makeText(context,
						"No hay oraciones de Usuario.", Toast.LENGTH_SHORT);
				toast.show();
				return null;
			}
		} else {
			Toast toast = Toast.makeText(context,
					"Error cargando oraciones de Usuario.", Toast.LENGTH_SHORT);
			toast.show();
			return null;
		}
	}
}
