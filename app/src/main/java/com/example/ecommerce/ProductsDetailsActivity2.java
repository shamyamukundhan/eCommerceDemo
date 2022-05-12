package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductsDetailsActivity2 extends AppCompatActivity {

    private Button addtoCartBtn;
    private ElegantNumberButton numberButton;
    private ImageView productImage;
    private TextView productPrice,productDescription,productName;
    private String productID= "";
     public String  state="Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details2);

        productID=getIntent().getStringExtra("pid");


        addtoCartBtn= findViewById(R.id.add_product_to_cart_btn);
        productImage= findViewById(R.id.product_image_details);

        numberButton=(ElegantNumberButton) findViewById(R.id.number_btn);
        productPrice= findViewById(R.id.product_price_details);
        productDescription= findViewById(R.id.product_description_details);
        productName= findViewById(R.id.product_name_details);

        getProductDetails(productID);

        addtoCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if(state.equalsIgnoreCase("order placed")||state.equalsIgnoreCase("order shipped"))
                {
                    Toast.makeText(ProductsDetailsActivity2.this, "You can purchase more orders once your order is shipped or confirmed ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingtoCartList();
                }
           }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        CheckOrderState();
    }

    private void addingtoCartList()
    {
        String saveCurrentTime,saveCurrentDate;

        Calendar calforDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calforDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calforDate.getTime());

       final DatabaseReference cartRef =FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");
        cartRef.child("User View").child(Prevalent.currentonlineusers.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            cartRef.child("Admin View").child(Prevalent.currentonlineusers.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            Toast.makeText(ProductsDetailsActivity2.this, "Added to cart list", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(ProductsDetailsActivity2.this,HomeActivity.class);
                                                    startActivity(intent);
                                        }
                                    });

                        }

                    }
                });
    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    //Products products= dataSnapshot.child("Products").child("productID").getValue(Products.class);

                    Products products= dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private  void CheckOrderState()
    {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentonlineusers.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String shippingState= dataSnapshot.child("state").getValue().toString();


                    if(shippingState.equalsIgnoreCase("shipped"))
                    {
                       state="Order Shipped";

                    }
                    else if (shippingState.equalsIgnoreCase("not Shipped"))
                    {
                        state="Order Placed";

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}