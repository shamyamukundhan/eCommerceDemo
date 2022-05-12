package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tshirts,sportsT_shirts,femaleDresses,sweaters;
    private ImageView glasses,purses_bagswallets,hats_caps,shoes;
    private ImageView headphones_handsfree,laptop_pc,watches,mobilesphones;
    private Button logout_btn,check_order_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tshirts=(ImageView) findViewById(R.id.t_shirts);
        sportsT_shirts=(ImageView) findViewById(R.id.sports_t_shirts);
        femaleDresses=(ImageView) findViewById(R.id.female_dresses);
        sweaters=(ImageView) findViewById(R.id.sweaters);

        glasses=(ImageView) findViewById(R.id.glasses);
        purses_bagswallets=(ImageView) findViewById(R.id.purses_bags_wallets);
        hats_caps=(ImageView) findViewById(R.id.hats_caps);
        shoes=(ImageView) findViewById(R.id.shoes);

        headphones_handsfree=(ImageView) findViewById(R.id.headphones_handfree);
        laptop_pc=(ImageView) findViewById(R.id.laptop_pc);
        watches=(ImageView) findViewById(R.id.watches);
        mobilesphones=(ImageView) findViewById(R.id.mobilephones);

        logout_btn=findViewById(R.id.admin_logout_btn);
        check_order_btn=findViewById(R.id.check_orders_btn);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(AdminCategoryActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        check_order_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class);
                startActivity(intent);

            }
        });

        tshirts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","T-shirts");
                startActivity(intent);
            }
        });

        sportsT_shirts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Sports T-Shirts");
                startActivity(intent);

            }
        });

        femaleDresses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Female Dresses");
                startActivity(intent);

            }
        });

        sweaters.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Sweaters");
                startActivity(intent);

            }
        });

        glasses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);

            }
        });

        purses_bagswallets.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Purses Bags Wallets");
                startActivity(intent);
            }
        });

        hats_caps.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Hats and Caps");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });

        headphones_handsfree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Headphones Handsfree");
                startActivity(intent);
            }
        });

        laptop_pc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Laptop_pc");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });

        mobilesphones.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Mobiles");
                startActivity(intent);
            }
        });

    }
}