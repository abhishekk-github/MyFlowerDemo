package com.example.myflower;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by abhishek on 14/11/16.
 */

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.ViewHolder> {

  private List<Model> flowerList;
  private FlowerHome mActivity;
  private ViewHolder mViewHolder;
  private View view;
  private List<Model> flowerListTemp;
  DBHelper dbHelper;

  public FlowerAdapter(Activity activity, List<Model> flowerList) {
    this.flowerList = flowerList;
    mActivity = (FlowerHome) activity;
    dbHelper = new DBHelper(activity);
    flowerListTemp = new ArrayList<>();
    flowerListTemp.addAll(flowerList);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    view = LayoutInflater.from(mActivity).inflate(R.layout.flower_item_layout, parent, false);

    mViewHolder = new ViewHolder(view);

    return mViewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textView.setText(flowerList.get(position).getName());
    Picasso.with(mActivity)
        .load(flowerList.get(position).getUrl())
        .placeholder(R.drawable.launcher)
        .error(R.drawable.launcher)
        .into(holder.mImageView);

    if(flowerList.get(position).getBookmark()){
      holder.mStarImageView.setImageResource(R.drawable.popup_star_icon_on);
    }
    else{
      holder.mStarImageView.setImageResource(R.drawable.popup_star_icon_off);
    }

    final int p = position;
    final ViewHolder h = holder;
    holder.mShareImageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sharePainText(p);
      }
    });
    holder.mImageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showImageDialog(p);
        dbHelper.getAllID();
      }
    });

    holder.mStarImageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        starImageDialog(p,h);
      }
    });
  }


  @Override
  public int getItemCount() {
    return flowerList.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView textView;
    ImageView mImageView;
    ImageView mShareImageView;
    ImageButton mStarImageView;

    public ViewHolder(View v) {

      super(v);

      textView = (TextView) v.findViewById(R.id.flowername_tv);
      mImageView = (ImageView) v.findViewById(R.id.flower_image_view);
      mShareImageView = (ImageView) v.findViewById(R.id.imageView_share);
      mStarImageView = (ImageButton) v.findViewById(R.id.bookmark_imageButton);
    }
  }

  public void filter(String string) {
    flowerList.clear();
    if (string.length() == 0) {
      flowerList.addAll(flowerListTemp);
    } else {
      for (Model flower : flowerListTemp) {
        if (flower.getName().toLowerCase(Locale.getDefault())
            .contains(string)) {
          flowerList.add(flower);
        }
      }
    }
    mActivity.isListEmpty(flowerList.isEmpty());
    notifyDataSetChanged();
  }

  private void showImageDialog(int position) {

    // custom dialog
    final Dialog dialog = new Dialog(mActivity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.image_layout_dialog);
    ImageView imageView = (ImageView) dialog.findViewById(R.id.full_flower_imageView);
    TextView textView  = (TextView) dialog.findViewById(R.id.full_flower_textView);

    Picasso.with(mActivity)
        .load(flowerList.get(position).getUrl())
        .placeholder(R.drawable.launcher)
        .error(R.drawable.launcher)
        .into(imageView);
    textView.setText(flowerList.get(position).getName());
    dialog.show();
  }

  private void sharePainText(int position) {
    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, flowerList.get(position).toString());
    mActivity.startActivity(Intent.createChooser(sharingIntent, "My Flower App"));
  }

  private void starImageDialog(int p, ViewHolder holder) {
    if(flowerList.get(p).getBookmark()){
      dbHelper.deleteBookmark(flowerList.get(p).getId());
      holder.mStarImageView.setImageResource(R.drawable.popup_star_icon_off);
      flowerList.get(p).setBookmark(false);
    }
    else{
      dbHelper.insertBookmark(flowerList.get(p).getId(),true);
      holder.mStarImageView.setImageResource(R.drawable.popup_star_icon_on);
      flowerList.get(p).setBookmark(true);
    }
  }
}
