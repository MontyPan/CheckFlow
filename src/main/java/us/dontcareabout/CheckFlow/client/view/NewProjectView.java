package us.dontcareabout.CheckFlow.client.view;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;

import us.dontcareabout.CheckFlow.client.data.DataCenter;
import us.dontcareabout.CheckFlow.client.ui.UiCenter;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;

public class NewProjectView extends Composite {
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	interface MyUiBinder extends UiBinder<Widget, NewProjectView> {}

	private static final VerticalLayoutData CP_VLD = new VerticalLayoutData(1, 20);

	@UiField TextField name;
	@UiField DateField deadline;
	@UiField VerticalLayoutContainer cpList;

	private CheckFlow data;
	private ArrayList<DateField> deadlineList = new ArrayList<>();

	public NewProjectView() {
		initWidget(uiBinder.createAndBindUi(this));

		name.addValidator(new EmptyValidator<String>());
	}

	public void setData(CheckFlow template) {
		this.data = template;
		cpList.clear();
		deadlineList.clear();

		for (CheckPoint cp : template.getPointList()) {
			DateField df = new DateField();
			deadlineList.add(df);

			FieldLabel tf = new FieldLabel();
			tf.setText(cp.getName());
			tf.setWidget(df);
			cpList.add(tf, CP_VLD);
		}

		cpList.forceLayout();
	}

	@UiHandler("confirm")
	void clickConfirm(SelectEvent se) {
		if (!validate()) { return; }

		data.setName(name.getValue());
		data.setDeadline(deadline.getValue());

		ArrayList<CheckPoint> checkPoints = data.getPointList();

		for (int i = 0; i < checkPoints.size(); i++) {
			checkPoints.get(i).setDeadline(deadlineList.get(i).getValue());
		}

		DataCenter.saveCheckList(data);
	}

	@UiHandler("cancel")
	void clickCancel(SelectEvent se) {
		UiCenter.checkFlowView();
	}

	private boolean validate() {
		if (!name.isValid()) { return false; }

		//下面是各個 deadline 的檢查
		//允許沒有設定 deadline
		Date last = null;

		for (int i = 0; i < deadlineList.size(); i++) {
			Date date = deadlineList.get(i).getValue();

			if (date == null) { continue; }

			if (last == null) {
				last = date;
				continue;
			}

			if (last.after(date)) {
				Window.alert("「" + data.getPointList().get(i).getName() + "」的死線比前一個還早，WTF？");
				return false;
			}
		}

		if (last != null && deadline.getValue() != null && last.after(deadline.getValue())) {
			Window.alert("整個檢查流程的死線比最後一個檢查點還早，WTF？");
			return false;
		}

		return true;
	}
}
