package hu.ait.android.itemrecyclerview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import hu.ait.android.itemrecyclerview.adapter.ItemRecyclerAdapter;
import hu.ait.android.itemrecyclerview.data.AppDatabase;
import hu.ait.android.itemrecyclerview.data.Item;
import hu.ait.android.itemrecyclerview.touch.ItemTouchHelperCallback;

public class MainActivity extends AppCompatActivity implements ItemCreateAndEditDialog.ItemHandler {

    public static final String KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT";
    private ItemRecyclerAdapter itemRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.btnAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewItemDialog();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerItem);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        initItems(recyclerView);

    }


    public void initItems(final RecyclerView recyclerView) {
        new Thread() {
            @Override
            public void run() {
                final List<Item> items =
                        AppDatabase.getAppDatabase(MainActivity.this).itemDAO().getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemRecyclerAdapter = new ItemRecyclerAdapter(items, MainActivity.this);
                        recyclerView.setAdapter(itemRecyclerAdapter);

                        ItemTouchHelper.Callback callback =
                                new ItemTouchHelperCallback(itemRecyclerAdapter);
                        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                        touchHelper.attachToRecyclerView(recyclerView);
                    }
                });
            }
        }.start();
    }


    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.getInspiration:
                loadInspirationalLink();
                return true;
            case R.id.action_add_from_toolbar:
                showNewItemDialog();
                return true;
            case R.id.action_delete_from_toolbar:
                deleteAll();
                return true;
        }
        return false;
    }

    private void loadInspirationalLink() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://www.pinterest.com"));
        startActivity(intent);
    }


    private void showNewItemDialog() {
        new ItemCreateAndEditDialog().show(getSupportFragmentManager(), "ItemCreateAndEditDialog");
    }


    @Override
    public void onNewItemCreated(final Item item) {
        new Thread() {
            @Override
            public void run() {
                long id = AppDatabase.getAppDatabase(MainActivity.this).itemDAO().insertItem(item);
                item.setItemId(id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemRecyclerAdapter.addItem(item);
                    }
                });
            }
        }.start();
    }


    @Override
    public void onItemUpdated(final Item item) {
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(MainActivity.this).itemDAO().update(item);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemRecyclerAdapter.updateItem(item);
                    }
                });
            }
        }.start();
    }

    public void editItem(Item item) {
        ItemCreateAndEditDialog editDialog = new ItemCreateAndEditDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ITEM_TO_EDIT, item);
        editDialog.setArguments(bundle);


        editDialog.show(getSupportFragmentManager(), "EditDialog");
    }


    public void deleteAll() {
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(MainActivity.this).itemDAO().deleteAll(itemRecyclerAdapter.getItemList());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemRecyclerAdapter.clearList();
                    }
                });
            }
        }.start();
    }
}
