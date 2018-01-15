package us.dontcareabout.CheckFlow.client.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.CheckFlow.client.data.SaveTemplateEndEvent.SaveTemplateEndHandler;


public class SaveTemplateEndEvent extends GwtEvent< SaveTemplateEndHandler> {
	public static final Type< SaveTemplateEndHandler> TYPE = new Type< SaveTemplateEndHandler>();

	@Override
	public Type< SaveTemplateEndHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveTemplateEndHandler handler) {
		handler.onSaveTemplateEnd(this);
	}

	public interface SaveTemplateEndHandler extends EventHandler{
		public void onSaveTemplateEnd(SaveTemplateEndEvent event);
	}
}