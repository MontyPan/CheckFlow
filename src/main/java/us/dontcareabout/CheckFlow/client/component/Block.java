package us.dontcareabout.CheckFlow.client.component;

import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;

import us.dontcareabout.CheckFlow.client.Formate;
import us.dontcareabout.CheckFlow.client.Palette;
import us.dontcareabout.gxt.client.draw.LRectangleSprite;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;

class Block extends LayerSprite {
	private static final int HEIGHT = 140;
	private static final int NUMBER_SIZE = 60;
	private static final int FOOTER_SIZE = 18;

	LTextSprite text = new LTextSprite();
	LTextSprite footer = new LTextSprite();
	LRectangleSprite progress = new LRectangleSprite();

	Block(String footerString) {
		resize(NUMBER_SIZE * 1.5 + 30, HEIGHT - 10);
		setLY(5);

		progress.setWidth(getWidth());
		progress.setFill(Palette.BLUE);
		add(progress);

		text.setTextBaseline(TextBaseline.MIDDLE);
		text.setFontSize(NUMBER_SIZE);
		text.setLY(50);
		add(text);

		footer.setText(footerString);
		footer.setTextBaseline(TextBaseline.MIDDLE);
		footer.setFontSize(FOOTER_SIZE);
		footer.setLX(getWidth() - FOOTER_SIZE - 20);
		footer.setLY(105);
		add(footer);
	}

	void setNumber(double value) {
		boolean isNaN = Double.isNaN(value);
		text.setText(isNaN ? "？" : Formate.NUMBER.format(value));
		double textWidth = isNaN ? NUMBER_SIZE :
			text.getText().length() * NUMBER_SIZE / 2;
		text.setLX(getWidth() - textWidth - 20);
	}

	void setProgress(double value) {
		setNumber(value * 100);
		progress.setHeight(getHeight() * value);
		progress.setLY(getHeight() - progress.getHeight());
	}
}