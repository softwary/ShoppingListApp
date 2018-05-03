package hu.ait.android.itemrecyclerview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;

import hu.ait.android.itemrecyclerview.data.Item;

public class ItemCreateAndEditDialog extends DialogFragment {

    public interface ItemHandler {
        public void onNewItemCreated(Item item);

        public void onItemUpdated(Item item);
    }


    private ItemHandler itemHandler;
    private Spinner spinnerCategory;
    private EditText etName;
    private EditText etDescription;
    private EditText etEstimatedPrice;
    private CheckBox cbPurchased;


    private TextView tvDate;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvEstimatedPrice;
    private Button btnEdit;
    public ImageView ivCategory;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ItemHandler) {
            itemHandler = (ItemHandler)context;
        } else {
            throw new RuntimeException(
                    "The Activity does not implement the ItemHandler interface");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_new_item, null);

        spinnerCategory = (Spinner) rootView.findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);


        etName = (EditText) rootView.findViewById(R.id.etName);
        etDescription = (EditText) rootView.findViewById(R.id.etDescription);
        etEstimatedPrice = (EditText) rootView.findViewById(R.id.etEstimatedPrice);


        if (getArguments() != null &&
                getArguments().containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
            Item itemToEdit = (Item) getArguments().getSerializable(MainActivity.KEY_ITEM_TO_EDIT);
            etName.setText(itemToEdit.getItemTitle());
            etDescription.setText(itemToEdit.getDescription());
            etEstimatedPrice.setText(itemToEdit.getEstimatedPrice());
            spinnerCategory.setSelection(itemToEdit.getCategoryAsEnum().getValue());
        }

        builder.setView(rootView);

        builder.setPositiveButton("Save Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create();

    }


    @Override
    public void onResume()
    {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (!TextUtils.isEmpty(etName.getText())) {
                        if (getArguments() != null &&
                                getArguments().containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
                            Item itemToEdit = (Item) getArguments().getSerializable(MainActivity.KEY_ITEM_TO_EDIT);
                            itemToEdit.setItemTitle(etName.getText().toString());
                            itemToEdit.setDescription(etDescription.getText().toString());
                            itemToEdit.setEstimatedPrice(etEstimatedPrice.getText().toString());
                            itemToEdit.setCategory(spinnerCategory.getSelectedItemPosition());

                            itemHandler.onItemUpdated(itemToEdit);
                        } else {
                            Item item = new Item(etName.getText().toString(),
                                    etDescription.getText().toString(),
                                    etEstimatedPrice.getText().toString(),
                                    new SimpleDateFormat("MM/dd hh:mma").format(
                                            new Date(System.currentTimeMillis())),
                                    false,
                                    spinnerCategory.getSelectedItemPosition()
                            );

                            itemHandler.onNewItemCreated(item);
                        }

                        d.dismiss();

                    } else {
                        etName.setError(getString(R.string.null_error));
                    }

                }
            });
        }
    }


}
