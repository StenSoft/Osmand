package net.osmand.plus.parkingpoint;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import net.osmand.data.LatLon;
import net.osmand.plus.OsmandPlugin;
import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.base.MenuBottomSheetDialogFragment;
import net.osmand.plus.base.bottomsheetmenu.BaseBottomSheetItem;
import net.osmand.plus.base.bottomsheetmenu.SimpleBottomSheetItem;
import net.osmand.plus.base.bottomsheetmenu.simpleitems.TitleItem;


public class ParkingTypeBottomSheetDialogFragment extends MenuBottomSheetDialogFragment {

	public static final String TAG = "ParkingTypeBottomSheetDialogFragment";
	public static final String LAT_KEY = "latitude";
	public static final String LON_KEY = "longitude";

	@Override
	public void createMenuItems(Bundle savedInstanceState) {
		items.add(new TitleItem(getString(R.string.parking_options)));
		BaseBottomSheetItem byTypeItem = new SimpleBottomSheetItem.Builder()
				.setIcon(getContentIcon(R.drawable.ic_action_time_start))
				.setTitle(getString(R.string.osmand_parking_no_lim_text))
				.setLayoutId(R.layout.bottom_sheet_item_simple)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						addParkingPositionByType(false);
					}
				})
				.create();
		items.add(byTypeItem);

		BaseBottomSheetItem byDateItem = new SimpleBottomSheetItem.Builder()
				.setIcon(getContentIcon(R.drawable.ic_action_time_span))
				.setTitle(getString(R.string.osmand_parking_time_limit))
				.setLayoutId(R.layout.bottom_sheet_item_simple)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						addParkingPositionByType(true);
					}
				})
				.create();
		items.add(byDateItem);
	}

	private void addParkingPositionByType(boolean limited) {
		Bundle args = getArguments();
		LatLon latLon = new LatLon(args.getDouble(LAT_KEY), args.getDouble(LON_KEY));
		ParkingPositionPlugin plugin = OsmandPlugin.getEnabledPlugin(ParkingPositionPlugin.class);
		MapActivity mapActivity = (MapActivity) getActivity();

		if (plugin != null) {
			if (plugin.isParkingEventAdded()) {
				plugin.showDeleteEventWarning(mapActivity);
			}
			if (limited) {
				plugin.setParkingPosition(mapActivity, latLon.getLatitude(), latLon.getLongitude(), true);
				plugin.showSetTimeLimitDialog(mapActivity, new Dialog(getContext()));
				mapActivity.getMapView().refreshMap();
			} else {
				plugin.addOrRemoveParkingEvent(false);
				plugin.setParkingPosition(mapActivity, latLon.getLatitude(), latLon.getLongitude(), false);
				plugin.showContextMenuIfNeeded(mapActivity, true);
				mapActivity.refreshMap();
			}
		}
		dismiss();
	}
}
