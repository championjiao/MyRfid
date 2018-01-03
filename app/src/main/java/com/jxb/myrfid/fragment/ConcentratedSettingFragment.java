package com.jxb.myrfid.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jxb.myrfid.R;
import com.jxb.myrfid.TextMoveLayout;
import com.jxb.myrfid.UHFApplication;
import com.jxb.myrfid.series.operation.U8Series;
import com.jxb.myrfid.series.reader.ERROR;
import com.jxb.myrfid.series.utils.MusicPlayer;
import com.jxb.myrfid.series.utils.MusicPlayer.Type;

import com.fntech.Loger;

@SuppressLint("ValidFragment")
public class ConcentratedSettingFragment extends Fragment implements OnClickListener, OnCheckedChangeListener {
	/**
	 * 屏幕宽度
	 */
	private int screenWidth;

	private ViewGroup.LayoutParams layoutParams;
	/**
	 * 自定义随着拖动条一起移动的控件
	 */
	private TextMoveLayout mOutPowerTextLayout;
	/**
	 * 随着进度条移动的功率
	 */
	private TextView moveTextDBM;

	private SeekBar mOutPowerSeekBar;
	// 温度
	private EditText temperatureText;
	private TextView bt_getTemperature;
	// 提示音
	private RadioGroup mGroupBeeper;
	private Switch mSoftSoundSwitch;
	// session&flag
	private TextView setSession = null;
	private Spinner spin_Session = null;
	private Spinner spin_Flag = null;

	private TextView tv_Set;
	private U8Series mUSeries;
	private Context mContext;

	public ConcentratedSettingFragment(Context context) {
		mContext = context;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

		screenWidth = wm.getDefaultDisplay().getWidth();

		moveTextDBM = new TextView(mContext);
		moveTextDBM.setBackgroundColor(Color.rgb(245, 245, 245));
		moveTextDBM.setTextColor(Color.rgb(0, 161, 229));
		moveTextDBM.setTextSize(16);
		layoutParams = new ViewGroup.LayoutParams(screenWidth, 40);

		moveTextDBM.layout(0, 20, screenWidth, 80);
		mUSeries = U8Series.getInstance();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_setting_all, container, false);
		mOutPowerTextLayout = (TextMoveLayout) layout.findViewById(R.id.out_power_textLayout);
		mOutPowerSeekBar = (SeekBar) layout.findViewById(R.id.out_power_seekBar);
		mOutPowerSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImp());
		mOutPowerSeekBar.setMax(33);

		// 获取模块温度
		bt_getTemperature = (TextView) layout.findViewById(R.id.get);
		bt_getTemperature.setOnClickListener(this);
		temperatureText = (EditText) layout.findViewById(R.id.temperature_text);

		// 提示音设置
		mGroupBeeper = (RadioGroup) layout.findViewById(R.id.group_beeper);
		mSoftSoundSwitch = (Switch) layout.findViewById(R.id.soft_sound_switch);
		mSoftSoundSwitch.setOnCheckedChangeListener(this);
		// session&flag
		setSession = (TextView) layout.findViewById(R.id.set);
		spin_Session = (Spinner) layout.findViewById(R.id.spinner_Session);
		spin_Flag = (Spinner) layout.findViewById(R.id.spinner_Flag);

		tv_Set = (TextView) layout.findViewById(R.id.set);
		tv_Set.setOnClickListener(this);
		return layout;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("toolsdebug", "---- onResume ---");
		mOutPowerTextLayout.addView(moveTextDBM, layoutParams);
	}

	public void init() {
		getSoftSound();
		getSession();

		getPower();

	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onPause() {
		super.onPause();
		mOutPowerTextLayout.removeAllViews();
	}

	private void getSession() {
		// 显示session&flag
		String[] mItems = { "s0", "s1", "s2", "s3" };
		String[] mItemsFlag = { "A", "B" };
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(mContext, R.layout.item_simple_spinner, mItems);
		spin_Session.setAdapter(_Adapter);

		spin_Session.setSelection(Integer.parseInt(mUSeries.getParams(U8Series.SESSIONSTATE)));
		_Adapter = new ArrayAdapter<String>(mContext, R.layout.item_simple_spinner, mItemsFlag);

		spin_Flag.setAdapter(_Adapter);
		spin_Flag.setSelection(Integer.parseInt(mUSeries.getParams(U8Series.FLAGSTATE)));
	}

	private void getSoftSound() {
		mSoftSoundSwitch.setChecked(UHFApplication.getSoftSound() == 1 ? true : false);
		Log.i("toolsdebug", " UHFApplication.getSoftSound() == " + UHFApplication.getSoftSound());
	}

	private void getPower() {
		try {
			// 显示功率
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String outPowerSetParams = mUSeries.getParams(U8Series.PARA_POWER);
			Loger.disk_log("getPower", outPowerSetParams, "U8");
			if (outPowerSetParams.trim().equalsIgnoreCase(ERROR.RECEVICE_INCOMPLETE)) {
				MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				Toast.makeText(mContext, getResources().getString(R.string.str_getting_out_power_fail) + " " + outPowerSetParams, Toast.LENGTH_SHORT).show();
				return;
			}

			if (outPowerSetParams != null) {
				refreshPowerView(outPowerSetParams);
			}
			// 显示软件提示音
		} catch (Exception e) {
			Loger.disk_log("Exception", "getPowerParamsException" + e.toString(), "M10_U8");
		}
	}

	private class OnSeekBarChangeListenerImp implements SeekBar.OnSeekBarChangeListener {

		// 触发操作，拖动
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			float value = ((float) screenWidth / (float) 30) * (float) 0.8;
			int moveStep = Math.round(value);
			moveTextDBM.setWidth(80);
			moveTextDBM.layout((int) (progress * moveStep), 20, screenWidth, 80);
			moveTextDBM.setText(progress + "dBm");
		}

		// 表示进度条刚开始拖动，开始拖动时候触发的操作
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		// 停止拖动时候
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	}

	private void refreshPowerView(String powerValue) {
		moveTextDBM.setText(powerValue + "dBm");
		mOutPowerSeekBar.setProgress(Integer.parseInt(powerValue));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.get:
				getTemperature();
				break;
			case R.id.set:
				if (setSessionAndFlag() && setSoftSound(mSoftSoundSwitch.isChecked()) && setPower()) {
					MusicPlayer.getInstance().play(Type.OK);
					Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(mContext, "设置失败", Toast.LENGTH_SHORT).show();
				MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				break;
			default:
				break;
		}
	}

	private void getTemperature() {
		temperatureText.setText("");
		try {
			String strTemperature = mUSeries.getParams(U8Series.TEMPERATURE);
			if (strTemperature.trim().equalsIgnoreCase(ERROR.RECEVICE_INCOMPLETE)) {// 接收异常
				MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
				Toast.makeText(mContext, ERROR.RECEVICE_INCOMPLETE, Toast.LENGTH_SHORT).show();
				return;
			}
			MusicPlayer.getInstance().play(Type.OK);
			temperatureText.setText(strTemperature);
		} catch (Exception e) {
			MusicPlayer.getInstance().play(Type.MUSIC_ERROR);
			Loger.disk_log("Exception", "getTemperatureParamsException" + e.toString(), "M10_U8");
			e.printStackTrace();
		}
	}

	private boolean setPower() {
		byte btOutputPower = 0x00;
		try {
			String powerStr = moveTextDBM.getText().toString();
			btOutputPower = (byte) Integer.parseInt(powerStr.subSequence(0, powerStr.indexOf("dBm")).toString());
		} catch (Exception e) {
			return false;
		}
		try {
			boolean setResult = mUSeries.setParams(U8Series.PARA_POWER, Byte.toString(btOutputPower));
			if (setResult) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			Loger.disk_log("Exception", "setPowerParamsException" + e.toString(), "M10_U8");
			return false;
		}

	}

	/**
	 * 设置蜂鸣器和软件提示音
	 */
	private boolean setSoftSound(boolean isOpen) {

		/*
		 * byte btMode = 0; int checkedRadioButtonId =
		 * mGroupBeeper.getCheckedRadioButtonId(); if (checkedRadioButtonId ==
		 * R.id.set_beeper_quiet) { btMode = 0; } else if (checkedRadioButtonId
		 * == R.id.set_beeper_all) { btMode = 1; } else if (checkedRadioButtonId
		 * == R.id.set_beeper_one) { btMode = 2; }
		 * mUSeries.setParams(U8Series.BEER_STATE, Byte.toString(btMode)) &&
		 */

		byte soundMode = (byte) (isOpen ? 1 : 0);
		try {
			if (mUSeries.setParams(U8Series.SOFT_SOUND, Byte.toString(soundMode))) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	private boolean setSessionAndFlag() {
		int Sessionindex = spin_Session.getSelectedItemPosition();
		int Flagindex = spin_Flag.getSelectedItemPosition();
		boolean setSessionStateResult = mUSeries.setParams(U8Series.SESSIONSTATE, Byte.toString((byte) Sessionindex));
		boolean setFlagStateResult = mUSeries.setParams(U8Series.FLAGSTATE, Byte.toString((byte) Flagindex));
		if (setSessionStateResult && setFlagStateResult) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
			case R.id.soft_sound_switch:

				if (isResumed()) {
					if (isChecked) {
						if (setSoftSound(isChecked)) {
							// Toast.makeText(mContext,
							// getString(R.string.open_soft_sound),
							// Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(mContext, getString(R.string.setting_soft_sound_failed), Toast.LENGTH_SHORT).show();
							mSoftSoundSwitch.setChecked(!isChecked);
						}
					} else {
						if (setSoftSound(isChecked)) {
							// Toast.makeText(mContext,
							// getString(R.string.close_soft_sound),
							// Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(mContext, getString(R.string.setting_soft_sound_failed), Toast.LENGTH_SHORT).show();
							mSoftSoundSwitch.setChecked(!isChecked);
						}
					}
				}
				break;

			default:
				break;
		}
	}
}

