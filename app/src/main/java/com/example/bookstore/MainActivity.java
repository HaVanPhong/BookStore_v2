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
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFormatException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookstore.API_Service.APIController;
import com.example.bookstore.API_Service.ResponseEntity;
import com.example.bookstore.CartActivity.BookCartAdapter;
import com.example.bookstore.CartActivity.IOnClick;
import com.example.bookstore.Constant.dataBook;
import com.example.bookstore.Database.Account;
import com.example.bookstore.Database.SQLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String URL_API_BOOK = "https://shopbook3.herokuapp.com/api/books";
    dataBook dataBook = new dataBook();
    String arrayBook;
    List<Book> listBook= new ArrayList<>();

    BookAdapter adapter;

    RecyclerView recyclerView;
    FrameLayout layoutCart;
    TextView tvCountCart;
    Account account;
    EditText edtSearch;
    TextView tvTruyenTranh, tvThieuNien, tvChinhTri, tvVanHoc;
    TextView tvNxbKimDong, tvNxbTre, tvNxbTheGioi, tvNxbPhuNu, tvNxbLaoDong, tvKqTK;

    List<Book> listBooksCart;
    BookCartAdapter bookCartAdapter;

    SQLHelper sqlHelper = new SQLHelper(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        AnhXa();

        adapter = new BookAdapter(listBook, MainActivity.this);

        Intent intent1 = getIntent();

        account = intent1.getParcelableExtra("account");
        if (account == null) {
            account = intent1.getParcelableExtra("acc");
        }

        actionBar.setTitle("Welcome " + account.getUsername());

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

                RecyclerView revCart = dialog.findViewById(R.id.revCart);
                TextView tvTongTien = dialog.findViewById(R.id.tvTongTien);
                Button btnThanhToan = dialog.findViewById(R.id.btnThanhToan);
                Button btnXoaHetCart = dialog.findViewById(R.id.btnXoaHetCart);
                TextView tvEmpty = dialog.findViewById(R.id.tvEmpty);

                listBooksCart = sqlHelper.getAllCart();
                if (listBooksCart.size() == 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    long s = 0;
                    for (int i = 0; i < listBooksCart.size(); i++) {
                        s += listBooksCart.get(i).getPrice();
                    }
                    tvTongTien.setText("Tổng tiền: " + s + " VNĐ");
                    bookCartAdapter = new BookCartAdapter(listBooksCart, MainActivity.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
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
                                            long s = 0;
                                            for (int i = 0; i < listBooksCart.size(); i++) {
                                                s += listBooksCart.get(i).getPrice();
                                            }
                                            int c = Integer.parseInt(tvCountCart.getText().toString().trim()) - 1;
                                            if (c < 0) {
                                                c = 0;
                                            }
                                            tvCountCart.setText(c + "");
                                            tvTongTien.setText("Tổng tiền: " + s + " VNĐ");
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
                                            long s = 0;
                                            for (int i = 0; i < listBooksCart.size(); i++) {
                                                s += listBooksCart.get(i).getPrice();
                                            }
                                            tvTongTien.setText("Tổng tiền: " + s + " VNĐ");
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
                                    .setMessage("Xác nhận thanh toán " + tvTongTien.getText())
                                    .setPositiveButton("ĐỒNG Ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sqlHelper.deleteAllCart();
                                            listBooksCart.clear();
                                            bookCartAdapter.upDateData(listBooksCart);
                                            long s = 0;
                                            for (int i = 0; i < listBooksCart.size(); i++) {
                                                s += listBooksCart.get(i).getPrice();
                                            }
                                            tvTongTien.setText("Tổng tiền: " + s + " VNĐ");
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


        int countCart = 0;
        countCart = sqlHelper.getAllCart().size();
        tvCountCart.setText(countCart + "");

        callApi("");

        tvNxbTre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi("publisher/NXB Trẻ");
                tvKqTK.setText("Kết quả tìm kiếm: Nhà XB Trẻ");
            }
        });
        tvNxbPhuNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi("publisher/NXB Phụ Nữ");
                tvKqTK.setText("Kết quả tìm kiếm: Nhà XB Phụ Nữ");
            }
        });
        tvNxbLaoDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi("publisher/NXB Lao Động");
                tvKqTK.setText("Kết quả tìm kiếm: Nhà XB Lao Động");
            }
        });
        tvNxbKimDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi("publisher/NXB Kim Đồng");
                tvKqTK.setText("Kết quả tìm kiếm: Nhà XB Kim Đồng");
            }
        });

        tvNxbTheGioi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi("publisher/NXB Thế Giới");
                tvKqTK.setText("Kết quả tìm kiếm: Nhà XB Thế Giới");
            }
        });

        //thể loại
        tvChinhTri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi("categories=Sách chính trị");
                tvKqTK.setText("Kết quả tìm kiếm: Sách Chính trị");
            }
        });
        tvThieuNien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi("categories/Sách thiếu niên");
                tvKqTK.setText("Kết quả tìm kiếm: Sách thiếu niên");
            }
        });
        tvTruyenTranh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "NXB KIM DONG", Toast.LENGTH_SHORT).show();
                callApi("categories/Truyện tranh");
                tvKqTK.setText("Kết quả tìm kiếm: Sách truyện tranh");

            }
        });

        tvVanHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi("categories/Sách văn học");
                tvKqTK.setText("Kết quả tìm kiếm: Sách văn học");
            }
        });


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                List<Book> list = new ArrayList<>();
                int size = listBook.size();
                for (int i = 0; i < size; i++) {
                    if (listBook.get(i).getTitle().toLowerCase().contains(str.toLowerCase())) {
                        list.add(listBook.get(i));
                    }
                }
                adapter.updateList(list);
            }
        });


    }

    private void callApi(String uri) {
        APIController.apiService.getBookByPublisher(uri).enqueue(new Callback<ResponseEntity>() {
            @Override
            public void onResponse(Call<ResponseEntity> call, Response<ResponseEntity> response) {
                if (response.isSuccessful()) {
                    int status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status == 200) {
                        List<com.example.bookstore.API_Service.Book> books = response.body().getData();
                        getDataAndRender(books);
                    }
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
//                ResponseEntity ress= response.body();
//                getDataAndRender();
            }

            @Override
            public void onFailure(Call<ResponseEntity> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Call API thất bại", Toast.LENGTH_SHORT).show();
            }
        });
//        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//        String url = URL_API_BOOK+uri;
//        Log.d("c", "url................: "+ url);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.replace(" ", "%20"), new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject= new JSONObject(response);
//                    int status= jsonObject.getInt("status");
//                    String message= jsonObject.getString("message");
//                    if (status==200){
//                        String data= jsonObject.getString("data");
//                        // lấy dữ liệu thành công từ api
//                        getDataAndRender(data);
//                    }else {
//                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
//                        Log.d("c", "server response: "+ message);
//                        recyclerView.setAdapter(new BookCartAdapter(null, MainActivity.this));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                arrayBook = "Không lấy được API sách";
//                recyclerView.setAdapter(new BookCartAdapter(null, MainActivity.this));
//                //api lỗi thì sẽ lấy dữ liệu ở đây
////                getDataAndRender(dataBook.getApi());
//                Toast.makeText(MainActivity.this, "Kết nối không ổn định, hãy thử lại" + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        requestQueue.add(stringRequest);
    }

    private void AnhXa() {
        recyclerView = findViewById(R.id.rcv);
        layoutCart = findViewById(R.id.rltCart);
        tvCountCart = findViewById(R.id.tvCountCart);
        edtSearch = findViewById(R.id.edtSearch);
        //thể loại
        tvTruyenTranh = findViewById(R.id.tvTruyenTranh);
        tvVanHoc = findViewById(R.id.tvVanHoc);
        tvThieuNien = findViewById(R.id.tvthieuNien);
        tvChinhTri = findViewById(R.id.tvChinhTri);
        //nxb
        tvNxbKimDong = findViewById(R.id.tvNxbKimDong);
        tvNxbLaoDong = findViewById(R.id.tvNxbLaoDong);
        tvNxbPhuNu = findViewById(R.id.tvNxbPhuNu);
        tvNxbTheGioi = findViewById(R.id.tvNxbTheGioi);
        tvNxbTre = findViewById(R.id.tvNxbTre);

        tvKqTK = findViewById(R.id.tvKqTk);
    }

    private void getDataAndRender(List<com.example.bookstore.API_Service.Book> booksAPI) {
        listBook= new ArrayList<>();
        int size = booksAPI.size();
        for (int i = 0; i < size; i++) {
            int id = booksAPI.get(i).getId();
            String imageLink = booksAPI.get(i).getImageLink();
            String title = booksAPI.get(i).getTitle();
            String author = booksAPI.get(i).getAuthor();
            int numOfPage = booksAPI.get(i).getNumOfPage();
            String description = booksAPI.get(i).getDescription();
            int rateStar = booksAPI.get(i).getRateStar();
            int numberOfView = booksAPI.get(i).getNumOfReview();
            long price = booksAPI.get(i).getPrice();
            String category = booksAPI.get(i).getCategoty();
            int amount = booksAPI.get(i).getAmount();
            Book book = new Book(id, imageLink, title, author, numOfPage, description, rateStar, numberOfView, price, category, amount);
            listBook.add(book);
            adapter = new BookAdapter(listBook, MainActivity.this);
//            adapter.updateList(listBook);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            adapter.setOnClickItemBook(new OnClickItemBook() {
                @Override
                public void clickItem(Book book1) {
                    Intent intent1 = new Intent(MainActivity.this, MainActivity2.class);
                    intent1.putExtra("book", book1);
                    intent1.putExtra("account", account);
                    startActivity(intent1);
                }

                @Override
                public void clickAddToCart(Book book) {
                    boolean isAddToCart = sqlHelper.addToCart(book);
                    if (isAddToCart) {
                        Toast.makeText(MainActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                        int c = Integer.parseInt(tvCountCart.getText().toString().trim()) + 1;
                        tvCountCart.setText(c + "");
                    } else {
                        Toast.makeText(MainActivity.this, "Sách đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


//        JSONArray jsonArray = null;
//        try {
//            jsonArray = new JSONArray(arrBook);
//            int size = jsonArray.length();
//            listBook= new ArrayList<>();
//            for (int i = 0; i < size; i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                int id= jsonObject.getInt("id");
//                String imageLink = jsonObject.getString("imageLink");
//                String title = jsonObject.getString("title");
//                String author = jsonObject.getString("author");
//                String des = jsonObject.getString("description");
//                String category = jsonObject.getString("categoty");
//                int page = jsonObject.getInt("numOfPage");
//                int rateStar = jsonObject.getInt("rateStar");
//                int soDanhgia = jsonObject.getInt("numOfReview");
//                long price = jsonObject.getLong("price");
//                int amount = jsonObject.getInt("amount");
//                Book book=new Book(id, imageLink, title, author, page, des, rateStar, soDanhgia, price, category, amount);
//                listBook.add(book);
//
////                adapter= new BookAdapter(listBook, MainActivity.this);
//                adapter.updateList(listBook);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
//                recyclerView.setAdapter(adapter);
//                recyclerView.setLayoutManager(layoutManager);
//                adapter.setOnClickItemBook(new OnClickItemBook() {
//                    @Override
//                    public void clickItem(Book book1) {
//                        Intent  intent1=new Intent(MainActivity.this, MainActivity2.class);
//                        intent1.putExtra("book", book1);
//                        intent1.putExtra("account", account);
//                        startActivity(intent1);
//                    }
//
//                    @Override
//                    public void clickAddToCart(Book book) {
//                        boolean isAddToCart= sqlHelper.addToCart(book);
//                        if (isAddToCart){
//                            Toast.makeText(MainActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
//                            int c = Integer.parseInt(tvCountCart.getText().toString().trim()) +1;
//                            tvCountCart.setText(c +"");
//                        }else{
//                            Toast.makeText(MainActivity.this, "Sách đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

}
