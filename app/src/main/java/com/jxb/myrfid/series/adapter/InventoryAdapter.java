package com.jxb.myrfid.series.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxb.myrfid.R;
import com.jxb.myrfid.series.reader.model.InventoryBuffer.InventoryTagMap;

import java.util.List;

public class InventoryAdapter extends BaseAdapter{

	private LayoutInflater layoutInflater;
	private Context context;
	
	private List<InventoryTagMap> list = null;
	
	public InventoryAdapter(Context context,List<InventoryTagMap> list){
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	public final class ListItemView{                //自定义控件集合     
		public TextView mIdText;
		public TextView mEpcText;
		public TextView mPcText;
		public TextView mTimesText;
		public TextView mRssiText;
		public TextView mFreqText;
		
    }

	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View layout, ViewGroup parent) {

		ListItemView  listItemView = null;
		
		if(layout == null){
			listItemView = new ListItemView();
			layout = layoutInflater.inflate(R.layout.inventory_item_list, null);
			listItemView.mIdText = (TextView)layout.findViewById(R.id.id_text);
			listItemView.mEpcText = (TextView)layout.findViewById(R.id.epc_text);
			listItemView.mPcText = (TextView)layout.findViewById(R.id.pc_text);
			listItemView.mTimesText = (TextView)layout.findViewById(R.id.times_text);
			listItemView.mRssiText = (TextView)layout.findViewById(R.id.rssi_text);
			listItemView.mFreqText = (TextView)layout.findViewById(R.id.freq_text);
			layout.setTag(listItemView);
		}else{
			listItemView = (ListItemView) layout.getTag();
		}
		
		InventoryTagMap map = list.get(position);
		/*if(map.strEPC.equals("30000002") || map.strEPC.equals("300833B2DDD9014000000000")){
			System.out.println("遇到有问题的标签");
		}*/
		
		listItemView.mIdText.setText(String.valueOf(position+1));
		listItemView.mEpcText.setText(map.strEPC);
		listItemView.mPcText.setText(map.strPC);
		listItemView.mTimesText.setText(String.valueOf(map.nReadCount));
		try {
			listItemView.mRssiText.setText((Integer.parseInt(map.strRSSI) - 129) + "dBm");
		} catch (Exception e) {
			listItemView.mRssiText.setText("");
		}
		
		listItemView.mFreqText.setText(map.strFreq);
		return layout;
	}
}
