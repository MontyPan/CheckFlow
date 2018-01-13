package us.dontcareabout.CheckFlow.client.component;

import java.util.ArrayList;
import java.util.Date;

import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;

import us.dontcareabout.CheckFlow.client.DateUtil;
import us.dontcareabout.CheckFlow.client.Palette;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;
import us.dontcareabout.gxt.client.draw.Cursor;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;

public class CheckListPanel extends LayerContainer {
	private static final int ITEM_HEIGHT = 50;

	private NotNowLayer prevLayer = new NotNowLayer(Palette.BLUE);
	private NowLayer nowLayer = new NowLayer();
	private NotNowLayer nextLayer = new NotNowLayer(Palette.RED[1]);

	private CheckFlow checkList;

	public CheckListPanel() {
		prevLayer.setLX(50);
		prevLayer.setLY(0);

		nowLayer.setLX(5);
		nowLayer.setLY(40);
		nowLayer.setLZIndex(50);

		nextLayer.setLX(50);
		nextLayer.setLY(430);

		this.addLayer(prevLayer);
		this.addLayer(nowLayer);
		this.addLayer(nextLayer);
	}

	public void setData(CheckFlow cf) {
		checkList = cf;

		int index = checkList.getUnfinishPointIndex();
		prevLayer.setData(checkList.getPointList().get(index - 1));
		nowLayer.setData(checkList.getPointList().get(index));
		nextLayer.setData(checkList.getPointList().get(index + 1));

		redrawSurfaceForced();	//zIndex 靈異現象的萬惡解
	}

	@Override
	protected void onResize(int width, int height) {
		int w = width - 50;
		prevLayer.onResize(w, 60);
		nowLayer.onResize(w, 400);
		nextLayer.onResize(w, 60);
		super.onResize(width, height);
	}

	class CheckPointLayer extends LayerSprite {
		private CheckPoint data;
		private LTextSprite text = new LTextSprite();
		private LTextSprite reciprocal = new LTextSprite();

		public CheckPointLayer() {
			add(text);
			add(reciprocal);
		}

		public void setData(CheckPoint checkPoint) {
			this.data = checkPoint;
			getText().setText(checkPoint.getName());
			int diff = DateUtil.daysBetween(checkPoint.getDeadline(), new Date());
			String reciprocalHeader = diff < 0 ? "逾 " : "剩 ";
			getReciprocal().setText(reciprocalHeader + Math.abs(diff) + " 天");
			adjustMember();
		}

		@Override
		protected void adjustMember() {
			getReciprocal().setLX(
				getWidth() -
				(getReciprocal().getText().length() + 2) *	//有兩個中文字補兩個 unit
				getReciprocal().getFontSize() / 2
				- 10	//右邊的 margin
			);
		}

		public LTextSprite getText() {
			return text;
		}

		public LTextSprite getReciprocal() {
			return reciprocal;
		}

		public CheckPoint getData() {
			return data;
		}
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
				cil.setLX(i < 5 ? 15 : w + 25);
				cil.setLY((i % 5) * (ITEM_HEIGHT + 10) + 90);
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
				add(text);

				refresh();
			}

			public LTextSprite getText() {
				return text;
			}

			private void refresh() {
				setBgColor(data.isFinish() ? Palette.PURPLE[2] : Palette.PURPLE[0]);
				text.setFill(data.isFinish() ? RGB.BLACK : RGB.WHITE);
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