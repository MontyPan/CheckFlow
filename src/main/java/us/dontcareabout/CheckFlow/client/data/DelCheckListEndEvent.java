package us.dontcareabout.CheckFlow.client.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.CheckFlow.client.data.DelCheckListEndEvent.DelCheckListEndHandler;

public class DelCheckListEndEvent extends GwtEvent< DelCheckListEndHandler> {
	public static final Type< DelCheckListEndHandler> TYPE = new Type< DelCheckListEndHandler>();

	@Override
	public Type< DelCheckListEndHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DelCheckListEndHandler handler) {
		handler.onDelCheckListEnd(this);
	}

	public interface DelCheckListEndHandler extends EventHandler{
		public void onDelCheckListEnd(DelCheckListEndEvent event);
	}
}