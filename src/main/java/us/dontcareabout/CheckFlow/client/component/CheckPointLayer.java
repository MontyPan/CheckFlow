package us.dontcareabout.CheckFlow.client.component;

import java.util.Date;

import us.dontcareabout.CheckFlow.client.DateUtil;
import us.dontcareabout.CheckFlow.shared.CheckPoint;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;

public class CheckPointLayer extends LayerSprite {
	private CheckPoint data;
	private LTextSprite text = new LTextSprite();
	private LTextSprite reciprocal = new LTextSprite();

	public CheckPointLayer() {
		add(text);
		add(reciprocal);
	}

	public void setData(CheckPoint checkPoint) {
		this.data = checkPoint;
		getText().setText(checkPoint.getName());

		if (checkPoint.isFinish()) {
			reciprocal.setText("");
		} else {
			int diff = DateUtil.daysBetween(checkPoint.getDeadline(), new Date());
			String reciprocalHeader = diff < 0 ? "逾 " : "剩 ";
			getReciprocal().setText(reciprocalHeader + Math.abs(diff) + " 天");
		}

		adjustMember();
	}

	@Override
	protected void adjustMember() {
		getReciprocal().setLX(
			getWidth() -
			(getReciprocal().getText().length() + 2) *	//有兩個中文字補兩個 unit
			getReciprocal().getFontSize() / 2
			- 10	//右邊的 margin
		);
	}

	public LTextSprite getText() {
		return text;
	}

	public LTextSprite getReciprocal() {
		return reciprocal;
	}

	public CheckPoint getData() {
		return data;
	}
}