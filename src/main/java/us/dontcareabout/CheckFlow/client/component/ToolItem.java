package us.dontcareabout.CheckFlow.client.component;

import com.sencha.gxt.chart.client.draw.RGB;

public 	class ToolItem extends TextButton {
	public ToolItem(String str) {
		super(str);
		setBgColor(RGB.LIGHTGRAY);
		setBgRadius(20);
		setMargin(20);
	}
}