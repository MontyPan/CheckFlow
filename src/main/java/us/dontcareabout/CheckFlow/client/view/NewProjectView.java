package us.dontcareabout.CheckFlow.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;

import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;

public class NewProjectView extends Composite {
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	interface MyUiBinder extends UiBinder<Widget, NewProjectView> {}

	private static final VerticalLayoutData CP_VLD = new VerticalLayoutData(1, 20);

	@UiField TextField name;
	@UiField DateField deadline;
	@UiField VerticalLayoutContainer cpList;

	public NewProjectView() {
		initWidget(uiBinder.createAndBindUi(this));

		name.addValidator(new EmptyValidator<String>());
	}

	public void setData(CheckFlow template) {
		cpList.clear();

		for (CheckPoint cp : template.getPointList()) {
			FieldLabel tf = new FieldLabel();
			tf.setText(cp.getName());
			tf.setWidget(new DateField());
			cpList.add(tf, CP_VLD);
		}

		cpList.forceLayout();
	}
}
