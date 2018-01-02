package com.fntech.m10a.gpio;

import java.io.FileOutputStream;

public class M10A_GPIO {

	public static void BdPowerOn() {
		
		//writeFile("/sys/class/gpio/export", "909");
		writeFile("/sys/class/gpio/gpio909/direction", "out");
		writeFile("/sys/class/gpio/gpio909/value", "1");
	}

	public static void BdPowerOff() {
		writeFile("/sys/class/gpio/gpio909/direction", "out");
		writeFile("/sys/class/gpio/gpio909/value", "0");
	}
	public static void PowerOn() {
		writeFile("/sys/class/gpio/gpio898/direction", "out");
		writeFile("/sys/class/gpio/gpio898/value", "1");
	}

	public static void PowerOff() {
		writeFile("/sys/class/gpio/gpio898/direction", "out");
		writeFile("/sys/class/gpio/gpio898/value", "0");
	}
	
	public static void _uhf_SwitchSerialPort() {
		writeFile("/sys/class/gpio/gpio910/direction", "out");
		writeFile("/sys/class/gpio/gpio910/value", "0");
	}
	
	public static void _14443_SwitchSerialPort() {
		writeFile("/sys/class/gpio/gpio908/direction", "out");
		writeFile("/sys/class/gpio/gpio908/value", "1");
	}

	public static void _gama_SwitchSerialPort() {
		writeFile("/sys/class/gpio/gpio908/direction", "out");
		writeFile("/sys/class/gpio/gpio908/value", "0");
	}
	
	private static void writeFile(String fileName, String writestr) {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = writestr.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
