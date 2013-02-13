package cl.flores.catholicprayers;

import java.io.File;

import cl.flores.catholicprayers.clases.ExpandableListChild;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class Util {
	private static Util util;
	private String[] colorGroup;
	private String[][] colorChild;
	private boolean[] groupsExpanded;
	private boolean showMessage;
	private int updateDialog;
	private int upToDateDialog;
	private String link;
	private ExpandableListChild child;
	private int deleteDialog;

	// Predetermined values
	private String titleSize = "20";
	private String groupSize = "20";
	private String childSize = "20";
	private String prayerMinSize = "12";
	private String prayerMaxSize = "36";
	private String prayerLength = "50";
	private boolean usePrayerSize = true;
	private boolean usePrayerLength = true;
	public float textSize = 20;
	private boolean update = true;

	/**
	 * The constructor of the class Util. Here are the definition of the colors.
	 */
	private Util() {
		colorGroup = new String[] { "#000010", "#000020", "#000030", "#000040",
				"#000050", "#000060", "#000070", "#000080", "#000090",
				"#0000a0", "#0000b0", "#0000c0", "#0000d0", "#0000e0",
				"#0000f0" };
		colorChild = new String[][] {
				{ "#001010", "#002010", "#003010", "#004010", "#005010",
						"#006010", "#007010", "#008010", "#009010", "#00a010",
						"#00b010", "#00c010", "#00d010", "#00e010", "#00f010" },
				{ "#001020", "#002020", "#003020", "#004020", "#005020",
						"#006020", "#007020", "#008020", "#009020", "#00a020",
						"#00b020", "#00c020", "#00d020", "#00e020", "#00f020" },
				{ "#001030", "#002030", "#003030", "#004030", "#005030",
						"#006030", "#007030", "#008030", "#009030", "#00a030",
						"#00b030", "#00c030", "#00d030", "#00e030", "#00f030" },
				{ "#001040", "#002040", "#003040", "#004040", "#005040",
						"#006040", "#007040", "#008040", "#009040", "#00a040",
						"#00b040", "#00c040", "#00d040", "#00e040", "#00f040" },
				{ "#001050", "#002050", "#003050", "#004050", "#005050",
						"#006050", "#007050", "#008050", "#009050", "#00a050",
						"#00b050", "#00c050", "#00d050", "#00e050", "#00f050" },
				{ "#001060", "#002060", "#003060", "#004060", "#005060",
						"#006060", "#007060", "#008060", "#009060", "#00a060",
						"#00b060", "#00c060", "#00d060", "#00e060", "#00f060" },
				{ "#001070", "#002070", "#003070", "#004070", "#005070",
						"#006070", "#007070", "#008070", "#009070", "#00a070",
						"#00b070", "#00c070", "#00d070", "#00e070", "#00f070" },
				{ "#001080", "#002080", "#003080", "#004080", "#005080",
						"#006080", "#007080", "#008080", "#009080", "#00a080",
						"#00b080", "#00c080", "#00d080", "#00e080", "#00f080" },
				{ "#001090", "#002090", "#003090", "#004090", "#005090",
						"#006090", "#007090", "#008090", "#009090", "#00a090",
						"#00b090", "#00c090", "#00d090", "#00e090", "#00f090" },
				{ "#0010a0", "#0020a0", "#0030a0", "#0040a0", "#0050a0",
						"#0060a0", "#0070a0", "#0080a0", "#0090a0", "#00a0a0",
						"#00b0a0", "#00c0a0", "#00d0a0", "#00e0a0", "#00f0a0" },
				{ "#0010b0", "#0020b0", "#0030b0", "#0040b0", "#0050b0",
						"#0060b0", "#0070b0", "#0080b0", "#0090b0", "#00a0b0",
						"#00b0b0", "#00c0b0", "#00d0b0", "#00e0b0", "#00f0b0" },
				{ "#0010c0", "#0020c0", "#0030c0", "#0040c0", "#0050c0",
						"#0060c0", "#0070c0", "#0080c0", "#0090c0", "#00a0c0",
						"#00b0c0", "#00c0c0", "#00d0c0", "#00e0c0", "#00f0c0" },
				{ "#0010d0", "#0020d0", "#0030d0", "#0040d0", "#0050d0",
						"#0060d0", "#0070d0", "#0080d0", "#0090d0", "#00a0d0",
						"#00b0d0", "#00c0d0", "#00d0d0", "#00e0d0", "#00f0d0" },
				{ "#0010e0", "#0020e0", "#0030e0", "#0040e0", "#0050e0",
						"#0060e0", "#0070e0", "#0080e0", "#0090e0", "#00a0e0",
						"#00b0e0", "#00c0e0", "#00d0e0", "#00e0e0", "#00f0e0" },
				{ "#0010f0", "#0020f0", "#0030f0", "#0040f0", "#0050f0",
						"#0060f0", "#0070f0", "#0080f0", "#0090f0", "#00a0f0",
						"#00b0f0", "#00c0f0", "#00d0f0", "#00e0f0", "#00f0f0" } };
		showMessage = true;
		updateDialog = 1;
		upToDateDialog = 2;
		deleteDialog = 3;
	}

	/**
	 * Return an unic instance of Util
	 * 
	 * @return util
	 */
	public static Util getInstance() {
		if (util == null) {
			util = new Util();
		}
		return util;
	}

	/**
	 * Returns a new color of a group, based on the actual color
	 * 
	 * @param groupPosition
	 *            The position of the group
	 * @return the new color
	 */
	public String getNextColorGroup(int groupPosition) {
		int pos = groupPosition % colorGroup.length;
		return colorGroup[pos];
	}

	/**
	 * Returns a new color of a child, based on the actual color
	 * 
	 * @param groupPosition
	 *            The position of the gorup
	 * @param childPosition
	 *            The position of the child
	 * @return the new color
	 */
	public String getNextColorChild(int groupPosition, int childPosition) {
		int posGroup = groupPosition % colorChild.length;
		int posChild = childPosition % colorChild[posGroup].length;
		return colorChild[posGroup][posChild];
	}

	/**
	 * @return the titleSize
	 */
	public String getTitleSize() {
		return titleSize;
	}

	/**
	 * @return the groupSize
	 */
	public String getGroupSize() {
		return groupSize;
	}

	/**
	 * @return the childSize
	 */
	public String getChildSize() {
		return childSize;
	}

	/**
	 * @return the prayerMinSize
	 */
	public String getPrayerMinSize() {
		return prayerMinSize;
	}

	/**
	 * @return the prayerMaxSize
	 */
	public String getPrayerMaxSize() {
		return prayerMaxSize;
	}

	/**
	 * @return the prayerLength
	 */
	public String getPrayerLength() {
		return prayerLength;
	}

	/**
	 * @return the usePrayerSize
	 */
	public boolean isUsePrayerSize() {
		return usePrayerSize;
	}

	/**
	 * @return the usePrayerLength
	 */
	public boolean isUsePrayerLength() {
		return usePrayerLength;
	}

	/**
	 * @return the groupsExpanded
	 */
	public boolean[] getGroupsExpanded() {
		return groupsExpanded;
	}

	/**
	 * @param groupsExpanded
	 *            the groupsExpanded to set
	 */
	public void setGroupsExpanded(boolean[] groupsExpanded) {
		this.groupsExpanded = groupsExpanded;
	}

	/**
	 * @return the showMessage
	 */
	public boolean isShowMessage() {
		return showMessage;
	}

	/**
	 * @param showMessage
	 *            the showMessage to set
	 */
	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	/**
	 * @return the updateDialog
	 */
	public int getUpdateDialog() {
		return updateDialog;
	}

	/**
	 * @return the upToDateDialog
	 */
	public int getUpToDateDialog() {
		return upToDateDialog;
	}

	/**
	 * Search for updates of the application. If there any update it return the
	 * link to download the update.
	 * 
	 * @param context
	 *            The application context, where to find the elements and show
	 *            the notifications.
	 * @param url
	 *            The URL where to find the update
	 */
	public int update(Context context, boolean result, String message,
			String url) {
		if (result) {
			link = url;
			try {
				double thisVersion = Double.parseDouble(context
						.getString(R.string.version));
				double serverVersion = Double.parseDouble(message);
				if (thisVersion < serverVersion) {
					return updateDialog;
				} else {
					return upToDateDialog;
				}
			} catch (Exception e) {
				Toast toast = Toast.makeText(context, "Error Inesperado.",
						Toast.LENGTH_SHORT);
				toast.show();
				return 0;
			}
		} else {
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.show();
			return 0;
		}
	}

	/**
	 * Create a new dialog to inform the user that the application is up to
	 * date.
	 * 
	 * @param context
	 *            The context where to show the dialog.
	 * @return Information dialog
	 */
	public Dialog upToDateDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Catholic Prayers");
		builder.setMessage("La aplicación esta actualizada.");
		builder.setPositiveButton("Aceptar", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.create();
	}

	/**
	 * Create a new dialog to ask the user to download the update.
	 * 
	 * @param context
	 *            The context where to show the dialog.
	 * @return
	 */
	public Dialog updateDialog(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Catholic Prayers");
		builder.setMessage("Hay una nueva versión. ¿Desea descargarla?");
		builder.setPositiveButton("Descargar", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent download = new Intent(Intent.ACTION_VIEW);
				download.setData(Uri.parse(link));
				try {
					context.startActivity(download);
				} catch (ActivityNotFoundException e) {
					Toast toast = Toast.makeText(context, "Error Inesperado.",
							Toast.LENGTH_SHORT);
					toast.show();
				} finally {
					dialog.cancel();
				}
			}
		});
		builder.setNegativeButton("Cancelar", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.create();
	}

	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @param update
	 *            the update to set
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}

	public int setDeleteDialog(ExpandableListChild child){
		this.child = child;
		return 3;
	}
	
	public int getDeleteDialog(){
		return deleteDialog;
	}
	
	public Dialog deleteDialog(final Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Catholic Prayers");
		builder.setMessage("¿Eliminar Oracion?");
		builder.setPositiveButton("Eliminar", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(child.getPath() != null){
					String status = Environment.getExternalStorageState();
					if (status.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
						File prayer = child.getPath();
						if(prayer.delete()){
							Toast.makeText(context, "Oracion eliminada", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(context, "Error eliminando oracion", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(context, "Error eliminando oracion", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(context, "No se puede eliminar oraciones internas", Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("Cancelar", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.create();
	}
}
