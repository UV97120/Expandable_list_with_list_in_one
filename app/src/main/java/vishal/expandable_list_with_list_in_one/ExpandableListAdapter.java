package vishal.expandable_list_with_list_in_one;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandableListAdapter extends BaseExpandableListAdapter{

	/*
	 * view types
	 */
	private static final int PAST_TRAVEL_VIEW = 1;
	private static final int FUTURE_TRAVEL_VIEW = 0;
	
	/*
	 * data
	 */
	private Context context = null;
	ArrayList<Group> groups = new ArrayList<Group>();
	
	public ExpandableListAdapter(Context context){
		this.context = context;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.BaseExpandableListAdapter#getChildType(int, int)
	 */
	@Override
	public int getChildType(int groupPosition, int childPosition) {
		int type = -1;
		if(groups.size() == 2 && groupPosition == 1){
			type = PAST_TRAVEL_VIEW;
		} else {
			type = FUTURE_TRAVEL_VIEW;
		}
		
		return type;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.BaseExpandableListAdapter#getChildTypeCount()
	 */
	@Override
	public int getChildTypeCount() {
		//Past and Future Travel Plans
		return 2;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).travelItems.get(childPosition);
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		//get the type of the group this child belongs
		Group.Type viewType = groups.get(groupPosition).type;
		View view = convertView;
		
		//if the type is future travel, use the future travel layout
		if(viewType == Group.Type.FUTURE) {
			if(view == null){
				view = LayoutInflater.from(context).inflate(R.layout.future_travel_line_item, parent, false);
				
				FutureTravelViewHolder holder = new FutureTravelViewHolder();
				holder.title = (TextView) view.findViewById(R.id.futureTravelTitle);
				holder.departure = (TextView) view.findViewById(R.id.futureTravelDeparture);
				holder.destination = (TextView) view.findViewById(R.id.futureTravelDestination);
				holder.date = (TextView) view.findViewById(R.id.futureTravelDate);
				holder.time = (TextView) view.findViewById(R.id.futureTravelTime);
				
				view.setTag(holder);
			}
			
			FutureTravelViewHolder holder = (FutureTravelViewHolder) view.getTag();
			
			TravelItem currentItem = (TravelItem)getChild(groupPosition, childPosition);
			
			holder.title.setText(currentItem.getTitle());
			holder.departure.setText(currentItem.getDeparture());
			holder.destination.setText(currentItem.getDestination());
			holder.date.setText(currentItem.getDate());
			holder.time.setText(currentItem.getTime());
		} else {
			//if the type is past, use the past travel layout
			if(view == null){
				view = LayoutInflater.from(context).inflate(R.layout.past_travel_line_item, parent, false);
				
				PastTravelViewHolder holder = new PastTravelViewHolder();
				holder.title = (TextView) view.findViewById(R.id.pastTravelTitle);
				holder.departure = (TextView) view.findViewById(R.id.pastTravelDeparture);
				holder.destination = (TextView) view.findViewById(R.id.pastTravelDestination);
				holder.date = (TextView) view.findViewById(R.id.pastTravelDate);
				
				view.setTag(holder);
			}
			
			PastTravelViewHolder holder = (PastTravelViewHolder) view.getTag();
			
			TravelItem currentItem = (TravelItem)getChild(groupPosition, childPosition);
			
			holder.title.setText(currentItem.getTitle());
			holder.departure.setText(currentItem.getDeparture());
			holder.destination.setText(currentItem.getDestination());
			holder.date.setText(currentItem.getDate());
		}
		return view;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).travelItems.size();
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		return groups.size();
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		
		View view = convertView;
		TextView text = null;
		ImageView image = null;
		
		if(view == null){
			view = LayoutInflater.from(context).inflate(R.layout.expandable_list_group_view, parent, false);
		}
		
		text = (TextView) view.findViewById(R.id.groupHeader);
		image = (ImageView) view.findViewById(R.id.expandableIcon);
		
		StringBuilder title = new StringBuilder();
		if(groupPosition == 0){
			title.append(context.getString(R.string.future_travel_list_header));
		} else {
			title.append(context.getString(R.string.past_travel_list_header));
		}
		title.append(" (");
		title.append(groups.get(groupPosition).travelItems.size());
		title.append(")");
		
		text.setText(title.toString());
		
		/*
		 * if this is not the first group (future travel) show the arrow image
		 * and change state if necessary
		 */
		if(groupPosition != 0){
			int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
			image.setImageResource(imageResourceId);
			
			image.setVisibility(View.VISIBLE);
		} else {
			image.setVisibility(View.INVISIBLE);
		}
		return view;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	/*
	 * setup travel plans and past trips into groups
	 */
	public void setupTrips(ArrayList<TravelItem> pastPlans, ArrayList<TravelItem> futurePlans){
		groups.clear();
		
		if(pastPlans != null){
			Group g1 = new Group();
			g1.type = Group.Type.FUTURE;
			g1.travelItems.clear();
			g1.travelItems = new ArrayList<TravelItem>(futurePlans);
			
			groups.add(g1);
		}
		if(futurePlans != null){
			Group g2 = new Group();
			g2.type = Group.Type.PAST;
			g2.travelItems.clear();
			g2.travelItems = new ArrayList<TravelItem>(pastPlans);
			
			groups.add(g2);
		}
		
		notifyDataSetChanged();
	}
	
	/*
	 * Holder for the Past view type
	 */
	class PastTravelViewHolder {
		TextView title;
		TextView departure;
		TextView destination;
		TextView date;
	}
	
	/*
	 * Holder for the Future view type
	 */
	class FutureTravelViewHolder {
		TextView title;
		TextView departure;
		TextView destination;
		TextView date;
		TextView time;
	}
	
	/*
	 * Wrapper for each group that contains the
	 * list elements and the type of travel.
	 */
	public static class Group {
		public enum Type {
			PAST, FUTURE;
		};
		
		public Type type;
		ArrayList<TravelItem> travelItems = new ArrayList<TravelItem>();
	}
}