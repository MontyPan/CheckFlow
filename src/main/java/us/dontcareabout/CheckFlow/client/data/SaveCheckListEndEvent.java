package us.dontcareabout.CheckFlow.client.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.CheckFlow.client.data.SaveCheckListEndEvent.SaveCheckListEndHandler;

public class SaveCheckListEndEvent extends GwtEvent< SaveCheckListEndHandler> {
	public static final Type< SaveCheckListEndHandler> TYPE = new Type< SaveCheckListEndHandler>();

	@Override
	public Type< SaveCheckListEndHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveCheckListEndHandler handler) {
		handler.onSaveCheckListEnd(this);
	}

	public interface SaveCheckListEndHandler extends EventHandler{
		public void onSaveCheckListEnd(SaveCheckListEndEvent event);
	}
}