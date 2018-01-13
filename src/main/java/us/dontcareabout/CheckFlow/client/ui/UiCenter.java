package us.dontcareabout.CheckFlow.client.ui;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.Viewport;

import us.dontcareabout.CheckFlow.client.component.CheckListPanel;
import us.dontcareabout.CheckFlow.client.data.DataCenter;
import us.dontcareabout.CheckFlow.client.view.CheckListView;
import us.dontcareabout.CheckFlow.client.view.NewCheckListView;
import us.dontcareabout.CheckFlow.shared.CheckFlow;

public class UiCenter {
	private final static Viewport viewport = new Viewport();
	private static CheckListView checkListView;
	private static CheckListPanel checkListPanel;
	private static NewCheckListView newCheckFlowView;

	public static void start() {
		RootPanel.get().add(viewport);
		checkListView();
	}

	public static void checkListView() {
		if (checkListView == null) {
			checkListView = new CheckListView();
		}

		switchTo(checkListView);
	}

	public static void checkListMonitor(CheckFlow checkFlow) {
		if (checkListPanel == null) {
			checkListPanel = new CheckListPanel();
		}

		checkListPanel.setData(checkFlow);
		switchTo(checkListPanel);
	}

	public static void newCheckListView() {
		if (newCheckFlowView == null) {
			newCheckFlowView = new NewCheckListView();
		}

		newCheckFlowView.setData(DataCenter.getTemplates().get(0));	//FIXME
		switchTo(newCheckFlowView);
	}

	private static void switchTo(Widget widget) {
		viewport.clear();
		viewport.add(widget);
		viewport.forceLayout();
	}
}
