package cn.fuen.xmldemo.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import cn.fuen.xmldemo.R;
import cn.fuen.xmldemo.activity.SetAndSaveActivity;
import cn.fuen.xmldemo.entity.Device;

public class DeviceModel implements DeviceModelImp {

	private final String path = Environment.getExternalStorageDirectory() + "/state.xml";
	private final String TAG = "DeviceModel";
	private List<Device> mReadList = new ArrayList<>();
	private Context mContext;
	private String[] names = null;
	private Map<String, String> mPowerOnList = new HashMap<>();
	private Map<String, String> mPowerOffList = new HashMap<>();

	public DeviceModel(Context context) {
		this.mContext = context;
		String SystemRelease = android.os.Build.VERSION.RELEASE;
		if (SystemRelease.equals("4.0.3")) {
			names = mContext.getResources().getStringArray(R.array.m10_GPIO);

		} else if (SystemRelease.equals("5.1.1")) {
			if (mContext.getResources().getDisplayMetrics().heightPixels == SetAndSaveActivity.M96DisplayHeight) {
				names = mContext.getResources().getStringArray(R.array.m96_GPIO);

			} else {
				names = mContext.getResources().getStringArray(R.array.m10A_GPIO);
			}

		}
	}

	@Override
	public void writeXML(List<Device> devices, int gpioLength) {
		ArrayList<String> gpioNameList = new ArrayList<>();

		for (int i = 0; i < names.length; i++) {
			gpioNameList.add(names[i]);
		}
		XmlSerializer xmlSerializer = Xml.newSerializer();
		try {

			xmlSerializer.setOutput(new FileOutputStream(path, false), "utf-8");
			xmlSerializer.startDocument("utf-8", true);
			xmlSerializer.startTag(null, "devices");

			/**
			 * model
			 */
			for (int i = 0; i < devices.size(); i++) {
				Device device = devices.get(i);

				xmlSerializer.startTag(null, "device");

				xmlSerializer.startTag(null, "model");
				xmlSerializer.text(device.getModel());
				xmlSerializer.endTag(null, "model");

				xmlSerializer.startTag(null, "enable");
				xmlSerializer.text(Boolean.toString(device.isEnable()));
				xmlSerializer.endTag(null, "enable");

				xmlSerializer.startTag(null, "serialPort");
				xmlSerializer.text(device.getSerialPort());
				xmlSerializer.endTag(null, "serialPort");

				xmlSerializer.startTag(null, "baudRate");
				xmlSerializer.text(Integer.toString(device.getBaudRate()));
				xmlSerializer.endTag(null, "baudRate");

				List<String> gpioList = device.getGpios();
				
				xmlSerializer.startTag(null, "poweron");

				for (int j = 0; j < gpioLength; j++) {
					xmlSerializer.startTag(null, gpioNameList.get(j));
					xmlSerializer.text(gpioList.get(j));// item
					xmlSerializer.endTag(null, gpioNameList.get(j));
				}
				xmlSerializer.endTag(null, "poweron");

				xmlSerializer.startTag(null, "poweroff");

				for (int j = gpioLength; j < gpioLength * 2; j++) {
					xmlSerializer.startTag(null, gpioNameList.get(j - gpioLength));
					xmlSerializer.text(gpioList.get(j));// item
					xmlSerializer.endTag(null, gpioNameList.get(j - gpioLength));
				}
				xmlSerializer.endTag(null, "poweroff");

				xmlSerializer.endTag(null, "device");
			}

			xmlSerializer.endTag(null, "devices");

			xmlSerializer.endDocument();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Device device = null;

	@Override
	public List<Device> readXML() {
		mReadList.clear();
		File file = new File(path);
		FileInputStream fis = null;
		Object putObject = null;
		try {
			fis = new FileInputStream(file);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(fis, "utf-8");

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {

				} else if (eventType == XmlPullParser.START_TAG) {
					String startTagName = xpp.getName();
					if (startTagName.equals("devices")) {
					} else if (startTagName.equals("device")) {
						device = new Device();
					} else if (startTagName.equals("model")) {
						String modelNameString = xpp.nextText();
//						Log.e("see", "        modelNameString=    " + modelNameString);
						device.setModel(modelNameString);
					} else if (startTagName.equals("enable")) {
						device.setEnable(Boolean.parseBoolean(xpp.nextText()));
					} else if (startTagName.equals("serialPort")) {
						device.setSerialPort(xpp.nextText());
					} else if (startTagName.equals("baudRate")) {
						device.setBaudRate(Integer.parseInt(xpp.nextText()));
					} else if (startTagName.equals("poweron")) {
						mPowerOnList = new HashMap<>();
						putObject = mPowerOnList;
					} else if (startTagName.equals("poweroff")) {
						mPowerOffList = new HashMap<>();
						putObject = mPowerOffList;
					} else {
						Map<String, String> currentPutMap = (Map<String, String>) putObject;
						String gpioValue = xpp.nextText();
						currentPutMap.put(startTagName, gpioValue);
//						Log.i("see", "  startTagName = " + startTagName + "  gpioValue =   " + gpioValue);
						/*mGpioSize++;
						if (mGpioSize <= gpioLength) {
							String gpioValue = xpp.nextText();
							Log.i("see", "  startTagName = " + startTagName + "  gpioValue =   " + gpioValue);
							mPowerOnList.put(startTagName, gpioValue);
						} else if (mGpioSize < gpioLength * 2 && mGpioSize > gpioLength) {
							mPowerOffList.put(startTagName, xpp.nextText());
						} else if (mGpioSize == gpioLength * 2) {
							mPowerOffList.put(startTagName, xpp.nextText());
							mGpioSize = 0;
						}*/
					}

				} else if (eventType == XmlPullParser.END_TAG) {
					String endTagName = xpp.getName();
					if (endTagName.equals("poweron")) {
						device.setPowerOnList(this.mPowerOnList);
					} else if (endTagName.equals("poweroff")) {
						device.setPowerOffList(this.mPowerOffList);
					} else if (endTagName.equals("device")) {
						mReadList.add(device);
					} else if (endTagName.equals("devices")) {
						mPowerOnList = null;
						mPowerOffList = null;
						mContext = null;
						break;
					}
				}
				eventType = xpp.next();

			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("toolsdebug", " XmlPullParserExcrption "+e.toString());
			return null;
		} finally {
			try {
				if (file != null)
					fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mReadList;
	}

	@Override
	public Device getDeviceFromModel(String model) {
		/*int gpioLength = 0;
		String SystemRelease = android.os.Build.VERSION.RELEASE;
		if (SystemRelease.equals(SetAndSaveActivity._4_0_3)) {
			String[] M10_GPIO = mContext.getResources().getStringArray(R.array.m10_GPIO);
			gpioLength = M10_GPIO.length;
		} else if (SystemRelease.equals(SetAndSaveActivity._5_1_1)) {
			if (mContext.getResources().getDisplayMetrics().heightPixels == SetAndSaveActivity.M96DisplayHeight) {
				String[] M96_GPIO = mContext.getResources().getStringArray(R.array.m96_GPIO);
				gpioLength = M96_GPIO.length;

			} else {
				String[] M10A_GPIO = mContext.getResources().getStringArray(R.array.m10A_GPIO);
				gpioLength = M10A_GPIO.length;

			}
		}*/
		if (readXML() == null) {
			return null;
		}
		Device device = null;
		for (Device d : mReadList) {
			if (d.getModel().equals(model)) {
				device = d;
			}
		}
		if (device != null) {
			if (!device.isEnable())
				return null;
		}
		// 加入enable节点,若不勾选此选项设置无效

		return device;
	}

	@Override
	public Device reLoadDevice(String model) {

		Device device = null;
		for (Device d : mReadList) {
			if (d.getModel().equals(model)) {
				device = d;
			}
		}
		return device;
	}

}
