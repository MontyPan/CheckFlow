package us.dontcareabout.CheckFlow.client.component;

import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;

import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.gxt.client.draw.LTextSprite;

public class CheckListInfo1 extends CheckListInfo {
	private LTextSprite nowCheckPoint = new LTextSprite();
	private Block progress = new Block("%");

	public CheckListInfo1() {
		nowCheckPoint.setTextBaseline(TextBaseline.MIDDLE);
		nowCheckPoint.setFontSize(18);
		nowCheckPoint.setLX(40);
		nowCheckPoint.setLY(95);
		add(nowCheckPoint);

		add(progress);
	}

	@Override
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
		progress.setProgress(checkFlow.getStepProgress());
	}

}
