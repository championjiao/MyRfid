package cn.fuen.xmldemo.model;

import java.io.FileOutputStream;

public class M10A_GPIOUtil {
	
	public static void gpio898_1() {
		writeFile("/sys/class/gpio/gpio898/direction", "out");
		writeFile("/sys/class/gpio/gpio898/value", "1");
	}

	public static void gpio898_0() {
		writeFile("/sys/class/gpio/gpio898/direction", "out");
		writeFile("/sys/class/gpio/gpio898/value", "0");
	}

	public static void gpio899_1() {
		writeFile("/sys/class/gpio/gpio899/direction", "out");
		writeFile("/sys/class/gpio/gpio899/value", "1");
	}

	public static void gpio899_0() {
		writeFile("/sys/class/gpio/gpio899/direction", "out");
		writeFile("/sys/class/gpio/gpio899/value", "0");
	}

	public static void gpio909_1() {

		writeFile("/sys/class/gpio/gpio909/direction", "out");
		writeFile("/sys/class/gpio/gpio909/value", "1");

	}

	public static void gpio909_0() {

		writeFile("/sys/class/gpio/gpio909/direction", "out");
		writeFile("/sys/class/gpio/gpio909/value", "0");

	}

	public static void gpio910_1() {
		writeFile("/sys/class/gpio/gpio910/direction", "out");
		writeFile("/sys/class/gpio/gpio910/value", "1");
	}

	public static void gpio910_0() {
		writeFile("/sys/class/gpio/gpio910/direction", "out");
		writeFile("/sys/class/gpio/gpio910/value", "0");
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
