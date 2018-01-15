package us.dontcareabout.CheckFlow.client.view;

import com.google.gwt.user.client.Window;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.TextArea;

import us.dontcareabout.CheckFlow.client.component.ToolItem;
import us.dontcareabout.CheckFlow.client.component.Toolbar;
import us.dontcareabout.CheckFlow.client.data.DataCenter;
import us.dontcareabout.CheckFlow.client.data.SaveTemplateEndEvent;
import us.dontcareabout.CheckFlow.client.data.SaveTemplateEndEvent.SaveTemplateEndHandler;
import us.dontcareabout.CheckFlow.client.ui.UiCenter;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;

public class MaintainTemplateView extends CenterLayoutContainer {
	private VerticalLayoutContainer container = new VerticalLayoutContainer();
	private Toolbar toolbar = new Toolbar();
	private TextArea context = new TextArea();

	private CheckFlow template;

	public MaintainTemplateView() {
		container.setPixelSize(400, 600);
		container.add(context, new VerticalLayoutData(1, 1, new Margins(5)));
		container.add(toolbar, new VerticalLayoutData(1, 60));
		buildToolbar();
		add(container);

		DataCenter.addSaveTemplateEnd(new SaveTemplateEndHandler() {
			@Override
			public void onSaveTemplateEnd(SaveTemplateEndEvent event) {
				UiCenter.checkListView();
			}
		});
	}

	public void setData(CheckFlow template) {
		this.template = template;

		StringBuffer result = new StringBuffer();

		for (CheckPoint cp : template.getPointList()) {
			result.append(cp.getName() + "\n");

			for (CheckPoint item : cp.getItemList()) {
				result.append("    " + item.getName() + "\n");
			}
		}

		context.setValue(result.toString());
	}

	private void buildToolbar() {
		ToolItem save = new ToolItem("儲存");
		save.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				doSave();
			}
		});
		toolbar.add(save);

		ToolItem cancel = new ToolItem("取消");
		cancel.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				UiCenter.checkListView();
			}
		});
		toolbar.add(cancel);
	}

	private void doSave() {
		String[] lines = context.getText().split("\n");

		CheckFlow result = new CheckFlow();
		result.setVersionId(template.getVersionId());
		//versionId 到 server side 實際存入的時候才改變 [逃]

		CheckPoint cp = null;
		CheckPoint item = null;

		for (String line : lines) {
			//跳過完全空行的
			if (line.trim().length() == 0) { continue; }
			if (line.startsWith("    ")) {
				if (cp == null) {
					Window.alert("格式不正確");
					return;
				}

				item = new CheckPoint();
				item.setName(line.trim());
				cp.getItemList().add(item);
			} else {
				cp = new CheckPoint();
				cp.setName(line.trim());
				result.getPointList().add(cp);
			}
		}

		DataCenter.saveTemplate(result);
	}
}
