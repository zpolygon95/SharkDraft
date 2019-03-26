package io.zjp.sharkdraft;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {
    private String[] data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
        }
    }

    public RecyclerListAdapter(String[] dataset) {
        data = dataset;
    }

    @Override
    public RecyclerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()
        ).inflate(R.layout.draft_row, parent, false);

        //...

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.length;
        else
            return 0;
    }
}
