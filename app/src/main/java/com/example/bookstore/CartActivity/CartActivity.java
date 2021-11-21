//package com.example.bookstore.CartActivity;
//
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Intent;
//import android.media.Image;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.example.bookstore.Book;
//import com.example.bookstore.BookAdapter;
//import com.example.bookstore.Database.Account;
//import com.example.bookstore.Database.BookDAO;
//import com.example.bookstore.Database.SQLHelper;
//import com.example.bookstore.MainActivity;
//import com.example.bookstore.MainActivity2;
//import com.example.bookstore.OnClickItemBook;
//import com.example.bookstore.R;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.List;
//
//public class CartActivity extends AppCompatActivity {
//    public static final String URL_API_BOOK = "https://bookshopb.herokuapp.com/api/books";
//    RecyclerView revCart;
//    TextView tvTongTien;
//    Button btnXoaHetCart, btnThanhToan;
//    ImageView imgHeaderCartBack;
//    TextView tvHeaderCart, tvEmpty;
//    List<Book> listBooksCart;
//    BookCartAdapter bookCartAdapter;
//    SQLHelper sqlHelper;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart);
//
//        ActionBar actionBar= getSupportActionBar();
//        actionBar.hide();
//        AnhXa();
//        tvHeaderCart.setText("Giỏ Hàng");
//
//        imgHeaderCartBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(CartActivity.this, MainActivity.class);
//                intent.putExtra("acc", account);
//                startActivity(intent);
//            }
//        });
//
//
//        sqlHelper= new SQLHelper(CartActivity.this);
//        List<BookDAO> listBookDao= sqlHelper.getAllCart();
//        int size= listBookDao.size();
//        if (size==0){
//            Toast.makeText(CartActivity.this, "Không có sách nào trong giỏ hàng", Toast.LENGTH_SHORT).show();
//            tvEmpty.setVisibility(View.VISIBLE);
//        }else
//        for (int i=0; i<size; i++){
//            RequestQueue requestQueue= Volley.newRequestQueue(CartActivity.this);
//            StringRequest stringRequest= new StringRequest(Request.Method.GET, URL_API_BOOK + "/"+  listBookDao.get(i).getIdBook(), new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(response);
//                        int id= jsonObject.getInt("id");
//                        String imageLink = jsonObject.getString("imageLink");
//                        String title = jsonObject.getString("title");
//                        int rateStar = jsonObject.getInt("rateStar");
//                        long price = jsonObject.getLong("price");
//                        Book book=new Book(imageLink, title, null, 0, null, rateStar, 0, price, null);
//                        book.setId(id);
//                        listBooksCart.add(book);
//                        bookCartAdapter= new BookCartAdapter(listBooksCart, CartActivity.this);
//                        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false);
//                        revCart.setLayoutManager(layoutManager);
//                        revCart.setAdapter(bookCartAdapter);
//                        bookCartAdapter.setiOnClick(new IOnClick() {
//                            @Override
//                            public void iOnClickRemove(Book book, int i) {
//                                sqlHelper.deleteFromCart(book.getId());
//                                listBooksCart.remove(i);
//                                bookCartAdapter.upDateData(listBooksCart);
//                            }
//                        });
//                    }
//                    catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(CartActivity.this, "Lỗi Internet, hãy thử lại để lấy dữ liệu", Toast.LENGTH_SHORT).show();
//                }
//            });
//            requestQueue.add(stringRequest);
//        }
//
//
////
////        bookCartAdapter= new BookCartAdapter(listBooksCart, CartActivity.this);
////        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false);
////        revCart.setLayoutManager(layoutManager);
////        revCart.setAdapter(bookCartAdapter);
//
//
//
//
//
//    }
//
//    private void AnhXa() {
//        revCart= findViewById(R.id.revCart);
//        tvTongTien= findViewById(R.id.tvTongTien);
//        btnThanhToan= findViewById(R.id.btnThanhToan);
//        btnXoaHetCart= findViewById(R.id.btnXoaHetCart);
//        imgHeaderCartBack= findViewById(R.id.imgHeaderCartBack);
//        tvHeaderCart= findViewById(R.id.tvHeaderCart);
//        tvEmpty= findViewById(R.id.tvEmpty);
//    }
//}