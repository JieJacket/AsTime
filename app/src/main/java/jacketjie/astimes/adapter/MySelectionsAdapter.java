package jacketjie.astimes.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.model.Selection;
import jacketjie.astimes.utils.interfaces.RecyclerOnItemClickListener;
import jacketjie.astimes.utils.interfaces.RecyclerOnItemLongClickListener;

/**
 * Created by wujie on 2015/12/12.
 */
public class MySelectionsAdapter extends RecyclerView.Adapter<MySelectionsAdapter.SelectionViewHolder> {
    private List<Selection> mDatas;
    private Context context;
    private LayoutInflater inflater;
    private RecyclerOnItemClickListener onItemClickListener;
    private RecyclerOnItemLongClickListener onItemLongClickListener;

    public RecyclerOnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(RecyclerOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerOnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(RecyclerOnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public MySelectionsAdapter(Context context, List<Selection> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public SelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_selections_item, parent, false);
        SelectionViewHolder holder = new SelectionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SelectionViewHolder holder, int position) {
        Selection selection = mDatas.get(position);
        holder.textView.setText(selection.getTitle());

        if (selection.getResId() != 0) {
            Drawable d = context.getResources().getDrawable(selection.getResId());
            d.setBounds(0, 0, d.getMinimumWidth()*2/3, d.getMinimumHeight()*2/3);
            holder.textView.setCompoundDrawables(d, null, null, null);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class SelectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textView;

        public SelectionViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.id_selections_item);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemListener(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemListener(v, getAdapterPosition());
            }
            return true;
        }
    }
}

