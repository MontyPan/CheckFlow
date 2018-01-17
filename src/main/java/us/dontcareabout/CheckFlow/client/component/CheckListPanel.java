package us.dontcareabout.CheckFlow.client.component;

import java.util.ArrayList;

import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;

import us.dontcareabout.CheckFlow.client.Palette;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;
import us.dontcareabout.gxt.client.draw.Cursor;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.Layer;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;

public class CheckListPanel extends LayerContainer {
	private static final int ITEM_HEIGHT = 50;
	private static final int H_UNIT = 60;
	private static final int MARGIN = 5;

	public void setData(CheckFlow cf) {
		this.clear();
		int height = 0;

		for (CheckPoint cp : cf.getPointList()) {
			if (cp != cf.getUnfinishPoint()) {
				NotNowLayer layer = new NotNowLayer(cp.isFinish() ? Palette.BLUE : Palette.RED[1]);
				layer.setData(cp);
				layer.setLX(MARGIN);
				layer.setLY(height + MARGIN);
				layer.setHeight(H_UNIT);
				this.addLayer(layer);
				height += H_UNIT + MARGIN;
			} else {
				int nowHeight = 95 + (int)(Math.ceil(cp.getItemList().size() / 2.0)) * (ITEM_HEIGHT + MARGIN);
				NowLayer layer = new NowLayer();
				layer.setData(cp);
				layer.setLY(height + MARGIN);
				layer.setLX(MARGIN);
				layer.setHeight(nowHeight);
				this.addLayer(layer);
				height += nowHeight + MARGIN;
			}
		}

		setHeight(height);
	}

	@Override
	protected void onResize(int width, int height) {
		for (Layer layer : getLayers()) {
			LayerSprite ls = (LayerSprite)layer;
			ls.onResize(width - MARGIN * 2, ls.getHeight());
		}

		super.onResize(width, height);
	}

	class NowLayer extends CheckPointLayer {
		private ArrayList<CheckItemLayer> itemLayers = new ArrayList<>();

		public NowLayer() {
			setBgColor(Palette.AMBER[1]);
			setBgOpacity(0.9);

			getText().setLX(12);
			getText().setLY(8);
			getText().setFontSize(60);

			getReciprocal().setLX(600);
			getReciprocal().setLY(15);
			getReciprocal().setFontSize(40);
			getReciprocal().setCursor(Cursor.POINTER);
		}

		@Override
		public void setData(CheckPoint checkPoint) {
			super.setData(checkPoint);

			//清空 CheckItemLayer 們
			for (CheckItemLayer layer : itemLayers) {
				remove(layer);
			}

			itemLayers.clear();
			////

			for (int i = 0; i < checkPoint.getItemList().size(); i++) {
				CheckItemLayer cil = new CheckItemLayer(checkPoint.getItemList().get(i));
				cil.setLZIndex(100);
				add(cil);
				itemLayers.add(cil);
				//還沒有真正掛到 DrawComponent 上去，所以補作
				cil.deploy(CheckListPanel.this);
			}

			adjustMember();
		}

		@Override
		protected void adjustMember() {
			super.adjustMember();
			double w = (getWidth() - 40) / 2; //左右各留 15、中間留 10

			for (int i = 0; i < itemLayers.size(); i++) {
				CheckItemLayer cil = itemLayers.get(i);
				cil.onResize(w, ITEM_HEIGHT);
				cil.setLX(i % 2 == 0 ? 15 : w + 25);
				cil.setLY(i / 2 * (ITEM_HEIGHT + MARGIN) + 90);
			}
		}

		class CheckItemLayer extends LayerSprite {
			private CheckPoint data;
			private LTextSprite text = new LTextSprite();

			public CheckItemLayer(CheckPoint checkItem) {
				data = checkItem;

				setCursor(Cursor.POINTER);
				setBgRadius(10);

				addSpriteSelectionHandler(new SpriteSelectionHandler() {
					@Override
					public void onSpriteSelect(SpriteSelectionEvent event) {
						data.setFinish(!data.isFinish());
						refresh();
					}
				});

				text.setFontSize(20);
				text.setLX(10);
				text.setLY(11);
				text.setText(data.getName());
				text.setFill(RGB.WHITE);
				add(text);

				refresh();
			}

			public LTextSprite getText() {
				return text;
			}

			private void refresh() {
				setBgColor(data.isFinish() ? Palette.BLUE : Palette.RED[1]);
			}
		}
	}

	class NotNowLayer extends CheckPointLayer {
		public NotNowLayer(Color bgColor) {
			setBgColor(bgColor);

			getText().setLX(10);
			getText().setLY(10);
			getText().setFontSize(30);
			getText().setFill(RGB.WHITE);

			getReciprocal().setLY(15);
			getReciprocal().setFontSize(20);
			getReciprocal().setFill(RGB.WHITE);
		}
	}
}