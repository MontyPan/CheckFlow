package us.dontcareabout.CheckFlow.client.component;

import java.util.ArrayList;

import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;

public class Toolbar extends LayerContainer {
	private ArrayList<LayerSprite> items = new ArrayList<>();
	private int margin = 8;

	@Override
	protected void onResize(int width, int height) {
		//Refactory 抽去 GF：HorizontalLayer
		double wUnit = width / items.size() - margin * 2;
		int index = 0;

		for (LayerSprite btn : items) {
			btn.resize(wUnit, height);
			btn.setLX(margin + index * (wUnit + margin * 2));
			index++;
		}

		super.onResize(width, height);
	}

	public void add(LayerSprite item) {
		addLayer(item);
		items.add(item);
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}
}
