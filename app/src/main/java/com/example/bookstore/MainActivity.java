package com.example.bookstore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.ParcelFormatException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookstore.CartActivity.BookCartAdapter;
import com.example.bookstore.CartActivity.IOnClick;
import com.example.bookstore.Constant.dataBook;
import com.example.bookstore.Database.Account;
import com.example.bookstore.Database.SQLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String URL_API_BOOK = "https://bookshopb.herokuapp.com/api/books";
    dataBook dataBook= new dataBook();
    String arrayBook;
    List<Book> listBook = new ArrayList<>();
    BookAdapter adapter;

    RecyclerView recyclerView;
    FrameLayout layoutCart;
    TextView tvCountCart;
    Account account;
    EditText edtSearch;

    List<Book> listBooksCart;
    BookCartAdapter bookCartAdapter;

    SQLHelper sqlHelper= new SQLHelper(MainActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        recyclerView = findViewById(R.id.rcv);
        layoutCart= findViewById(R.id.rltCart);
        tvCountCart= findViewById(R.id.tvCountCart);
        edtSearch= findViewById(R.id.edtSearch);

        Intent intent1=getIntent();

        account=intent1.getParcelableExtra("account");
        if(account==null){
            account= intent1.getParcelableExtra("acc");
        }

        actionBar.setTitle("Welcome "+ account.getUsername());

        layoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_cart);

                Window window = dialog.getWindow();
                if (window == null) {
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams windowAtributes = window.getAttributes();
                windowAtributes.gravity = Gravity.CENTER_VERTICAL;
                window.setAttributes(windowAtributes);

                dialog.setCancelable(true);

                RecyclerView revCart= dialog.findViewById(R.id.revCart);
                TextView tvTongTien= dialog.findViewById(R.id.tvTongTien);
                Button btnThanhToan= dialog.findViewById(R.id.btnThanhToan);
                Button btnXoaHetCart= dialog.findViewById(R.id.btnXoaHetCart);
                TextView tvEmpty= dialog.findViewById(R.id.tvEmpty);

                listBooksCart= sqlHelper.getAllCart();
                if (listBooksCart.size()==0){
                    tvEmpty.setVisibility(View.VISIBLE);
                }else {
                    long s=0;
                    for (int i=0; i<listBooksCart.size(); i++){
                        s+=listBooksCart.get(i).getPrice();
                    }
                    tvTongTien.setText("Tổng tiền: "+ s+" VNĐ");
                    bookCartAdapter = new BookCartAdapter(listBooksCart, MainActivity.this);
                    RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                    revCart.setAdapter(bookCartAdapter);
                    revCart.setLayoutManager(layoutManager);
                    bookCartAdapter.setiOnClick(new IOnClick() {
                        @Override
                        public void iOnClickRemove(Book book, int i) {
                            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("XÓA KHỎI GIỎ HÀNG")
                                    .setMessage("Xác nhận xóa sách khỏi giỏ hàng: ")
                                    .setPositiveButton("ĐỒNG Ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sqlHelper.deleteFromCart(book.getId());
                                            listBooksCart.remove(i);
                                            bookCartAdapter.upDateData(listBooksCart);
                                            long s=0;
                                            for (int i=0; i<listBooksCart.size(); i++){
                                                s+=listBooksCart.get(i).getPrice();
                                            }
                                            int c = Integer.parseInt(tvCountCart.getText().toString().trim()) -1;
                                            if (c<0){
                                                c=0;
                                            }
                                            tvCountCart.setText(c +"");
                                            tvTongTien.setText("Tổng tiền: "+ s+" VNĐ");
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).create();
                            dialog.show();
                        }
                    });
                    btnXoaHetCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("XÓA TOÀN BỘ")
                                    .setMessage("Xác nhận xóa toàn bộ sách")
                                    .setPositiveButton("ĐỒNG Ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sqlHelper.deleteAllCart();
                                            listBooksCart.clear();
                                            bookCartAdapter.upDateData(listBooksCart);
                                            long s=0;
                                            for (int i=0; i<listBooksCart.size(); i++){
                                                s+=listBooksCart.get(i).getPrice();
                                            }
                                            tvTongTien.setText("Tổng tiền: "+ s+" VNĐ");
                                            tvCountCart.setText("0");
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).create();
                            dialog.show();
                        }
                    });

                    btnThanhToan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("THANH TOÁN")
                                    .setMessage("Xác nhận thanh toán "+ tvTongTien.getText())
                                    .setPositiveButton("ĐỒNG Ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sqlHelper.deleteAllCart();
                                            listBooksCart.clear();
                                            bookCartAdapter.upDateData(listBooksCart);
                                            long s=0;
                                            for (int i=0; i<listBooksCart.size(); i++){
                                                s+=listBooksCart.get(i).getPrice();
                                            }
                                            tvTongTien.setText("Tổng tiền: "+ s+" VNĐ");
                                            tvCountCart.setText("0");
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).create();
                            dialog.show();
                        }
                    });
                }

                dialog.show();
            }
        });


        int countCart=0;
        countCart = sqlHelper.getAllCart().size();
        tvCountCart.setText(countCart +"");

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        Account finalAccount = account;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_API_BOOK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayBook = response;
                // lấy dữ liệu thành công từ api
                getDataAndRender(arrayBook);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                arrayBook = "Không lấy được API sách";
                //api lỗi thì sẽ lấy dữ liệu ở đây
                getDataAndRender(dataBook.getApi());
            }
        });
        requestQueue.add(stringRequest);


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str= s.toString();
                List<Book> list= new ArrayList<>();
                int size= listBook.size();
                for (int i=0; i<size; i++){
                    if (listBook.get(i).getTitle().toLowerCase().contains(str.toLowerCase())){
                        list.add(listBook.get(i));
                    }
                }
                adapter.updateList(list);
            }
        });


    }

    private void getDataAndRender(String arrBook) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(arrBook);
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id= jsonObject.getInt("id");
                String imageLink = jsonObject.getString("imageLink");
                String title = jsonObject.getString("title");
                String author = jsonObject.getString("author");
                String des = jsonObject.getString("description");
                String category = jsonObject.getString("categoty");
                int page = jsonObject.getInt("numOfPage");
                int rateStar = jsonObject.getInt("rateStar");
                int soDanhgia = jsonObject.getInt("numOfReview");
                long price = jsonObject.getLong("price");
                Book book=new Book(id, imageLink, title, author, page, des, rateStar, soDanhgia, price, category);
                listBook.add(book);

                adapter = new BookAdapter(listBook, MainActivity.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
                adapter.setOnClickItemBook(new OnClickItemBook() {
                    @Override
                    public void clickItem(Book book1) {
                        Intent  intent1=new Intent(MainActivity.this, MainActivity2.class);
                        intent1.putExtra("book", book1);
                        intent1.putExtra("account", account);
                        startActivity(intent1);
                    }

                    @Override
                    public void clickAddToCart(Book book) {
                        boolean isAddToCart= sqlHelper.addToCart(book);
                        if (isAddToCart){
                            Toast.makeText(MainActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                            int c = Integer.parseInt(tvCountCart.getText().toString().trim()) +1;
                            tvCountCart.setText(c +"");
                        }else{
                            Toast.makeText(MainActivity.this, "Sách đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}