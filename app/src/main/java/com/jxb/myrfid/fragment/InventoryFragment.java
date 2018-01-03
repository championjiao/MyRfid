package com.jxb.myrfid.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jxb.myrfid.R;
import com.jxb.myrfid.series.adapter.InventoryAdapter;
import com.jxb.myrfid.series.model.ResponseHandler;
import com.jxb.myrfid.series.operation.U8Series;
import com.jxb.myrfid.series.reader.model.InventoryBuffer;
import com.jxb.myrfid.series.reader.model.InventoryBuffer.InventoryTagMap;
import com.jxb.myrfid.series.reader.server.ReaderHelper;
import com.jxb.myrfid.series.utils.FileUtil;
import com.jxb.myrfid.series.utils.MusicPlayer;
import com.jxb.myrfid.series.utils.MusicPlayer.Type;
import com.fntech.Loger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@SuppressLint("ValidFragment")
public class InventoryFragment extends Fragment implements OnClickListener {

	private static final String TAG = "InventoryFragment";

	private U8Series mUSeries;

	private static Context context;
	public static Button btnStart;
	public static Button btnStop;
	private Button btnClear;
	private Button btnSave;
	private ListView tagListView;
	private TextView txtRealInventoryCount;

	private InventoryAdapter inventoryAdapter;
	private List<InventoryTagMap> inventoryTagData = new ArrayList<InventoryTagMap>();
	private String stringTxtRealInventoryCount = "";
	private int inventoryTimeHistory = 0;
	private long mRefreshTime;

	private ReaderHelper mReaderHelper;
	private static InventoryBuffer m_curInventoryBuffer;

	public static boolean isInventory;

	private Handler mHandler;

	public InventoryFragment(Context context) {
		InventoryFragment.context = context;
		mUSeries = U8Series.getInstance();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler();

		try {
			mReaderHelper = ReaderHelper.getDefaultHelper();
		} catch (Exception e) {
			e.printStackTrace();
		}
		m_curInventoryBuffer = mReaderHelper.getCurInventoryBuffer();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View layout = inflater.inflate(R.layout.activity_inventory, container, false);

		btnStart = (Button) layout.findViewById(R.id.bt_start);
		btnStop = (Button) layout.findViewById(R.id.bt_stop);
		btnClear = (Button) layout.findViewById(R.id.bt_clear);
		btnSave = (Button) layout.findViewById(R.id.bt_save);
		tagListView = (ListView) layout.findViewById(R.id.inventory_tag_list_view);
		txtRealInventoryCount = (TextView) layout.findViewById(R.id.txtRealInventoryCount);
		inventoryAdapter = new InventoryAdapter(context, inventoryTagData);
		tagListView.setAdapter(inventoryAdapter);
		btnStop.setEnabled(false);
		return layout;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		stringTxtRealInventoryCount = this.getString(R.string.inventoryCountText);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		btnClear.setOnClickListener(this);
		btnSave.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.bt_start:
				Loger.disk_log("bt_start", "开始按键被按下", "U8");
				isInventory = true;
				try {
					mRefreshTime = new Date().getTime();
					mUSeries.startInventory(new ResponseHandler() {

						@Override
						public void onFailure(String msg) {
							super.onFailure(msg);
							Toast.makeText(context, getResources().getText(R.string.Disk_failure) + msg, Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(String msg, Object data, byte[] parameters) {
							super.onSuccess(msg, data, parameters);
							if (msg.equals(U8Series.REFRESHTEXT)) {
								refreshText();
							} else if (msg.equals(U8Series.REFRESHLIST)) {
								inventoryTagData.clear();
								inventoryTagData.addAll((Collection<? extends InventoryTagMap>) data);
								inventoryAdapter.notifyDataSetChanged();
							} else {
								System.out.println("盘询成功,返回标识异常");
							}
						}

					});
					mHandler.postDelayed(mRefreshRunnable, 500);
					btnStart.setEnabled(false);
					btnStop.setEnabled(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.bt_stop:
				stopInventory();
				break;
			case R.id.bt_clear:
				mRefreshTime = new Date().getTime();
				inventoryTimeHistory = 0;
				m_curInventoryBuffer.clearTagMap();
				mReaderHelper.clearInventoryTotal();
				inventoryTagData.clear();
				inventoryAdapter.notifyDataSetChanged();
				if (isInventory)
					refreshText();
				else
					txtRealInventoryCount.setText(String.format(stringTxtRealInventoryCount, 0, 0, 0, 0));

				break;
			case R.id.bt_save:
				if (inventoryTagData != null && inventoryTagData.size() > 0 && FileUtil.saveInventoryData(inventoryTagData)) {
					Toast.makeText(context, getString(R.string.str_save_succeed), Toast.LENGTH_SHORT).show();
					MusicPlayer.getInstance().play(Type.OK);
				} else {
					Toast.makeText(context, getString(R.string.str_save_failed), Toast.LENGTH_SHORT).show();
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				}
				break;
			default:
				break;
		}
	}

	private void stopInventory() {
		isInventory = false;
		try {
			mUSeries.stopInventory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mHandler.removeCallbacks(mRefreshRunnable);
		// inventoryTimeHistory += ((new Date().getTime()) - mRefreshTime) /
		// 1000;
		btnStop.setEnabled(false);
		btnStart.setEnabled(true);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (isInventory)
			stopInventory();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	private Runnable mRefreshRunnable = new Runnable() {
		public void run() {
			refreshText();
			mHandler.postDelayed(this, 2000);
		}
	};

	public void refreshText() {
		long now = new Date().getTime();
		String text = String.format(stringTxtRealInventoryCount, m_curInventoryBuffer.lsTagList.size(), mReaderHelper.getInventoryTotal(), m_curInventoryBuffer.nReadRate, (now - mRefreshTime) / 1000 + inventoryTimeHistory);
		txtRealInventoryCount.setText(text);
	}
}
