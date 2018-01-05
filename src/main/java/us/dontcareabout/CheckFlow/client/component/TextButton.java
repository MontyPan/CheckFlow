package us.dontcareabout.CheckFlow.client.component;

import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.util.PreciseRectangle;

import us.dontcareabout.gxt.client.draw.Cursor;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;

public class TextButton extends LayerSprite {
	private LTextSprite textSprite = new LTextSprite();
	private double margin;

	public TextButton() {
		add(textSprite);
		setMemberCursor(Cursor.POINTER);
	}

	public TextButton(String text) {
		this();
		setText(text);
	}

	public void setText(String text) {
		textSprite.setText(text);
	}

	public void setTextColor(Color color) {
		textSprite.setFill(color);
	}

	public void setMargin(double margin) {
		this.margin = margin;
	}

	@Override
	protected void adjustMember() {
		resizeText(textSprite, getWidth() - margin * 2, getHeight() - margin * 2);

		PreciseRectangle textBox = textSprite.getBBox();
		textSprite.setLX((getWidth() - textBox.getWidth()) / 2.0);
		textSprite.setLY((getHeight() - textBox.getHeight()) / 2.0 - textSprite.getFontSize() * fontOffsetY(textSprite.getFont()));
	}

	//Refactory 搬去 GF =============================>
	private static double fontOffsetY(String fontName) {
		if (fontName == null) { return 0.1; }

		return 0;
	}

	/**
	 * 在指定的大小中找尋最適當的字體大小
	 */
	private static void resizeText(TextSprite text, double w, double h) {
		PreciseRectangle box = text.getBBox();
		boolean flag = false;

		//先判斷是不是爆框，如果爆框就只有縮小一途
		while (box.getWidth() > w || box.getHeight() > h) {
			flag = true;

			//不能無止境的小下去...
			if (text.getFontSize() < 11) { break; }

			text.setFontSize(text.getFontSize() - 1);
			text.redraw();
			box = text.getBBox();
		}

		if (flag) { return; }

		//原本沒有爆框，試著變大直到爆框
		while (box.getWidth() < w && box.getHeight() < h) {
			text.setFontSize(text.getFontSize() + 1);
			text.redraw();
			box = text.getBBox();
		}

		//往回退一號
		text.setFontSize(text.getFontSize() - 1);
		text.redraw();
	}
}
