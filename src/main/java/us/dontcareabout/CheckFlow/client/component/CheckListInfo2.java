package us.dontcareabout.CheckFlow.client.component;

import java.util.ArrayList;

import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.core.client.util.PreciseRectangle;

import us.dontcareabout.CheckFlow.client.Formate;
import us.dontcareabout.CheckFlow.client.Palette;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;
import us.dontcareabout.gxt.client.draw.LRectangleSprite;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;

public class CheckListInfo2 extends CheckListInfo {
	private CfProgress progress = new CfProgress();
	private LRectangleSprite bottomBorder = new LRectangleSprite();

	private ArrayList<CpProgress> pointRS = new ArrayList<>();

	public CheckListInfo2() {
		name.setLZIndex(100);	//避免 pointRS 蓋掉 name
		progress.setLY(118);
		add(progress);

		bottomBorder.setLY(128);
		bottomBorder.setFill(RGB.BLACK);
		add(bottomBorder);
	}

	@Override
	public void setData(CheckFlow cf) {
		checkFlow = cf;
		name.setText(cf.getName());
		progress.setValue(cf.getAverageProgress());

		for (CheckPoint cp : cf.getPointList()) {
			CpProgress cpp = new CpProgress(cp);
			add(cpp);
			pointRS.add(cpp);
		}

		drawReciprocal();
		adjustMember();
	}

	@Override
	protected void adjustMember() {
		//目前不考慮高度變化
		//TODO 寬度過小時 name 與 nowCheckPoint 要縮減字
		double space = getWidth() - reciprocal.getWidth() - 10;

		progress.setLX(2);
		progress.setLY(getHeight() - 24);
		progress.resize(space - 3, 20);	//右端要跟 reciprocal 隔 5px，但是左邊又已經留了 2px
		bottomBorder.setLY(getHeight() - 4);
		bottomBorder.setWidth(getWidth());
		bottomBorder.setHeight(2);
		reciprocal.setLX(space + 5);

		int cpAmount = checkFlow.getPointList().size();
		double cppWidth = 50;

		if (cppWidth * cpAmount > space - 2) {
			cppWidth = (space - 2) / cpAmount;
		}

		double cppLx = space - (cppWidth * cpAmount);

		for (CpProgress cpp : pointRS) {
			cpp.adjust(cppLx, 5, cppWidth - 1, getHeight() - 30);
			cppLx += cppWidth;
		}
	}

	class CfProgress extends LayerSprite {
		private double value;
		private LRectangleSprite bg = new LRectangleSprite();
		private LTextSprite text = new LTextSprite();

		CfProgress() {
			bg.setFill(Palette.BLUE);
			add(bg);
			text.setFontSize(15);
			add(text);
		}

		public void setValue(double value) {
			this.value = value;
			text.setText(Formate.NUMBER.format(value * 100) + "%");
			adjustMember();
		}

		@Override
		protected void adjustMember() {
			bg.setHeight(getHeight());
			bg.setWidth(getWidth() * value);

			text.redraw();
			PreciseRectangle textBox = text.getBBox();
			if (bg.getWidth() < textBox.getWidth() + 5) {
				text.setLX(bg.getWidth() + 5);
				text.setFill(Palette.BLUE);
			} else {
				text.setLX(bg.getWidth() - textBox.getWidth() - 5);
				text.setFill(RGB.WHITE);
			}
		}
	}

	class CpProgress extends LRectangleSprite {
		private CheckPoint checkPoint;

		CpProgress(CheckPoint cp) {
			checkPoint = cp;
			setFill(Palette.BLUE);
			setOpacity(cp.getProgress());
		}

		void adjust(double x, double y, double width, double height) {
			if (width < 0 || height < 0) { return; }

			setWidth(width);
			setHeight(height * checkPoint.getProgress());
			setLX(x);
			setLY(y + height * (1 - checkPoint.getProgress()));
		}
	}
}
