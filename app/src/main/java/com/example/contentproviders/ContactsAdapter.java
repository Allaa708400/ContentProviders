package com.example.contentproviders;

import android.app.DirectAction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contentproviders.databinding.CustomContactItemBinding;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

  private ArrayList<ContactItem> contactItems;

    public ContactsAdapter(ArrayList<ContactItem> contactItems) {
        this.contactItems = contactItems;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CustomContactItemBinding binding = CustomContactItemBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false);

        ContactViewHolder holder = new ContactViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        ContactItem contactItem = contactItems.get(position);

       if (contactItem.getEmail() != null) {
           holder.binding.contactItemTvEmail.setText(contactItem.getEmail());

       }

        if (contactItem.getName() != null) {
            holder.binding.contactItemTvName.setText(contactItem.getName());

        }

        if (contactItem.getNumber() != null) {
            holder.binding.contactItemTvNumber.setText(contactItem.getNumber());

        }
        if (contactItem.getOtherDetails() != null) {
            holder.binding.contactItemTvOtherDetails.setText(contactItem.getOtherDetails());

        }





        Bitmap image = null;
        if (contactItem.getPhoto() != null  &&  !contactItem.getPhoto().equals("")){

            image = BitmapFactory.decodeFile(contactItem.getPhoto());



            if (image != null) {
                holder.binding.contactItemIv.setImageBitmap(image);

            }
            else {
                image = BitmapFactory.decodeResource(holder.binding.getRoot().getContext().getResources()
                ,R.drawable.baseline_account_circle_24);
                holder.binding.contactItemIv.setImageBitmap(image);


            }

        }else {

            image = BitmapFactory.decodeResource(holder.binding.getRoot().getContext().getResources()
                    ,R.drawable.baseline_account_circle_24);
            holder.binding.contactItemIv.setImageBitmap(image);

        }


    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{

        CustomContactItemBinding binding;


        public ContactViewHolder(@NonNull CustomContactItemBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;


        }
    }
}
