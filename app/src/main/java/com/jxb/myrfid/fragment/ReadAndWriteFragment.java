package com.jxb.myrfid.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jxb.myrfid.CustomTextWatcher;
import com.jxb.myrfid.R;
import com.jxb.myrfid.series.model.Message;
import com.jxb.myrfid.series.operation.U8Series;
import com.jxb.myrfid.series.reader.model.InventoryBuffer;

import com.jxb.myrfid.series.utils.MusicPlayer;
import com.jxb.myrfid.series.utils.MusicPlayer.Type;
import com.jxb.myrfid.series.utils.StringTool;
import com.jxb.myrfid.series.utils.Tools;

import com.fntech.Loger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class ReadAndWriteFragment extends Fragment implements OnClickListener {

	private Context mContext;

	private TextView mRead, mWrite, mLock, mKill;// mGet, mSelect,

	private EditText returnData;

	private Spinner mTagEpcListSpinner;

	private List<String> list;
	private ArrayAdapter<String> adapter;

	private EditText mPasswordEditText;
	private EditText mStartAddrEditText;
	private EditText mDataLenEditText;
	
	private EditText mDataEditText;
	private EditText mLockPasswordEditText;
	private EditText mKillPasswordEditText;
	private RadioGroup mGroupAccessAreaType;
	private RadioGroup mGroupLockAreaType;
	private RadioGroup mGroupLockType;

	// public static boolean haveSetEPC;

	private U8Series mUSeries;

	public static enum rwBlock {
		ACSESSPRASSWORD, KILLPRASSWORD, EPC, TID, USER
	}

	public ReadAndWriteFragment(Context context) {
		this.mContext = context;
	}

	@Override
	public void onResume() {
		super.onResume();
	};

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUSeries = U8Series.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.activity_readwrite, container, false);

		mTagEpcListSpinner = (Spinner) layout.findViewById(R.id.tag_epc_list_spinner);

		returnData = (EditText) layout.findViewById(R.id.return_data);
		returnData.addTextChangedListener(new CustomTextWatcher(returnData));

		mRead = (TextView) layout.findViewById(R.id.read);
		mWrite = (TextView) layout.findViewById(R.id.write);
		mLock = (TextView) layout.findViewById(R.id.lock);
		mKill = (TextView) layout.findViewById(R.id.kill);

		mPasswordEditText = (EditText) layout.findViewById(R.id.password_text);
		mPasswordEditText.addTextChangedListener(new CustomTextWatcher(mPasswordEditText));
		
		mStartAddrEditText = (EditText) layout.findViewById(R.id.start_addr_text);
		mDataLenEditText = (EditText) layout.findViewById(R.id.data_length_text);
		
		mDataEditText = (EditText) layout.findViewById(R.id.data_write_text);
		mDataEditText.addTextChangedListener(new CustomTextWatcher(mDataEditText));
		
		mLockPasswordEditText = (EditText) layout.findViewById(R.id.lock_password_text);
		mLockPasswordEditText.addTextChangedListener(new CustomTextWatcher(mLockPasswordEditText));
		
		mKillPasswordEditText = (EditText) layout.findViewById(R.id.kill_password_text);
		mKillPasswordEditText.addTextChangedListener(new CustomTextWatcher(mKillPasswordEditText));

		mGroupAccessAreaType = (RadioGroup) layout.findViewById(R.id.group_access_area_type);
		mGroupLockAreaType = (RadioGroup) layout.findViewById(R.id.group_lock_area_type);
		mGroupLockType = (RadioGroup) layout.findViewById(R.id.group_lock_type);

		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, list);
		mTagEpcListSpinner.setAdapter(adapter);
		mTagEpcListSpinner.setSelection(0);

		return layout;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mTagEpcListSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				String strEPC = mTagEpcListSpinner.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		mRead.setOnClickListener(this);
		mWrite.setOnClickListener(this);
		mLock.setOnClickListener(this);
		mKill.setOnClickListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	public void addEpcList(Context context, InventoryBuffer m_curInventoryBuffer) {

		list.clear();
		list.add("Cancel");
		for (int i = 0; i < m_curInventoryBuffer.lsTagList.size(); i++) {
			list.add(m_curInventoryBuffer.lsTagList.get(i).strEPC);
		}
		adapter.notifyDataSetChanged();
		mTagEpcListSpinner.setSelection(0);
	}
	int i=0;
	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.read || id == R.id.write) {
			// 数据处理
			byte btMemBank = 0x00;
			byte btWordAdd = 0x00;
			byte btWordCnt = 0x00;
			byte[] btAryPassWord = null;
			if (mGroupAccessAreaType.getCheckedRadioButtonId() == R.id.set_access_area_password) {
				btMemBank = 0x00;
			} else if (mGroupAccessAreaType.getCheckedRadioButtonId() == R.id.set_access_area_epc) {
				btMemBank = 0x01;
			} else if (mGroupAccessAreaType.getCheckedRadioButtonId() == R.id.set_access_area_tid) {
				btMemBank = 0x02;
			} else if (mGroupAccessAreaType.getCheckedRadioButtonId() == R.id.set_access_area_user) {
				btMemBank = 0x03;
			}
			try {
				btWordAdd = (byte) Integer.parseInt(mStartAddrEditText.getText().toString());
			} catch (Exception e) {
				MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				Toast.makeText(mContext, getResources().getString(R.string.param_start_addr_error), Toast.LENGTH_SHORT).show();
				return;
			}
			try {
//				String[] reslut = StringTool.stringToStringArray(mPasswordEditText.getText().toString().toUpperCase(Locale.ENGLISH), 2);
//				btAryPassWord = StringTool.stringArrayToByteArray(reslut, 4);
				btAryPassWord = Tools.stringToByteArray(mPasswordEditText.getText().toString().toUpperCase(Locale.ENGLISH));
				if(btAryPassWord.length!=4){
					throw new Exception("password length not right");
				}
			} catch (Exception e) {
				MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				Toast.makeText(mContext, getResources().getString(R.string.param_password_error), Toast.LENGTH_SHORT).show();
				return;
			}
			// 数据处理结束

			// 如果是读操作
			if (arg0.getId() == R.id.read) {
				try {
					btWordCnt = (byte) Integer.parseInt(mDataLenEditText.getText().toString());
				} catch (Exception e) {
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
					Toast.makeText(mContext, getResources().getString(R.string.param_data_len_error), Toast.LENGTH_SHORT).show();
					return;
				}

				if ((btWordCnt & 0xFF) <= 0) {
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
					Toast.makeText(mContext, getResources().getString(R.string.param_data_len_error), Toast.LENGTH_SHORT).show();
					return;
				}
				Message msg = null;
				try {
					if (mTagEpcListSpinner.getSelectedItem().toString().equalsIgnoreCase("cancel")) {
						msg = mUSeries.readTagMemory(mTagEpcListSpinner.getSelectedItem().toString().getBytes(), btMemBank, btWordCnt, btWordAdd, btAryPassWord);
					} else {
						String[] result = StringTool.stringToStringArray(mTagEpcListSpinner.getSelectedItem().toString(), 2);
						byte[] epcSelected = StringTool.stringArrayToByteArray(result, result.length);
						msg = mUSeries.readTagMemory(epcSelected, btMemBank, btWordCnt, btWordAdd, btAryPassWord);

					}
					if (msg.getCode() == 0) {
						returnData.setText(msg.getResult());
						MusicPlayer.getInstance().play(Type.OK);
					} else {
						returnData.setText("");
						MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
						Toast.makeText(mContext, msg.getMessage(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(mContext, getResources().getText(R.string.Read_Tag_Failure), Toast.LENGTH_SHORT).show();
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
					Loger.disk_log("Read", "readTagMemoryException,info = " + e.toString(), "M10_U8");
				}
			} else {
				// 写操作
				byte[] btAryData = null;
				String[] result = null;
				try {
					btWordCnt = (byte) Integer.parseInt(mDataLenEditText.getText().toString());
				} catch (Exception e) {
					Toast.makeText(mContext, getResources().getString(R.string.param_data_len_error), Toast.LENGTH_SHORT).show();
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
					return;
				}

				if ((btWordCnt & 0xFF) <= 0) {
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
					Toast.makeText(mContext, getResources().getString(R.string.param_data_len_error), Toast.LENGTH_SHORT).show();
					return;
				}
				try {
//					result = StringTool.stringToStringArray(mDataEditText.getText().toString().toUpperCase(Locale.ENGLISH), 2);
//					btAryData = StringTool.stringArrayToByteArray(result, result.length);
					btAryData= Tools.stringToByteArray(mDataEditText.getText().toString().toUpperCase(Locale.ENGLISH));
					Log.e("yourTag", " btAryData.length "+btAryData.length+" btWordCnt= "+btWordCnt);
//					btWordCnt = (byte) ((result.length / 2 + result.length % 2) & 0xFF);
					if (btAryData.length % 2 != 0 || btWordCnt != btAryData.length / 2) {
						MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
						Toast.makeText(mContext,  getResources().getString(R.string.param_data_len_error), Toast.LENGTH_SHORT).show();
						return;
					}
				} catch (Exception e) {
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
					Toast.makeText(mContext, getResources().getString(R.string.param_data_error), Toast.LENGTH_SHORT).show();
					return;
				}

				if (btAryData == null || btAryData.length <= 0) {
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
					Toast.makeText(mContext, getResources().getString(R.string.param_data_error), Toast.LENGTH_SHORT).show();
					return;
				}

				if (btAryPassWord == null || btAryPassWord.length < 4) {
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
					Toast.makeText(mContext, getResources().getString(R.string.param_password_error), Toast.LENGTH_SHORT).show();
					return;
				}
				// mDataLenEditText.setText(String.valueOf(btWordCnt & 0xFF));
				Message msg = null;
				try {
					if (mTagEpcListSpinner.getSelectedItem().toString().equalsIgnoreCase("cancel")) {
						msg = mUSeries.writeTagMemory(mTagEpcListSpinner.getSelectedItem().toString().getBytes(), btMemBank, btWordCnt, btWordAdd, btAryData, btAryPassWord);
					} else {
						String[] result1 = StringTool.stringToStringArray(mTagEpcListSpinner.getSelectedItem().toString(), 2);
						byte[] epcSelected = StringTool.stringArrayToByteArray(result1, result1.length);
						msg = mUSeries.writeTagMemory(epcSelected, btMemBank, btWordCnt, btWordAdd, btAryData, btAryPassWord);
					}
					if (msg.getCode() == 0) {
						MusicPlayer.getInstance().play(Type.OK);
						Toast.makeText(mContext, getResources().getString(R.string.Write_Tag_Successful), Toast.LENGTH_SHORT).show();
					} else {
						MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
						Toast.makeText(mContext, msg.getMessage(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// Toast.makeText(mContext, "操作失败",
					// Toast.LENGTH_SHORT).show();
					Toast.makeText(mContext, getResources().getString(R.string.Write_Tag_Failure), Toast.LENGTH_SHORT).show();
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
					Loger.disk_log("Read", "writeTagMemoryException,info = " + e.toString(), "M10_U8");
				}
			}
		} else if (id == R.id.lock) {
			byte btMemBank = 0x00;
			byte[] btAryPassWord = null;
			Enum operation = null;
			if (mGroupLockAreaType.getCheckedRadioButtonId() == R.id.set_lock_area_access_password) {
				btMemBank = 0x04;
			} else if (mGroupLockAreaType.getCheckedRadioButtonId() == R.id.set_lock_area_kill_password) {
				btMemBank = 0x05;
			} else if (mGroupLockAreaType.getCheckedRadioButtonId() == R.id.set_lock_area_epc) {
				btMemBank = 0x03;
			} else if (mGroupLockAreaType.getCheckedRadioButtonId() == R.id.set_lock_area_tid) {
				btMemBank = 0x02;
			} else if (mGroupLockAreaType.getCheckedRadioButtonId() == R.id.set_lock_area_user) {
				btMemBank = 0x01;
			}
			if (mGroupLockType.getCheckedRadioButtonId() == R.id.set_lock_free) {
				operation = U8Series.lockOperation.LOCK_FREE;
				i=0;
			} else if (mGroupLockType.getCheckedRadioButtonId() == R.id.set_lock_free_ever) {
				operation = U8Series.lockOperation.LOCK_FREE_EVER;
				i=1;
			} else if (mGroupLockType.getCheckedRadioButtonId() == R.id.set_lock_lock) {
				operation = U8Series.lockOperation.LOCK_LOCK;
				i=2;
			} else if (mGroupLockType.getCheckedRadioButtonId() == R.id.set_lock_lock_ever) {
				operation = U8Series.lockOperation.LOCK_LOCK_EVER;
				i=3;
			}
			try {
				btAryPassWord = Tools.stringToByteArray(mLockPasswordEditText.getText().toString().toUpperCase(Locale.ENGLISH));
				if(btAryPassWord.length!=4){
					throw new Exception("password length not right");
				}
//				String[] reslut = StringTool.stringToStringArray(mLockPasswordEditText.getText().toString().toUpperCase(Locale.ENGLISH), 2);
//				if(reslut.length!=4)
//					throw new Exception("password length not right");
//				btAryPassWord = StringTool.stringArrayToByteArray(reslut, 4);
			} catch (Exception e) {
				MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				Toast.makeText(mContext, getResources().getString(R.string.param_lockpassword_error), Toast.LENGTH_SHORT).show();
				return;
			}
			if (btAryPassWord == null || btAryPassWord.length != 4) {
				MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				Toast.makeText(mContext, getResources().getString(R.string.param_lockpassword_error), Toast.LENGTH_SHORT).show();
				return;
			}

			Message msg = null;
			try {
				if (mTagEpcListSpinner.getSelectedItem().toString().equalsIgnoreCase("cancel")) {
					msg = mUSeries.lockTagMemory(mTagEpcListSpinner.getSelectedItem().toString().getBytes(), btMemBank, operation, btAryPassWord);
				} else {
					String[] result1 = StringTool.stringToStringArray(mTagEpcListSpinner.getSelectedItem().toString(), 2);
					byte[] epcSelected = StringTool.stringArrayToByteArray(result1, result1.length);
					msg = mUSeries.lockTagMemory(epcSelected, btMemBank, operation, btAryPassWord);
				}
				if (msg.getCode() == 0) {
					MusicPlayer.getInstance().play(Type.OK);
					switch (i) {
					case 0:
						Toast.makeText(mContext, getResources().getString(R.string.unLock_Tag_Successful), Toast.LENGTH_SHORT).show();						
						break;

					case 2:
						Toast.makeText(mContext, getResources().getString(R.string.Lock_Tag_Successful), Toast.LENGTH_SHORT).show();
						break;
						
					case 1:
						Toast.makeText(mContext, getResources().getString(R.string.ever_unLock_Tag_Successful), Toast.LENGTH_SHORT).show();
						break;
						
					case 3:
						Toast.makeText(mContext, getResources().getString(R.string.ever_Lock_Tag_Successful), Toast.LENGTH_SHORT).show();
						break;
					}
				} else {
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
					switch (i) {
					case 0:
						Toast.makeText(mContext, getResources().getString(R.string.unLock_Tag_Failure), Toast.LENGTH_SHORT).show();						
						break;

					case 2:
						Toast.makeText(mContext, getResources().getString(R.string.Lock_Tag_Failure), Toast.LENGTH_SHORT).show();
						break;
						
					case 1:
						Toast.makeText(mContext, getResources().getString(R.string.ever_unLock_Tag_Failure), Toast.LENGTH_SHORT).show();
						break;
						
					case 3:
						Toast.makeText(mContext, getResources().getString(R.string.ever_Lock_Tag_Failure), Toast.LENGTH_SHORT).show();
						break;
					}
				}

			} catch (Exception e) {
				Toast.makeText(mContext, getResources().getString(R.string.Lock_Tag_Failure), Toast.LENGTH_SHORT).show();
				MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				Loger.disk_log("Read", "lockTagMemoryException,info = " + e.toString(), "M10_U8");
				e.printStackTrace();
			}
		} else if (id == R.id.kill) {
			byte[] btAryPassWord = null;
			try {
//				String[] reslut = StringTool.stringToStringArray(mKillPasswordEditText.getText().toString().toUpperCase(Locale.ENGLISH), 2);
//				btAryPassWord = StringTool.stringArrayToByteArray(reslut, 4);
				btAryPassWord = Tools.stringToByteArray(mKillPasswordEditText.getText().toString().toUpperCase(Locale.ENGLISH));
				if(btAryPassWord.length!=4){
					throw new Exception("password length not right");
				}
			} catch (Exception e) {
				MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				Toast.makeText(mContext, getResources().getString(R.string.param_killpassword_error), Toast.LENGTH_SHORT).show();
				return;
			}
			if (btAryPassWord == null || btAryPassWord.length != 4) {
				MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				Toast.makeText(mContext, getResources().getString(R.string.param_killpassword_error), Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				Message msg = null;
				if (mTagEpcListSpinner.getSelectedItem().toString().equalsIgnoreCase("cancel")) {
					mUSeries.killTag(mTagEpcListSpinner.getSelectedItem().toString().getBytes(), btAryPassWord);
				} else {
					String[] result1 = StringTool.stringToStringArray(mTagEpcListSpinner.getSelectedItem().toString(), 2);
					byte[] epcSelected = StringTool.stringArrayToByteArray(result1, result1.length);
					msg = mUSeries.killTag(epcSelected, btAryPassWord);
				}
				if (msg.getCode() == 0) {
					MusicPlayer.getInstance().play(Type.OK);
					Toast.makeText(mContext, getResources().getString(R.string.Kill_Tag_Successful), Toast.LENGTH_SHORT).show();
				} else {
					MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
					Toast.makeText(mContext, getResources().getString(R.string.Kill_Tag_Failure), Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				Toast.makeText(mContext, getResources().getString(R.string.Kill_Tag_Failure), Toast.LENGTH_SHORT).show();
				MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				Loger.disk_log("Read", "killTagMemoryException,info = " + e.toString(), "M10_U8");
				e.printStackTrace();

			}
		}
	}

}
