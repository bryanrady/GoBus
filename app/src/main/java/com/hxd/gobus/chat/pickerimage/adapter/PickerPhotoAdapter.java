package com.hxd.gobus.chat.pickerimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hxd.gobus.R;
import com.hxd.gobus.chat.pickerimage.fragment.PickerImageFragment;
import com.hxd.gobus.chat.pickerimage.model.PhotoInfo;
import com.hxd.gobus.chat.pickerimage.utils.PickerImageLoadTool;
import com.hxd.gobus.chat.pickerimage.utils.RotateImageViewAware;
import com.hxd.gobus.chat.pickerimage.utils.ScreenUtil;
import com.hxd.gobus.chat.pickerimage.utils.ThumbnailsUtil;

import java.util.List;


public class PickerPhotoAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<PhotoInfo> list;
	private GridView gridView;
	private int hasSelect = 0;
	private int width = ScreenUtil.screenWidth / 4;
	private boolean isMutiMode;
	private int maxSelectSize;
	
	private PickerImageFragment.OnPhotoSelectClickListener onPhotoSelectClickListener;

	public PickerPhotoAdapter(Context context, List<PhotoInfo> list, GridView gridView,
                              boolean isMutiMode, int hasSelect, int maxSelectSize) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.gridView = gridView;
		this.isMutiMode = isMutiMode;
		this.hasSelect = hasSelect;
		this.maxSelectSize = maxSelectSize;
		
		if (onPhotoSelectClickListener == null) {
			onPhotoSelectClickListener = (PickerImageFragment.OnPhotoSelectClickListener) context;
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void updateSelectNum(int hasSelectNum){
		this.hasSelect = hasSelectNum;
	}

	public void refreshView(int index) {
		int visiblePos = gridView.getFirstVisiblePosition();
		View view = gridView.getChildAt(index - visiblePos);
		ViewHolder holder = (ViewHolder) view.getTag();
		if (list.get(index).isChoose()) {
			holder.select.setImageResource(R.mipmap.picker_image_selected);
		} else {
			holder.select.setImageResource(R.mipmap.picker_image_normal);
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {			
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.picker_photo_grid_item, null);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.picker_photo_grid_item_img);
			viewHolder.select = (ImageView) convertView.findViewById(R.id.picker_photo_grid_item_select);
			viewHolder.selectHotPot = (RelativeLayout) convertView.findViewById(R.id.picker_photo_grid_item_select_hotpot);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(isMutiMode){
			viewHolder.selectHotPot.setVisibility(View.VISIBLE);
		}else{
			viewHolder.selectHotPot.setVisibility(View.GONE);
		}
		
		LayoutParams hotpotLayoutParams = viewHolder.selectHotPot.getLayoutParams();
		hotpotLayoutParams.width = width / 2;
		hotpotLayoutParams.height = width / 2;
		viewHolder.selectHotPot.setLayoutParams(hotpotLayoutParams);
		viewHolder.selectHotPot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PhotoInfo photo = list.get(position);
				if (photo.isChoose()) {
					photo.setChoose(false);
					hasSelect--;
				} else if (hasSelect < maxSelectSize) {
					photo.setChoose(true);
					hasSelect++;
				} else {
					Toast.makeText(mContext, String.format(mContext.getResources().getString(
							R.string.picker_image_exceed_max_image_select), maxSelectSize), Toast.LENGTH_SHORT).show();
					return;
				}
				
				refreshView(position);
				onPhotoSelectClickListener.onPhotoSelectClick(photo);
			}
		});
			
		if (list.get(position).isChoose()) {
			viewHolder.select.setImageResource(R.mipmap.picker_image_selected);
		} else {
			viewHolder.select.setImageResource(R.mipmap.picker_image_normal);
		}
		
		LayoutParams imageLayoutParams = viewHolder.image.getLayoutParams();
		imageLayoutParams.width = width;
		imageLayoutParams.height = width;
		viewHolder.image.setLayoutParams(imageLayoutParams);
		
		final PhotoInfo photoInfo = list.get(position);
		if (photoInfo != null) {
			String thumbPath = ThumbnailsUtil.getThumbnailWithImageID(photoInfo.getImageId(), photoInfo.getFilePath());
			PickerImageLoadTool.disPlay(thumbPath, new RotateImageViewAware(viewHolder.image, photoInfo.getAbsolutePath()),
					R.mipmap.image_default);
		}
		
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
		public ImageView select;
		public RelativeLayout selectHotPot;
	}
}
