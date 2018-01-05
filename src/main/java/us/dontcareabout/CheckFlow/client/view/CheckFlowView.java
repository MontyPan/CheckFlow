package us.dontcareabout.CheckFlow.client.view;

import java.util.ArrayList;

import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import us.dontcareabout.CheckFlow.client.component.CheckFlowInfo;
import us.dontcareabout.CheckFlow.client.component.TextButton;
import us.dontcareabout.CheckFlow.client.data.CheckFlowReadyEvent;
import us.dontcareabout.CheckFlow.client.data.CheckFlowReadyEvent.CheckFlowReadyHandler;
import us.dontcareabout.CheckFlow.client.data.DataCenter;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.gxt.client.draw.Layer;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;

public class CheckFlowView extends VerticalLayoutContainer {
	private CheckFlowList list = new CheckFlowList();
	private VerticalLayoutContainer main = new VerticalLayoutContainer();

	public CheckFlowView() {
		main.add(list, new VerticalLayoutData(1, -1));
		main.setScrollMode(ScrollMode.AUTOY);
		add(main, new VerticalLayoutData(1, 1));
		add(new Toolbar(), new VerticalLayoutData(1, 80));

		DataCenter.addCheckFlowReady(new CheckFlowReadyHandler() {
			@Override
			public void onCheckFlowReady(CheckFlowReadyEvent event) {
				checkFlowReady(event.data);
			}
		});
	}

	private void checkFlowReady(ArrayList<CheckFlow> checkFlows) {
		list.setData(checkFlows);
		list.setWidth(0);	//強制觸發 onResize()。不過高度不能調 XD
		main.forceLayout();	//這邊觸發的 onResize() 才會是正確的大小 Orz
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		DataCenter.wantCheckFlows();
	}

	class CheckFlowList extends LayerContainer {
		public void setData(ArrayList<CheckFlow> checkFlows) {
			this.clear();
			int index = 0;

			for (final CheckFlow cf : checkFlows) {
				CheckFlowInfo item = new CheckFlowInfo();
				item.setLY(index * (CheckFlowInfo.HEIGHT + 5));
				item.setData(cf);
				addLayer(item);
				index++;
			}

			setHeight(checkFlows.size() * (CheckFlowInfo.HEIGHT + 5));
		}

		@Override
		protected void onResize(int width, int height) {
			for (Layer layer : getLayers()) {
				((LayerSprite) layer).onResize(width, CheckFlowInfo.HEIGHT);
			}

			super.onResize(width, height);
		}
	}

	class ToolItem extends TextButton {
		public ToolItem(String str) {
			super(str);
			setBgColor(RGB.LIGHTGRAY);
			setBgRadius(20);
			setMargin(20);
		}
	}

	class Toolbar extends LayerContainer {
		ToolItem add = new ToolItem("新增專案");
		ToolItem checkTemplate = new ToolItem("流程範本管理");

		ArrayList<LayerSprite> items = new ArrayList<>();

		public Toolbar() {
			add(add);
			add(checkTemplate);
		}

		@Override
		protected void onResize(int width, int height) {
			//Refactory 抽去 GF：HorizontalLayer
			final int margin = 10;
			double wUnit = width / items.size() - margin * 2;
			int index = 0;

			for (LayerSprite btn : items) {
				btn.onResize(wUnit, height);
				btn.setLX(margin + index * (wUnit + margin * 2));
				index++;
			}

			super.onResize(width, height);
		}

		private void add(LayerSprite item) {
			addLayer(item);
			items.add(item);
		}
	}
}