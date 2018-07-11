package us.dontcareabout.CheckFlow.client.component;

import java.util.Date;

import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;

import us.dontcareabout.CheckFlow.client.DateUtil;
import us.dontcareabout.CheckFlow.client.Palette;
import us.dontcareabout.CheckFlow.client.ui.UiCenter;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;

public class CheckListInfo1 extends LayerSprite {
	private LTextSprite name = new LTextSprite();
	private LTextSprite nowCheckPoint = new LTextSprite();
	private Block reciprocal = new Block("天");
	private Block progress = new Block("%");

	private CheckFlow checkFlow;

	public CheckListInfo1() {
		name.setTextBaseline(TextBaseline.MIDDLE);
		name.setFontSize(40);
		name.setLX(20);
		name.setLY(45);
		add(name);

		nowCheckPoint.setTextBaseline(TextBaseline.MIDDLE);
		nowCheckPoint.setFontSize(18);
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
}
