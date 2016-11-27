package com.kelc.plbtw_n.plbtw_n.Main.Olahraga;

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

import com.kelc.plbtw_n.plbtw_n.Main.Top.FoldingCellListAdapter;
import com.kelc.plbtw_n.plbtw_n.Main.Top.modelTopNews;
import com.kelc.plbtw_n.plbtw_n.R;
import com.ramotion.foldingcell.FoldingCell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by 12070 on 11/26/2016.
 */

public class FoldingCellListAdapterOlahraga extends ArrayAdapter<modelOlahraga> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    FoldingCellListAdapterOlahraga.ViewHolder viewHolder;

    public FoldingCellListAdapterOlahraga(Context context, ArrayList<modelOlahraga> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        modelOlahraga item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        if (cell == null) {
            viewHolder = new FoldingCellListAdapterOlahraga.ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.title_news = (TextView) cell.findViewById(R.id.title_news);
            viewHolder.content_news = (TextView) cell.findViewById(R.id.content_news);
            viewHolder.image_news = (ImageView) cell.findViewById(R.id.image_news);
            viewHolder.read_more = (Button) cell.findViewById(R.id.button_readmore);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (FoldingCellListAdapterOlahraga.ViewHolder) cell.getTag();
        }

        // bind data from selected element to view through view holder
        viewHolder.title_news.setText(item.getTitle());
        viewHolder.content_news.setText(item.getContent());
        new FoldingCellListAdapterOlahraga.NewsOlahragaTask().execute(item.getImage(), "");


        viewHolder.read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }
    private class NewsOlahragaTask extends AsyncTask<String, Integer, Bitmap> {


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
                bmp.compress(Bitmap.CompressFormat.PNG, 10, out);
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

    private static class ViewHolder {
        ImageView image_news;
        TextView  title_news;
        TextView  content_news;
        Button read_more;
    }
}
