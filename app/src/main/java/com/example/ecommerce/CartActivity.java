package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity
{

    private TextView txtTotalAmount, txtmsg1;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcess_Btn;
    private int overallTotalPrice=0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        txtmsg1=findViewById(R.id.msg1);
        txtTotalAmount=findViewById(R.id.total_price);
        NextProcess_Btn=findViewById(R.id.next_process_btn);
        recyclerView=findViewById(R.id.cart_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NextProcess_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CartActivity.this, ConfirmFinalActivity.class);
                intent.putExtra("Total Price", String.valueOf(overallTotalPrice));
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        CheckOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentonlineusers.getPhone()).child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {

                holder.txtProductName.setText(model.getPname());
                holder.txtProductQuantity.setText("Quantity :  "+ model.getQuantity());
                holder.txtProductPrice.setText("Price:"+ model.getPrice()+"$");

                int onetypeProductPrice=((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());

                overallTotalPrice=overallTotalPrice+onetypeProductPrice;

                txtTotalAmount.setText("Total Price = $"+String.valueOf(overallTotalPrice));

               holder.itemView.setOnClickListener(new View.OnClickListener()
               {
                   @Override
                   public void onClick(View view)
                   {
                       CharSequence option[] =new CharSequence []
                       {
                               "Edit",
                               "Remove"
                       };
                       AlertDialog.Builder builder= new AlertDialog.Builder(CartActivity.this);
                       builder.setTitle("Cart options");
                       builder.setItems(option, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i)
                       {
                           if(i==0)
                           {
                               Intent intent = new Intent(CartActivity.this, ProductsDetailsActivity2.class);
                               intent.putExtra("pid", model.getPid());
                               startActivity(intent);
                           }
                           if(i==1)
                           {
                               cartListRef.child("User View")
                                       .child(Prevalent.currentonlineusers.getPhone())
                                       .child("Products")
                                       .child(model.getPid())
                                       .removeValue()
                                       .addOnCompleteListener(new OnCompleteListener<Void>()
                                       {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task)
                                           {
                                               Toast.makeText(CartActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                                               Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                               startActivity(intent);

                                           }
                                       });
                           }

                       }
                   });

                   builder.show();

                   }
               });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder= new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
                     String username = dataSnapshot.child("name").getValue().toString();

                     if(shippingState.equals("shipped"))
                     {

                         txtTotalAmount.setText("Dear " +username+" \n your order is shipped successfully");
                         recyclerView.setVisibility(View.GONE);
                         txtmsg1.setVisibility(View.VISIBLE);
                         txtmsg1.setText("Congratulations!,Your final order has been shipped successfully Soon you will receive your order at your doorstep");
                         NextProcess_Btn.setVisibility(View.GONE);
                         Toast.makeText(CartActivity.this, "You can purchase more products once you receive you first final order", Toast.LENGTH_SHORT).show();

                     }
                     else if (shippingState.equals("not shipped"))
                     {
                         txtTotalAmount.setText("Shipping State= Not Shipped");
                         recyclerView.setVisibility(View.GONE);
                         txtmsg1.setVisibility(View.VISIBLE);
                         NextProcess_Btn.setVisibility(View.GONE);
                         Toast.makeText(CartActivity.this, "You can purchase more products once you receive you first final order", Toast.LENGTH_SHORT).show();

                     }
                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

    }
}