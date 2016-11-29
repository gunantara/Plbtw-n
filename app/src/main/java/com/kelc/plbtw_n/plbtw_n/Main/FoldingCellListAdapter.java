package com.kelc.plbtw_n.plbtw_n.Main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelc.plbtw_n.plbtw_n.R;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class FoldingCellListAdapter extends ArrayAdapter<modelNews> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private Context context;

    ViewHolder viewHolder;

    public FoldingCellListAdapter(Context context, ArrayList<modelNews> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // get item for selected view
        modelNews item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.title_news = (TextView) cell.findViewById(R.id.title_news);
            viewHolder.content_news = (TextView) cell.findViewById(R.id.content_news);
            viewHolder.image_news = (ImageView) cell.findViewById(R.id.image_news);
            viewHolder.image_news_fold = (ImageView) cell.findViewById(R.id.image_news_fold);
//            viewHolder.read_more = (Button) cell.findViewById(R.id.button_readmore);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }

//            // set custom btn handler for list item from that item
//            if (item.getRequestBtnClickListener() != null) {
//                viewHolder.contentRequestBtn.setOnClickListener(item.getRequestBtnClickListener());
//            } else {
//                // (optionally) add "default" handler if no handler found in item
//                viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
//            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        // bind data from selected element to view through view holder
        viewHolder.title_news.setText(item.getTitle());
        viewHolder.content_news.setText(item.getContent());
//        new NewsTask().execute(item.getImage(), "");
//        viewHolder.image_news.setImageBitmap(item.getImage());
//        Picasso.with(convertView.getContext()).load(item.getImage()).get()
//        try {
            Picasso.with(context).load(item.getImage()).into(viewHolder.image_news);
            Picasso.with(context).load(item.getImage()).into(viewHolder.image_news_fold);

//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//        viewHolder.read_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                foldCell(convertView);
//            }
//        });

        return cell;
    }

    private void foldCell(View view){
        FoldingCell cell = (FoldingCell) view;
        cell.toggle(false);
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }
    private class NewsTask extends AsyncTask<String, Integer, Bitmap> {


        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Drawable myDrawable = getContext().getResources().getDrawable(R.drawable.no_image);
            Bitmap decodedbmp      = ((BitmapDrawable) myDrawable).getBitmap();

            try {
                URL url = new URL(urls[0]);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 5, out);
                decodedbmp = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            }catch (IOException e){
                e.printStackTrace();
            }
            return decodedbmp;
        }

        protected void onPostExecute(Bitmap result) {
            viewHolder.image_news.setImageBitmap(result);
        }
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView image_news;
        ImageView image_news_fold;
        TextView  title_news;
        TextView  content_news;
        Button read_more;
    }
}
