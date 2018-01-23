package us.dontcareabout.CheckFlow.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	public static final Resources instance = GWT.create(Resources.class);

	public ImageResource arrowDown();
}
