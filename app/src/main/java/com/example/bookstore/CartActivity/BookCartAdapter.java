package com.example.bookstore.CartActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.Book;
import com.example.bookstore.BookAdapter;
import com.example.bookstore.OnClickItemBook;
import com.example.bookstore.R;

import java.util.List;

public class BookCartAdapter extends RecyclerView.Adapter<BookCartAdapter.ViewHolder> {
    List<Book> list;
    Context context;
    IOnClick iOnClick;

    public BookCartAdapter(List<Book> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public IOnClick getiOnClick() {
        return iOnClick;
    }

    public void setiOnClick(IOnClick iOnClick) {
        this.iOnClick = iOnClick;
    }

    @NonNull
    @Override
    public BookCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_item_book_on_cart, parent, false);
        BookCartAdapter.ViewHolder viewHoldernew = new BookCartAdapter.ViewHolder(view);
        return viewHoldernew;
    }

    @Override
    public void onBindViewHolder(@NonNull BookCartAdapter.ViewHolder holder, int position) {
        Book book = list.get(position);
        Glide.with(context).load(book.getImageLink()).into(holder.imgBookCart);
        holder.tvDanhGiaBookCart.setText(book.getRateStar()+"");
        holder.tvPrBookCart.setText(book.getPrice()+" VNĐ");
        holder.tvTitleBookCart.setText(book.getTitle());
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClick.iOnClickRemove(book, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBookCart;
        TextView tvTitleBookCart, tvPrBookCart, tvDanhGiaBookCart;
        Button btnRemove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBookCart=itemView.findViewById(R.id.imgBookCart);
            tvTitleBookCart=itemView.findViewById(R.id.tvTitleBookCart);
            tvPrBookCart=itemView.findViewById(R.id.tvPrBookCart);
            btnRemove= itemView.findViewById(R.id.btnRemove);
            tvDanhGiaBookCart= itemView.findViewById(R.id.tvDanhGiaBookCart);
        }
    }
    public void upDateData(List<Book> books){
        list=books;
        notifyDataSetChanged();
    }
}

