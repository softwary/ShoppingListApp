package hu.ait.android.itemrecyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import hu.ait.android.itemrecyclerview.MainActivity;
import hu.ait.android.itemrecyclerview.R;
import hu.ait.android.itemrecyclerview.data.AppDatabase;
import hu.ait.android.itemrecyclerview.data.Item;
import hu.ait.android.itemrecyclerview.touch.ItemTouchHelperAdapter;

public class ItemRecyclerAdapter
        extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {


    public List<Item> getItemList() {
        return itemList;
    }

    public void clearList() {
        itemList.clear();
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox cbPurchased;
        public TextView tvTitle;
        public ImageView ivCategory;
        public TextView tvEstimatedPrice;
        public TextView tvDescription;
        public TextView tvDate;
        public Button btnEdit;


        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            cbPurchased = itemView.findViewById(R.id.cbPurchased);
            tvEstimatedPrice = itemView.findViewById(R.id.tvEstimatedPrice);
            ivCategory = itemView.findViewById(R.id.ivCategory);
        }
    }

    private List<Item> itemList;
    private Context context;
    //private int lastPosition = -1;

    public ItemRecyclerAdapter(List<Item> items, Context context) {
        itemList = items;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View viewRow = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_row, parent, false);
        return new ViewHolder(viewRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,
                                 int position) {


        holder.tvTitle.setText(
                itemList.get(holder.getAdapterPosition()).getItemTitle()
        );
//this causes a null pointer exception
        holder.ivCategory.setImageResource(itemList.get(position).getCategoryAsEnum().getIconId());


        holder.tvEstimatedPrice.setText("$" +
                itemList.get(holder.getAdapterPosition()).getEstimatedPrice());

        holder.tvDescription.setText(
                itemList.get(holder.getAdapterPosition()).getDescription()
        );


        holder.cbPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Item item = itemList.get(holder.getAdapterPosition());
                item.setPurchased(holder.cbPurchased.isChecked());
                notifyItemChanged(holder.getAdapterPosition());
                new Thread() {
                    @Override
                    public void run() {
                        AppDatabase.getAppDatabase(context).itemDAO().update(item);
                    }
                }.start();
            }
        });
        
        holder.tvDate.setText(
                itemList.get(
                        holder.getAdapterPosition()).getCreateDate());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).editItem(
                        itemList.get(holder.getAdapterPosition()));
            }
        });
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(Item item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(final int position) {
        final Item itemToDelete = itemList.get(position);
        itemList.remove(itemToDelete);
        notifyItemRemoved(position);
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(context).itemDAO().delete(
                        itemToDelete);
            }
        }.start();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }

        //notifyDataSetChanged();
        notifyItemMoved(fromPosition, toPosition);
    }

    public void updateItem(Item item) {
        int editPos = findItemIndexByItemId(item.getItemId());
        itemList.set(editPos,item);
        notifyItemChanged(editPos);
    }

    private int findItemIndexByItemId(long itemId) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getItemId() == itemId) {
                return i;
            }
        }

        return -1;
    }




}
