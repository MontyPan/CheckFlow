package us.dontcareabout.CheckFlow.client.component;

import java.util.Date;

import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawComponent;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;

import us.dontcareabout.CheckFlow.client.DateUtil;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;

public class CheckFlowInfo extends DrawComponent {
	private static final int HEIGHT = 140;
	private static final int NAME_SIZE = 40;
	private static final int NUMBER_SIZE = 60;
	private static final int FOOTER_SIZE = 18;
	private static final Color BLACK = new Color("#1b1b1b");
	private static final Color BLUE = new Color("#58a5f0");
	private static final Color[] RED = {
		new Color("#8e0000"), new Color("#c62828"), new Color("#ff5f52")
	};

	private TextSprite name = new TextSprite();
	private TextSprite nowCheckPoint = new TextSprite();
	private TextSprite reciprocal = new TextSprite();
	private TextSprite reciprocalDay = new TextSprite("天");
	private RectangleSprite reciprocalBG = new RectangleSprite(NUMBER_SIZE * 1.5 + 30, HEIGHT - 10);
	private TextSprite progress = new TextSprite();
	private TextSprite progressRatio = new TextSprite("%");
	private RectangleSprite progressBG = new RectangleSprite(NUMBER_SIZE * 1.5 + 30, HEIGHT - 10);

	private CheckFlow checkFlow;
	private int reciprocalWidth = 0;
	private int progressWidth = 0;

	public CheckFlowInfo() {
		reciprocalBG.setZIndex(0);
		reciprocalBG.setY(5);
		addSprite(reciprocalBG);

		progressBG.setZIndex(0);
		progressBG.setFill(BLUE);
		addSprite(progressBG);

		name.setTextBaseline(TextBaseline.MIDDLE);
		name.setFontSize(NAME_SIZE);
		name.setX(20);
		name.setY(45);
		addSprite(name);

		nowCheckPoint.setTextBaseline(TextBaseline.MIDDLE);
		nowCheckPoint.setFontSize(FOOTER_SIZE);
		nowCheckPoint.setX(40);
		nowCheckPoint.setY(95);
		addSprite(nowCheckPoint);

		progress.setTextBaseline(TextBaseline.MIDDLE);
		progress.setFontSize(NUMBER_SIZE);
		progress.setY(60);
		addSprite(progress);

		progressRatio.setTextBaseline(TextBaseline.MIDDLE);
		progressRatio.setFontSize(FOOTER_SIZE);
		progressRatio.setY(115);
		addSprite(progressRatio);

		reciprocal.setTextBaseline(TextBaseline.MIDDLE);
		reciprocal.setFontSize(NUMBER_SIZE);
		reciprocal.setY(60);
		addSprite(reciprocal);

		reciprocalDay.setTextBaseline(TextBaseline.MIDDLE);
		reciprocalDay.setFontSize(FOOTER_SIZE);
		reciprocalDay.setY(115);
		addSprite(reciprocalDay);
	}

	public void setData(CheckFlow cf) {
		checkFlow = cf;
		name.setText(cf.getName());

		for (CheckPoint cp : cf.getPointList()) {
			if (!cp.isFinish()) {
				nowCheckPoint.setText("→ " + cp.getName());
				break;
			}
		}

		drawProgress();
		drawReciprocal();
		redrawSurface();
	}

	@Override
	protected void onResize(int width, int height) {
		//目前不考慮高度變化
		//TODO 寬度過小時 name 與 nowCheckPoint 要縮減字

		localProgress(width);
		progressBG.setX(width - reciprocalBG.getWidth() - progressBG.getWidth() - 10);
		progressRatio.setX(width - reciprocalBG.getWidth() - 45);

		localReciprocal(width);
		reciprocalDay.setX(width - FOOTER_SIZE - 20);
		reciprocalBG.setX(width - reciprocalBG.getWidth() - 5);
		super.onResize(width, height);
	}

	private void drawProgress() {
		progress.setText(NumberFormat.getFormat("##").format(checkFlow.getProgress() * 100));
		localProgress(getOffsetWidth());

		progressWidth = progress.getText().length() * NUMBER_SIZE / 2;
		progressBG.setHeight(HEIGHT * checkFlow.getProgress() - 10);
		progressBG.setY(HEIGHT - progressBG.getHeight() - 5);
	}

	private void localProgress(int width) {
		progress.setX(width - reciprocalBG.getWidth() - progressWidth - 25);
	}

	private void drawReciprocal() {
		final int duration = 7;

		if (checkFlow.getDeadline() == null) {
			reciprocal.setText("？");
			reciprocal.setFill(RGB.BLACK);
			reciprocalBG.setFill(RGB.WHITE);
			reciprocalWidth = NUMBER_SIZE;
			localReciprocal(getOffsetWidth());
			return;
		}

		int diff = DateUtil.daysBetween(checkFlow.getDeadline(), new Date());

		reciprocal.setText("" + Math.abs(diff));	//都顯示正數，用顏色來區分
		reciprocalWidth = reciprocal.getText().length() * NUMBER_SIZE / 2;
		Color textColor = RGB.BLACK;

		if (diff >= duration) {
			reciprocalBG.setFill(RGB.WHITE);
		} else if (diff >= 0) {
			int value = (int)((3.0 * diff / duration) % 3);
			reciprocalBG.setFill(RED[value]);
			if (value < 2) { textColor = RGB.WHITE; }
		} else {
			reciprocalBG.setFill(BLACK);
			textColor = RGB.WHITE;
		}

		reciprocal.setFill(textColor);
		reciprocalDay.setFill(textColor);

		localReciprocal(getOffsetWidth());
	}

	private void localReciprocal(int width) {
		reciprocal.setX(width - reciprocalWidth - 20);
	}
}
