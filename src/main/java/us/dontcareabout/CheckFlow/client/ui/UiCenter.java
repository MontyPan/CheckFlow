package us.dontcareabout.CheckFlow.client.ui;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.Viewport;

import us.dontcareabout.CheckFlow.client.view.CheckFlowView;

public class UiCenter {
	private final static Viewport viewport = new Viewport();
	private static CheckFlowView checkFlowView;

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

	private static void switchTo(Widget widget) {
		viewport.clear();
		viewport.add(widget);
		viewport.forceLayout();
	}
}
