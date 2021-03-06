package us.dontcareabout.CheckFlow.client.view;

import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import us.dontcareabout.CheckFlow.client.component.CheckListPanel;
import us.dontcareabout.CheckFlow.client.component.CheckListPanel1;
import us.dontcareabout.CheckFlow.client.component.CheckListPanel2;
import us.dontcareabout.CheckFlow.client.component.ToolItem;
import us.dontcareabout.CheckFlow.client.component.Toolbar;
import us.dontcareabout.CheckFlow.client.data.DataCenter;
import us.dontcareabout.CheckFlow.client.data.DelCheckListEndEvent;
import us.dontcareabout.CheckFlow.client.data.DelCheckListEndEvent.DelCheckListEndHandler;
import us.dontcareabout.CheckFlow.client.data.SaveCheckListEndEvent;
import us.dontcareabout.CheckFlow.client.data.SaveCheckListEndEvent.SaveCheckListEndHandler;
import us.dontcareabout.CheckFlow.client.data.Setting;
import us.dontcareabout.CheckFlow.client.ui.UiCenter;
import us.dontcareabout.CheckFlow.shared.CheckFlow;

public class CheckListMonitor extends VerticalLayoutContainer {
	private CheckListPanel list;
	private Toolbar toolbar = new Toolbar();
	private VerticalLayoutContainer main = new VerticalLayoutContainer();

	private CheckFlow checkList;

	public CheckListMonitor() {
		main.setScrollMode(ScrollMode.AUTOY);

		if (Setting.style()) {
			list = new CheckListPanel1();
			main.add(list, new VerticalLayoutData(1, -1));
		} else {
			list = new CheckListPanel2();
			main.add(list, new VerticalLayoutData(1, 1));
		}

		add(toolbar, new VerticalLayoutData(1, 60));
		add(main, new VerticalLayoutData(1, 1));
		buildToolbar();

		DataCenter.addSaveCheckListEnd(new SaveCheckListEndHandler() {
			@Override
			public void onSaveCheckListEnd(SaveCheckListEndEvent event) {
				UiCenter.checkListView();
			}
		});
		DataCenter.addDelCheckListEnd(new DelCheckListEndHandler() {
			@Override
			public void onDelCheckListEnd(DelCheckListEndEvent event) {
				UiCenter.checkListView();
			}
		});
	}

	public void setData(CheckFlow checkList) {
		this.checkList = checkList;
		list.setData(checkList);
		main.forceLayout();
	}

	private void buildToolbar() {
		ToolItem save = new ToolItem("儲存");
		save.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				DataCenter.saveCheckList(checkList);
			}
		});
		toolbar.add(save);

		ToolItem delete = new ToolItem("刪除");
		delete.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				DataCenter.delCheckList(checkList);
			}
		});
		toolbar.add(delete);

		ToolItem cancel = new ToolItem("取消");
		cancel.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				UiCenter.checkListView();
			}
		});
		toolbar.add(cancel);
	}
}
