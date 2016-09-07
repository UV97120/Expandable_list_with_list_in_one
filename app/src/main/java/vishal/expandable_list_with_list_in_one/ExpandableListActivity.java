package vishal.expandable_list_with_list_in_one;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;


public class ExpandableListActivity extends Activity {

	private ExpandableListView expList;
	private ExpandableListAdapter expListAdapter;
	private Resources res;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandable_list_layout);
		
		expList = (ExpandableListView) findViewById(R.id.expandableList);
		
		expListAdapter = new ExpandableListAdapter(getApplicationContext());
		
		//removes the standard group state indicator
		expList.setGroupIndicator(null);
		
		/*
		 * Setting up mock data
		 */
		ArrayList<TravelItem> futureItems = new ArrayList<TravelItem>();
		ArrayList<TravelItem> pastItems = new ArrayList<TravelItem>();
		
		res = getResources();
		
		for(int x = 0; x < res.getStringArray(R.array.future_titles).length; x++){
			TravelItem future = new TravelItem();
			future.setTitle(res.getStringArray(R.array.future_titles)[x]);
			future.setDeparture(res.getStringArray(R.array.future_departures)[x]);
			future.setDestination(res.getStringArray(R.array.future_destinations)[x]);
			future.setDate(res.getStringArray(R.array.future_dates)[x]);
			future.setTime(res.getStringArray(R.array.future_times)[x]);
			
			futureItems.add(future);
		}
		for(int y = 0; y < res.getStringArray(R.array.past_titles).length; y++){
			TravelItem past = new TravelItem();
			past.setTitle(res.getStringArray(R.array.past_titles)[y]);
			past.setDeparture(res.getStringArray(R.array.past_departures)[y]);
			past.setDestination(res.getStringArray(R.array.past_destinations)[y]);
			past.setDate(res.getStringArray(R.array.past_dates)[y]);
			
			pastItems.add(past);
		}
		
		//send data to adapter
		expListAdapter.setupTrips(pastItems, futureItems);
		//tie the adapter to our expandable list view
		expList.setAdapter(expListAdapter);
		
		/*
		 * if there is at least one child, expand the first
		 * group by default.  (future travel plans)
		 */
		if(expListAdapter.getChildrenCount(0) >= 1){
			expList.expandGroup(0);
		}

		/*
		 * override the onGroupClick method to make sure the
		 * first group (future travel plans) does not collapse
		 * or expand.
		 */
		/*expList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if(groupPosition == 0){
					return true;
				} else {
					return false;
				}
			}
		});*/
	}

}
