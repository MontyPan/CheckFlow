package us.dontcareabout.CheckFlow.client.ui;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.Viewport;

import us.dontcareabout.CheckFlow.client.component.CheckFlowPanel;
import us.dontcareabout.CheckFlow.client.view.CheckFlowView;
import us.dontcareabout.CheckFlow.shared.CheckFlow;

public class UiCenter {
	private final static Viewport viewport = new Viewport();
	private static CheckFlowView checkFlowView;
	private static CheckFlowPanel checkFlowPanel;

	public static void start() {
		RootPanel.get().add(viewport);
		checkFlowView();
	}

	public static void checkFlowView() {
		if (checkFlowView == null) {
			checkFlowView = new CheckFlowView();
		}

		switchTo(checkFlowView);
	}

	public static void checkFlowMonitor(CheckFlow checkFlow) {
		if (checkFlowPanel == null) {
			checkFlowPanel = new CheckFlowPanel();
		}

		checkFlowPanel.setData(checkFlow);
		switchTo(checkFlowPanel);
	}

	private static void switchTo(Widget widget) {
		viewport.clear();
		viewport.add(widget);
		viewport.forceLayout();
	}
}
