package us.dontcareabout.CheckFlow.client.component;

import java.util.Date;

import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;

import us.dontcareabout.CheckFlow.client.DateUtil;
import us.dontcareabout.CheckFlow.client.Palette;
import us.dontcareabout.CheckFlow.client.ui.UiCenter;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.gxt.client.draw.LRectangleSprite;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;

public class CheckListInfo extends LayerSprite {
	public static final int HEIGHT = 140;

	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getFormat("###");
	private static final int NAME_SIZE = 40;
	private static final int NUMBER_SIZE = 60;
	private static final int FOOTER_SIZE = 18;

	private LTextSprite name = new LTextSprite();
	private LTextSprite nowCheckPoint = new LTextSprite();
	private Block reciprocal = new Block("天");
	private Block progress = new Block("%");

	private CheckFlow checkFlow;

	public CheckListInfo() {
		name.setTextBaseline(TextBaseline.MIDDLE);
		name.setFontSize(NAME_SIZE);
		name.setLX(20);
		name.setLY(45);
		add(name);

		nowCheckPoint.setTextBaseline(TextBaseline.MIDDLE);
		nowCheckPoint.setFontSize(FOOTER_SIZE);
		nowCheckPoint.setLX(40);
		nowCheckPoint.setLY(95);
		add(nowCheckPoint);

		add(progress);
		add(reciprocal);

		addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				UiCenter.checkListMonitor(checkFlow);
			}
		});
	}

	public void setData(CheckFlow cf) {
		checkFlow = cf;
		name.setText(cf.getName());

		if (checkFlow.getUnfinishPoint() != null) {
			nowCheckPoint.setText("→ " + checkFlow.getUnfinishPoint().getName());
		}

		drawProgress();
		drawReciprocal();
		adjustMember();
	}

	@Override
	protected void adjustMember() {
		//目前不考慮高度變化
		//TODO 寬度過小時 name 與 nowCheckPoint 要縮減字

		progress.setLX(getWidth() - reciprocal.getWidth() - progress.getWidth() - 10);
		reciprocal.setLX(getWidth() - reciprocal.getWidth() - 5);
	}

	private void drawProgress() {
		progress.setProgress(checkFlow.getProgress());
	}

	private void drawReciprocal() {
		final int duration = 7;

		if (checkFlow.getDeadline() == null) {
			reciprocal.setNumber(Double.NaN);
			reciprocal.text.setFill(RGB.BLACK);
			reciprocal.setBgColor(RGB.WHITE);
			return;
		}

		int diff = DateUtil.daysBetween(checkFlow.getDeadline(), new Date());

		reciprocal.setNumber(Math.abs(diff));
		Color textColor = RGB.BLACK;

		if (diff >= duration) {
			reciprocal.setBgColor(RGB.WHITE);
		} else if (diff >= 0) {
			int value = (int)((3.0 * diff / duration) % 3);
			reciprocal.setBgColor(Palette.RED[value]);
			if (value < 2) { textColor = RGB.WHITE; }
		} else {
			reciprocal.setBgColor(Palette.BLACK);
			textColor = RGB.WHITE;
		}

		reciprocal.text.setFill(textColor);
		reciprocal.footer.setFill(textColor);
	}

	private class Block extends LayerSprite {
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
			text.setText(isNaN ? "？" : NUMBER_FORMAT.format(value));
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
}
