package com.example.bookstore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookstore.Database.Account;
import com.example.bookstore.Database.Comment;
import com.example.bookstore.Database.SQLHelper;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    TextView tvTacGia, tvGiaBan, tvDanhGia, tvMoTa, tvSoLuotDanhGia, tvTheLoai, tvSoTrang, tvTitle;
    RatingBar ratingBar;
    Button btnGuiDanhGia;
    ImageView image, imgBack;
    RecyclerView rcvComment;
    EditText edtComment;
    ImageView btnSend;
    ActionBar actionBar;

    SQLHelper sqlHelper;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_item_book);
        AnhXa();
        actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent1 = getIntent();
        Book book = intent1.getParcelableExtra("book");
        final Account account=intent1.getParcelableExtra("account");


        actionBar.setTitle(book.getTitle());

        sqlHelper=new SQLHelper(MainActivity2.this);
        //lấy ra dữ liệu
        SetupData(book);
        SetUPDataComment( book);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtComment.getText().toString()!=null){
                    sqlHelper.insertComment(account, book, edtComment.getText().toString());
                    SetUPDataComment(book);
                    edtComment.setText("");
                }else
                    Toast.makeText(MainActivity2.this, "viết bình luận, please!", Toast.LENGTH_SHORT).show();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity2.this, MainActivity.class);
                intent1.putExtra("account", account);
                startActivity(intent1);
            }
        });


    }

    private void SetUPDataComment(Book book) {
        List<Comment> list= sqlHelper.GetCommentOfBook(book.getImageLink());
        CommentAdapter commentAdapter=new CommentAdapter(list, MainActivity2.this);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(MainActivity2.this, RecyclerView.VERTICAL, false);
        rcvComment.setLayoutManager(layoutManager);
        rcvComment.setAdapter(commentAdapter);

    }

    private void SetupData(Book book) {
        actionBar.setTitle(book.getTitle());
        tvTitle.setText(book.getTitle());
        tvTacGia.setText("Tác Giả: " + book.getAuthor());
        tvGiaBan.setText(book.getPrice() + " vnđ");
        float star = book.getRateStar();
        star = star == 0 ? (int) book.getRateStar() : (float) book.getRateStar() / 10;
        tvDanhGia.setText(star + "");
        tvMoTa.setText(book.getDescription());
        tvSoLuotDanhGia.setText(book.getNumberOfView() + " lượt đánh giá");
        tvTheLoai.setText(book.getCategory());
        tvSoTrang.setText(book.getNumOfPage() + " trang");
        ratingBar.setNumStars(5);
        ratingBar.setMax(5);
        ratingBar.setStepSize((float) 0.1);
        ratingBar.setRating((float) book.getRateStar() / 10);

        Glide.with(MainActivity2.this).load(book.getImageLink()).into(image);
    }

    private void AnhXa() {
        rcvComment = findViewById(R.id.rcvComment);
        edtComment = findViewById(R.id.edtComment);
        btnSend = findViewById(R.id.btnSend);
        image = findViewById(R.id.image);
        tvTacGia = findViewById(R.id.tvTacGia);
        tvGiaBan = findViewById(R.id.tvgiaBan);
        tvDanhGia = findViewById(R.id.tvdanhGia);
        tvMoTa = findViewById(R.id.tvmoTa);
        tvSoLuotDanhGia = findViewById(R.id.tvSoLuotDanhGia);
        tvTheLoai = findViewById(R.id.tvTheLoai);
        tvSoTrang = findViewById(R.id.tvSoTrang);
        ratingBar = findViewById(R.id.rbRating);
        btnGuiDanhGia = findViewById(R.id.btnGuiDanhGia);
        tvTitle=findViewById(R.id.tvTitle);
        imgBack=findViewById(R.id.imgBack);
    }
}