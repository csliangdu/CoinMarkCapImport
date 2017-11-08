package com.zdx.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

public class LoadConfig {

	private static HashMap<Object, Object> LoadProperty(String prop) {
		HashMap<Object, Object> ret = null;
		Properties properties = new Properties();

		try {
			InputStream stream = new FileInputStream(prop);
			properties.load(stream);
			ret = new HashMap<Object, Object>();
			ret.putAll(properties);
		} catch (FileNotFoundException e) {
			System.out.println("No such file " + prop);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private static HashMap<Object, Object> LoadYaml(String confPath) {
		HashMap<Object, Object> ret = null;
		Yaml yaml = new Yaml();
		try {
			InputStream stream = new FileInputStream(confPath);

			ret = (HashMap<Object, Object>) yaml.load(stream);
			if (ret == null || ret.isEmpty() == true) {
				throw new RuntimeException("Failed to read config file");
			}

		} catch (FileNotFoundException e) {
			System.out.println("No such file " + confPath);
			throw new RuntimeException("No config file");
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new RuntimeException("Failed to read config file");
		}

		return ret;
	}

	public static HashMap<Object, Object> LoadConf(String arg) {
		HashMap<Object, Object> ret = null;

		if (arg.endsWith("yaml")) {
			ret = LoadYaml(arg);
		} else {
			ret = LoadProperty(arg);
		}
		return ret;
	}
}
