package us.dontcareabout.CheckFlow.client.component;

import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.gxt.client.draw.component.TextButton;

public 	class ToolItem extends TextButton {
	public ToolItem(String str) {
		super(str);
		setBgColor(RGB.LIGHTGRAY);
		setBgRadius(20);
		setMargin(20);
	}
}