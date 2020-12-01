package com.youtube.hempfest.warps.system;

import com.youtube.hempfest.hempcore.formatting.string.RandomID;

public class WID {


	private String wID;

	private WID() {
	}

	private WID(String wID) {
		this.wID = wID;
	}

	public String toString() {
		return wID;
	}

	private void setId() {
		this.wID = new RandomID(6).generate();
	}

	private static WID generate() {
		WID result = new WID();
		result.setId();
		return result;
	}

	public static WID randomID() {
		return generate();
	}

	public static WID fromString(String wID) {
		return new WID(wID);
	}





}
