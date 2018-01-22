package us.dontcareabout.CheckFlow.client.ui;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.Viewport;

import us.dontcareabout.CheckFlow.client.data.DataCenter;
import us.dontcareabout.CheckFlow.client.view.CheckListMonitor;
import us.dontcareabout.CheckFlow.client.view.CheckListView;
import us.dontcareabout.CheckFlow.client.view.MaintainTemplateView;
import us.dontcareabout.CheckFlow.client.view.NewCheckListView;
import us.dontcareabout.CheckFlow.shared.CheckFlow;

public class UiCenter {
	private final static Viewport viewport = new Viewport();
	private static CheckListView checkListView;
	private static CheckListMonitor checkListMonitor;
	private static NewCheckListView newCheckFlowView;
	private static MaintainTemplateView maintainTemplate;

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
		if (checkListMonitor == null) {
			checkListMonitor = new CheckListMonitor();
		}

		checkListMonitor.setData(checkFlow);
		switchTo(checkListMonitor);
	}

	public static void newCheckListView() {
		if (newCheckFlowView == null) {
			newCheckFlowView = new NewCheckListView();
		}

		newCheckFlowView.setData(DataCenter.getTemplates().get(0));	//FIXME
		switchTo(newCheckFlowView);
	}

	public static void maintainTemplate() {
		if (maintainTemplate == null) {
			maintainTemplate = new MaintainTemplateView();
		}

		maintainTemplate.setData(DataCenter.getTemplates().get(0));	//FIXME
		switchTo(maintainTemplate);
	}

	private static void switchTo(Widget widget) {
		viewport.clear();
		viewport.add(widget);
		viewport.forceLayout();
	}
}
