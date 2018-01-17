package us.dontcareabout.CheckFlow.client.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;

import us.dontcareabout.CheckFlow.client.data.DataCenter;
import us.dontcareabout.CheckFlow.client.data.SaveCheckListEndEvent;
import us.dontcareabout.CheckFlow.client.data.SaveCheckListEndEvent.SaveCheckListEndHandler;
import us.dontcareabout.CheckFlow.client.ui.UiCenter;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;
import us.dontcareabout.gxt.client.component.GFComposite;

public class NewCheckListView extends GFComposite {
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	interface MyUiBinder extends UiBinder<Widget, NewCheckListView> {}

	private static final VerticalLayoutData CP_VLD = new VerticalLayoutData(1, 20);

	@UiField TextField name;
	@UiField DateField deadline;
	@UiField VerticalLayoutContainer cpList;

	private final ArrayList<DateField> deadlineList = new ArrayList<>();
	private final SaveCheckListEndHandler saveHandler = new SaveCheckListEndHandler() {
		@Override
		public void onSaveCheckListEnd(SaveCheckListEndEvent event) {
			unmask();
			UiCenter.checkListView();
		}
	};
	private final Validator<String> fileNameValidator = new Validator<String>() {
		@Override
		public List<EditorError> validate(Editor<String> editor, String value) {
			RegExp regExp = RegExp.compile("[\\\\/:\"*?<>|]+");

			if (regExp.test(value)) {
				List<EditorError> errors = new ArrayList<EditorError>();
				errors.add(new DefaultEditorError(editor, "包含非法字元「\\ / : \" * ? < > |」", ""));
				return errors;
			}
			return null;
		}
	};

	private CheckFlow data;

	public NewCheckListView() {
		initWidget(uiBinder.createAndBindUi(this));

		name.addValidator(new EmptyValidator<String>());
		name.addValidator(fileNameValidator);
	}

	public void setData(CheckFlow template) {
		//用同一個 reference 也沒關係，反正只有要讀取 template 的 CheckPoint.name 而已 XD
		this.data = template;

		//因為是用同一個 NewCheckListView 的 instance，所以要清空前一次輸入的東西
		name.clear();
		deadline.clear();
		//CheckPoint 的 deadline 們都是每次重新 new...
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

		mask("資料儲存中...");
		DataCenter.saveCheckList(data);
	}

	@UiHandler("cancel")
	void clickCancel(SelectEvent se) {
		UiCenter.checkListView();
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

	@Override
	protected void enrollWhenVisible() {
		enrollHR(DataCenter.addSaveCheckListEnd(saveHandler));
	}
}
