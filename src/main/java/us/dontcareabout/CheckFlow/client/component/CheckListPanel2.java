package us.dontcareabout.CheckFlow.client.component;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
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

	private static final SimpleEventBus eventBus = new SimpleEventBus();
	private static final VerticalLayoutData VLD_1x_1 = new VerticalLayoutData(1, 1);
	private static final VerticalLayoutData VLD_1x1 = new VerticalLayoutData(1, -1);
	private static final HorizontalLayoutData CP_LIST_HLD = new HorizontalLayoutData(200, 1, new Margins(0, 2, 0, 0));

	@UiField VerticalLayoutContainer cfContainer;
	@UiField HorizontalLayoutContainer cpContainer;

	private CfDetail cfDetail;
	private ArrayList<CpDetail> cpDetailList = new ArrayList<>();

	public CheckListPanel2() {
		initWidget(uiBinder.createAndBindUi(this));
		eventBus.addHandler(CheckPointChangeEvent.TYPE, new CheckPointChangeHandler() {
			@Override
			public void onCheckPointChange(CheckPointChangeEvent event) {
				refresh();
			}
		});
		eventBus.addHandler(ExpendCheckPointEvent.TYPE, new ExpendCheckPointHandler() {
			@Override
			public void onExpendCheckPoint(ExpendCheckPointEvent event) {
				refresh();
			}
		});
	}

	@Override
	public void setData(CheckFlow cf) {
		//雖然 cfDetail 可以重複使用
		//但是因為 CpListLayer 共用以及其他下略數百字的關係
		//所以還是直接清調重加比較省事
		cfContainer.clear();
		cfDetail = new CfDetail(cf);
		cfContainer.add(cfDetail, VLD_1x1);

		//清空 cpDetailList 以及各 CpDetail 的 HR
		for (CpDetail cpd : cpDetailList) {
			cpd.clearHR();
		}

		cpDetailList.clear();
		////

		for (CheckPoint cp : cf.getPointList()) {
			cpDetailList.add(new CpDetail(cp));
		}

		refresh();
	}

	/**
	 * 套 scheduleDeferred() 是確保來自 {@link #eventBus} 觸發的 refresh()
	 * 是在各 UI component 都更改完畢狀態之後才更新畫面。
	 * 對於 {@link #setData(CheckFlow)} 而言是無意義、甚至會造成畫面閃爍的 Orz。
	 */
	void refresh() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				cfDetail.refresh();
				cpContainer.clear();

				for (CpDetail cpd : cpDetailList) {
					if (!cpd.isShow()) { continue; }

					cpContainer.add(cpd, CP_LIST_HLD);
				}

				cpContainer.forceLayout();
			}
		});
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

		void refresh() {
			list.refresh();
		}
	}

	class CpDetail extends VerticalLayoutContainer {
		GroupingHandlerRegistration hrGroup = new GroupingHandlerRegistration();
		CheckPoint checkPoint;

		/** 紀錄 CfDetail 要求顯示 CpDetail 的 flag	 */
		boolean isExpend = false;

		CpDetail(CheckPoint cp) {
			this.checkPoint = cp;
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

			hrGroup.add(
				eventBus.addHandler(ExpendCheckPointEvent.TYPE, new ExpendCheckPointHandler() {
					@Override
					public void onExpendCheckPoint(ExpendCheckPointEvent event) {
						if (event.data != checkPoint) { return; }
						isExpend = !isExpend;
					}
				})
			);
			hrGroup.add(
				eventBus.addHandler(CheckPointChangeEvent.TYPE, new CheckPointChangeHandler() {
					@Override
					public void onCheckPointChange(CheckPointChangeEvent event) {
						if (!checkPoint.getItemList().contains(event.data)) { return; }

						boolean originFinish = checkPoint.isFinish();
						double progress = checkPoint.getProgress();
						checkPoint.setFinish(progress == 1);

						if (originFinish != checkPoint.isFinish()) {
							eventBus.fireEvent(new CheckPointChangeEvent(checkPoint));
						}
					}
				})
			);
		}

		boolean isShow() {
			return !checkPoint.isFinish() || isExpend;
		}

		void clearHR() {
			hrGroup.removeHandler();
		}
	}

	static final int TITLE_SIZE = 30;
	static final int TITLE_HEIGHT = TITLE_SIZE + 20;

	class EmptyCpLayer extends LayerSprite {
		LTextSprite title = new LTextSprite();
		EmptyButton finish;

		EmptyCpLayer(final CheckPoint cp) {
			setBgColor(Palette.AMBER[1]);

			title.setLX(5);
			title.setLY(2);
			title.setFontSize(TITLE_SIZE);
			title.setFill(Palette.BLACK);
			title.setText(cp.getName());
			add(title);

			finish = new EmptyButton(cp);
			finish.addSpriteSelectionHandler(new SpriteSelectionHandler() {
				@Override
				public void onSpriteSelect(SpriteSelectionEvent event) {
					cp.setFinish(!cp.isFinish());
					refresh();
					eventBus.fireEvent(new CheckPointChangeEvent(cp));
				}
			});
			add(finish);
		}

		@Override
		protected void adjustMember() {
			finish.resize(getWidth() - 14, 40);
			finish.setLX(7);
			finish.setLY(TITLE_HEIGHT);
		}

		class EmptyButton extends CpButton {
			EmptyButton(CheckPoint cp) {
				super(cp);
				setText("完成");
			}
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

		void refresh() {
			for (CpButton btn : btnList) {
				btn.refresh();
			}

			redraw();
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

		class CpBtn4Cf extends CpButton {
			CpBtn4Cf(CheckPoint cp) {
				super(cp);
				addSpriteSelectionHandler(new SpriteSelectionHandler() {
					@Override
					public void onSpriteSelect(SpriteSelectionEvent event) {
						if (!checkPoint.isFinish()) { return; }

						eventBus.fireEvent(new ExpendCheckPointEvent(checkPoint));
					}
				});
			}
		}

		class CpBtn4Cp extends CpButton {
			CpBtn4Cp(CheckPoint cp) {
				super(cp);
				addSpriteSelectionHandler(new SpriteSelectionHandler() {
					@Override
					public void onSpriteSelect(SpriteSelectionEvent event) {
						checkPoint.setFinish(!checkPoint.isFinish());
						refresh();
						eventBus.fireEvent(new CheckPointChangeEvent(checkPoint));
					}
				});
			}
		}
	}

	class CpButton extends TextButton {
		final CheckPoint checkPoint;

		CpButton(CheckPoint cp) {
			this.checkPoint = cp;
			setBgRadius(10);
			setText(cp.getName());
			refresh();
		}

		void refresh() {
			setBgColor(checkPoint.isFinish() ? Palette.BLUE : Palette.RED[2]);
		}
	}

	static class CheckPointChangeEvent extends GwtEvent< CheckPointChangeHandler> {
		static final Type< CheckPointChangeHandler> TYPE = new Type< CheckPointChangeHandler>();
		public final CheckPoint data;

		public CheckPointChangeEvent(CheckPoint cp) {
			data = cp;
		}

		@Override
		public Type< CheckPointChangeHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(CheckPointChangeHandler handler) {
			handler.onCheckPointChange(this);
		}
	}

	interface CheckPointChangeHandler extends EventHandler{
		public void onCheckPointChange(CheckPointChangeEvent event);
	}

	static class ExpendCheckPointEvent extends GwtEvent< ExpendCheckPointHandler> {
		static final Type< ExpendCheckPointHandler> TYPE = new Type< ExpendCheckPointHandler>();
		public final CheckPoint data;

		public ExpendCheckPointEvent(CheckPoint result) {
			data = result;
		}

		@Override
		public Type< ExpendCheckPointHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(ExpendCheckPointHandler handler) {
			handler.onExpendCheckPoint(this);
		}
	}

	interface ExpendCheckPointHandler extends EventHandler{
		public void onExpendCheckPoint(ExpendCheckPointEvent event);
	}
}
