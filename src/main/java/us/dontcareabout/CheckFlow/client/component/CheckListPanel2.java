package us.dontcareabout.CheckFlow.client.component;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import us.dontcareabout.CheckFlow.client.Palette;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;

public class CheckListPanel2 extends Composite implements CheckListPanel {
	private static CheckListPanel2UiBinder uiBinder = GWT.create(CheckListPanel2UiBinder.class);
	interface CheckListPanel2UiBinder extends UiBinder<Widget, CheckListPanel2> {}

	private static final VerticalLayoutData VLD_1x_1 = new VerticalLayoutData(1, 1);
	private static final VerticalLayoutData VLD_1x1 = new VerticalLayoutData(1, -1);
	private static final HorizontalLayoutData CP_LIST_HLD = new HorizontalLayoutData(200, 1, new Margins(0, 2, 0, 0));

	@UiField VerticalLayoutContainer cfContainer;
	@UiField HorizontalLayoutContainer cpContainer;

	private CfDetail cfDetail;
	private ArrayList<CpDetail> cpDetailList = new ArrayList<>();

	public CheckListPanel2() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setData(CheckFlow cf) {
		//雖然 cfDetail 可以重複使用
		//但是因為 CpListLayer 共用以及其他下略數百字的關係
		//所以還是直接清調重加比較省事
		cfContainer.clear();
		cfDetail = new CfDetail(cf);
		cfContainer.add(cfDetail, VLD_1x1);

		cpDetailList.clear();

		for (CheckPoint cp : cf.getPointList()) {
			cpDetailList.add(new CpDetail(cp));
		}

		refresh();
	}

	 void refresh() {
		cpContainer.clear();

		for (CpDetail cpd : cpDetailList) {
			if (!cpd.isShow()) { continue; }

			cpContainer.add(cpd, CP_LIST_HLD);
		}

		cpContainer.forceLayout();
	}

	class CfDetail extends VerticalLayoutContainer {
		private final CpListLayer list;

		CfDetail(CheckFlow cf) {
			list = new CpListLayer(true);

			//要先作 add()，這樣 LayerSprite.asWidget() 才會被 call 到
			//後面 setData() 要 resize() 的時候才能真正改到 widget 的大小
			add(list, VLD_1x_1);
			list.setData(cf.getName(), cf.getPointList());
		}
	}

	class CpDetail extends VerticalLayoutContainer {
		CheckPoint cp;

		/** 紀錄 CfDetail 要求顯示 CpDetail 的 flag	 */
		boolean isExpend = true;

		CpDetail(CheckPoint cp) {
			this.cp = cp;
			setScrollMode(ScrollMode.AUTOY);
			setAdjustForScroll(true);

			if (cp.getItemList().size() == 0) {
				add(new EmptyCpLayer(cp), new VerticalLayoutData(1, 100));
			} else {
				VerticalLayoutContainer scroll = new VerticalLayoutContainer();
				CpListLayer list = new CpListLayer(false);

				//參見 CfDetail 的 constructor
				scroll.add(list, VLD_1x_1);
				list.setData(cp.getName(), cp.getItemList());
				add(scroll, VLD_1x1);
			}
		}

		boolean isShow() {
			return !cp.isFinish() || isExpend;
		}
	}

	static final int TITLE_SIZE = 30;
	static final int TITLE_HEIGHT = TITLE_SIZE + 20;

	class EmptyCpLayer extends LayerSprite {
		LTextSprite title = new LTextSprite();
		TextButton finish = new TextButton("完成");

		EmptyCpLayer(CheckPoint cp) {
			setBgColor(Palette.AMBER[1]);

			title.setLX(5);
			title.setLY(2);
			title.setFontSize(TITLE_SIZE);
			title.setFill(Palette.BLACK);
			title.setText(cp.getName());
			add(title);

			finish.setBgColor(Palette.RED[2]);
			add(finish);
		}

		@Override
		protected void adjustMember() {
			finish.resize(getWidth() - 14, 40);
			finish.setLX(7);
			finish.setLY(TITLE_HEIGHT);
		}
	}

	class CpListLayer extends LayerSprite {
		static final int ITEM_HEIGHT = 30;

		boolean isCF;
		LTextSprite title = new LTextSprite();
		ArrayList<CpButton> btnList = new ArrayList<>();

		CpListLayer(boolean isCF) {
			this.isCF = isCF;
		}

		@Override
		protected void adjustMember() {
			int index = 0;
			for (CpButton btn : btnList) {
				btn.resize(getWidth() - 4, ITEM_HEIGHT);
				btn.setLX(2);
				btn.setLY(index * (ITEM_HEIGHT + 2) + TITLE_HEIGHT);
				index++;
			}
		}

		void setData(String name, ArrayList<CheckPoint> list) {
			setBgColor(Palette.AMBER[1]);

			title.setLX(5);
			title.setLY(2);
			title.setFontSize(TITLE_SIZE);
			title.setFill(Palette.BLACK);

			add(title);
			title.setText(name);

			resize(getWidth(), list.size() * (ITEM_HEIGHT + 2) + TITLE_HEIGHT + 3);

			for (CheckPoint cp : list) {
				CpButton btn = isCF ? new CpBtn4Cf(cp) : new CpBtn4Cp(cp);
				add(btn);
				btnList.add(btn);
			}

			redeploy();
		}

		class CpButton extends TextButton {
			final CheckPoint cp;

			CpButton(CheckPoint cp) {
				this.cp = cp;
				setBgRadius(10);
				setText(cp.getName());
				setBgColor(cp.isFinish() ? Palette.BLUE : Palette.RED[2]);
			}
		}

		class CpBtn4Cf extends CpButton {
			CpBtn4Cf(CheckPoint cp) {
				super(cp);
				addSpriteSelectionHandler(new SpriteSelectionHandler() {
					@Override
					public void onSpriteSelect(SpriteSelectionEvent event) {

					}
				});
			}
		}

		class CpBtn4Cp extends CpButton {
			CpBtn4Cp(CheckPoint cp) {
				super(cp);
			}
		}
	}
}
