package us.dontcareabout.CheckFlow.client;

import com.google.gwt.user.client.Window;

import us.dontcareabout.CheckFlow.client.ui.UiCenter;
import us.dontcareabout.gwt.client.GFEP;
import us.dontcareabout.gwt.client.iCanUse.Feature;

public class CFEP extends GFEP {
	public CFEP() {
		needFeature(Feature.Canvas);
	}

	@Override
	protected String version() { return "0.0.1"; }

	@Override
	protected String defaultLocale() { return "zh_TW"; }


	@Override
	protected void featureFail() {
		Window.alert("這個瀏覽器我不尬意，不給用..... \\囧/");
	}

	@Override
	protected void start() {
		UiCenter.start();
	}
}