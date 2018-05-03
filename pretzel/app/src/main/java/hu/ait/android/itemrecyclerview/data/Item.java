package hu.ait.android.itemrecyclerview.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import hu.ait.android.itemrecyclerview.R;

@Entity
public class Item implements Serializable {
    public enum Category {
        GROCERIES(0, R.drawable.groceries_icon),
        CLOTHES(1, R.drawable.clothes_icon),
        ENTERTAINMENT(2, R.drawable.entertainment_icon);

        private int value;
        private int iconId;

        private Category(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static Category fromInt(int value) {
            for (Category p : Category.values()) {
                if (p.value == value) {
                    return p;
                }
            }
            return GROCERIES;
        }
    }


    @PrimaryKey(autoGenerate = true)
    private long itemId;

    private int category;
    private String description;
    private String estimatedPrice;
    private String itemTitle;
    private String createDate;
    private boolean purchased;

    public Item(String itemTitle, String description, String estimatedPrice, String createDate, boolean purchased, int category) {
        this.itemTitle = itemTitle;
        this.description = description;
        this.estimatedPrice = estimatedPrice;
        this.createDate = createDate;
        this.purchased = purchased;
        this.category = category;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEstimatedPrice() {
        return estimatedPrice;
    }

    public String setEstimatedPrice(String estimatedPrice) {
        return estimatedPrice;
    }

    public int getCategory() {return category;}

    public void setCategory(int category) {this.category = category;}

    public Category getCategoryAsEnum() {return Category.fromInt(category);}

}

