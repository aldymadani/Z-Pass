package com.example.zpass.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zpass.EditAccountActivity;
import com.example.zpass.R;
import com.example.zpass.model.Account;
import com.example.zpass.utils.IntentNameExtra;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class AccountAdapter extends FirestoreRecyclerAdapter<Account, AccountAdapter.AccountHolder> {

    private Context context;

    public AccountAdapter(@NonNull FirestoreRecyclerOptions options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull AccountAdapter.AccountHolder holder, int position, @NonNull Account model) {
        holder.accountName.setText(model.getName());
        Picasso.get().load(model.getImageUrl()).error(R.drawable.ic_baseline_error_24).into(holder.accountImage);
        holder.accountCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditAccountActivity.class);
                intent.putExtra(IntentNameExtra.ACCOUNT, model);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item,
                parent, false);
        return new AccountHolder(v);
    }

    class AccountHolder extends RecyclerView.ViewHolder {
        ImageView accountImage;
        TextView accountName;
        CardView accountCardView;

        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            accountImage = itemView.findViewById(R.id.ImageViewAccount);
            accountName = itemView.findViewById(R.id.TextViewAccountNameList);
            accountCardView = itemView.findViewById(R.id.CardViewAccount);

        }
    }
}
