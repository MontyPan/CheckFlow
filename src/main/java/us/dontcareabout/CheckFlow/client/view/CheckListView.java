package us.dontcareabout.CheckFlow.client.view;

import java.util.ArrayList;

import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import us.dontcareabout.CheckFlow.client.component.CheckListInfo;
import us.dontcareabout.CheckFlow.client.component.ToolItem;
import us.dontcareabout.CheckFlow.client.component.Toolbar;
import us.dontcareabout.CheckFlow.client.data.CheckListReadyEvent;
import us.dontcareabout.CheckFlow.client.data.CheckListReadyEvent.CheckFlowListHandler;
import us.dontcareabout.CheckFlow.client.data.DataCenter;
import us.dontcareabout.CheckFlow.client.ui.UiCenter;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.gxt.client.draw.Layer;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;

public class CheckListView extends VerticalLayoutContainer {
	private CheckFlowList list = new CheckFlowList();
	private Toolbar toolbar = new Toolbar();
	private VerticalLayoutContainer main = new VerticalLayoutContainer();

	public CheckListView() {
		main.add(list, new VerticalLayoutData(1, -1));
		main.setScrollMode(ScrollMode.AUTOY);
		add(main, new VerticalLayoutData(1, 1));
		add(toolbar, new VerticalLayoutData(1, 80));
		buildToolbar();

		DataCenter.addCheckFlowReady(new CheckFlowListHandler() {
			@Override
			public void onCheckFlowReady(CheckListReadyEvent event) {
				checkFlowReady(event.data);
			}
		});
	}

	private void buildToolbar() {
		ToolItem add = new ToolItem("新增檢查流程");
		add.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				UiCenter.newCheckListView();
			}
		});
		ToolItem checkTemplate = new ToolItem("流程範本管理");
		toolbar.add(add);
		toolbar.add(checkTemplate);
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
				CheckListInfo item = new CheckListInfo();
				item.setLY(index * (CheckListInfo.HEIGHT + 5));
				item.setData(cf);
				addLayer(item);
				index++;
			}

			setHeight(checkFlows.size() * (CheckListInfo.HEIGHT + 5));
		}

		@Override
		protected void onResize(int width, int height) {
			for (Layer layer : getLayers()) {
				((LayerSprite) layer).onResize(width, CheckListInfo.HEIGHT);
			}

			super.onResize(width, height);
		}
	}
}