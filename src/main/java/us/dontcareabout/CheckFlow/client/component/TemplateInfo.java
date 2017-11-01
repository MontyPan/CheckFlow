package us.dontcareabout.CheckFlow.client.component;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;

import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;

public class TemplateInfo extends FramedPanel {
	private static final VerticalLayoutData CP_VLD = new VerticalLayoutData(1, 1, new Margins(1, 1, 0, 2));
	private static final VerticalLayoutData VERSION_VLD = new VerticalLayoutData(1, 30, new Margins(10, 3, 0, 3));

	private VerticalLayoutContainer container = new VerticalLayoutContainer();
	private LabelToolItem version = new LabelToolItem();

	public TemplateInfo() {
		add(container);
	}

	public void setData(CheckFlow cf) {
		setHeadingText(cf.getName());
		version.setLabel("版本：" + cf.getVersionId().substring(0, 8));

		VerticalLayoutContainer checkPointList = new VerticalLayoutContainer();
		checkPointList.setScrollMode(ScrollMode.AUTOY);

		for (CheckPoint cp : cf.getPointList()) {
			checkPointList.add(new CheckPointPanel(cp));
		}

		container.clear();
		container.add(checkPointList, CP_VLD);
		container.add(version, VERSION_VLD);
		Scheduler.get().scheduleFinally(new ScheduledCommand() {
			@Override
			public void execute() {
				forceLayout();
			}
		});
	}

	private class CheckPointPanel extends ContentPanel {
		private VerticalLayoutContainer itemList = new VerticalLayoutContainer();

		public CheckPointPanel(CheckPoint cp) {
			add(itemList);
			setHeadingText(cp.getName());

			for (CheckPoint item : cp.getItemList()) {
				itemList.add(new LabelToolItem(item.getName()));
			}
		}
	}
}