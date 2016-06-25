package com.ailk.main.cache;

import com.ailk.jdbc.CacheLoader;
import com.ailk.jdbc.GlobalManager;

public class LoadCache {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: java LoadCache [function]");
			return;
		}

		if (args[0].equalsIgnoreCase("inMemLoad")) {
			GlobalManager globalManager = new GlobalManager();

			CacheLoader.inMemLoad(globalManager);
			GlobalManager.setInstance(globalManager);
		} else if (args[0].equalsIgnoreCase("alterTable")) {
			CacheLoader.alterTable();
		} else {
			System.out.println("Usage: java LoadCache [function]");
		}
	}

}
