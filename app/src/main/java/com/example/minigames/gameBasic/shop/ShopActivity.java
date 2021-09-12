package com.example.minigames.gameBasic.shop;



import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.minigames.R;
import com.example.minigames.base.BaseActivity;
import com.example.minigames.firebase.FirebaseHelper;

import com.example.minigames.databinding.ActivityShopBinding;

import com.example.minigames.ultis.Constants;
import com.example.minigames.ultis.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class ShopActivity extends BaseActivity<ActivityShopBinding>{


    public boolean item0=false; /* In the database it's ID number is 1 */
    public boolean item1=false; /* In the database it's ID number is 4 */
    public boolean item2=false; /* In the database it's ID number is 6 */
    public boolean item3=false; /* In the database it's ID number is 15 */
    public boolean item4=false; /* In the database it's ID number is 23 */
    public boolean item5=false; /* In the database it's ID number is 47 */
    public boolean item6=false; /* In the database it's ID number is 100 */
    public int animate,saveVal,val;
    ;
    @Override
    protected boolean isHaveRightMenu() {
        return true;
    }

    @Override
    protected boolean isHaveBackMenu() {
        return true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_shop;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.title_activity_shop);
    }

    @Override
    protected void subscribeUi()
    {
        getCurrentData();
        setInventory(1,2);
        getPurchase();
        onClickFox();
        onClickSkeleton();
        onClickItem1();
        onClickItem2();
        onClickItem3();
        onClickItem4();
        onClickItem5();
    }

    public void getPurchase()
    {
        animate = 1;
    }

    public void getCurrentData() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseHelper.getInstance().getUserDao().getUser(currentUser.getUid()).observe(this, user -> {
             if(Integer.valueOf(user.getCurrency()).equals(null))
            {
                binding.currencyVar.setText("0");
            }
             else
                 {
                 binding.currencyVar.setText(String.valueOf(user.getCurrency()));
             }
        });

        ImageButton btn1 = (binding.charItem1);
        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });


    }

    public void onClickFox()
    {
        ImageButton btn1 = (binding.charItem1);
        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int cost = 10;
                if(item5 == false) {
                    int coins = Integer.parseInt(binding.currencyVar.getText().toString());
                    if (coins >= 100) {
                        Utils.showConfirmDialog(ShopActivity.this, "Confirm Purchase", "You are about to purchase Fox character for 100 coins", (dialog, which) -> {
                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                        int n = coins - 100;
                                        String i = Integer.toString(n);
                                        binding.currencyVar.setText(i);
                                        Log.i("coin", i);
                                        val = n;
                                        setCurrency();
                                        setInventory(6, 1);
                                        Log.i("bool", String.valueOf(item5));
                                    }
                                    else {finish();}});
                    }
                    else {
                        Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You do not have enough Coins to purchase this item");
                    }
                }
                else {
                    Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You've already bought this item");
                }
                btn1.setImageResource(R.drawable.foxy);
            }
        });


    }
    public void onClickSkeleton()
    {
        ImageButton btn1 = (binding.charItem2);
        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int cost = 10;
                if(item6 == false) {
                    int coins = Integer.parseInt(binding.currencyVar.getText().toString());
                    if (coins >= 100) {
                        Utils.showConfirmDialog(ShopActivity.this, "Confirm Purchase", "You are about to purchase Skeleton character for 100", (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                int n = coins - 100;
                                String i = Integer.toString(n);
                                binding.currencyVar.setText(i);
                                Log.i("coin", i);
                                val = n;
                                setCurrency();
                                setInventory(7, 1);
                                Log.i("bool", String.valueOf(item5));
                            }
                            else {finish();}});
                    }
                    else {
                        Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You do not have enough Coins to purchase this item");
                    }
                }
                else {
                    Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You've already bought this item");
                }

            }
        });

    }

    public void onClickItem1()
    {
        ImageButton btn1 = (binding.itembtn1);
        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int cost = 10;
                if(item0 == false) {
                    int coins = Integer.parseInt(binding.currencyVar.getText().toString());
                    if (coins >= 10) {
                        Utils.showConfirmDialog(ShopActivity.this, "Confirm Purchase", "You are about to purchase Caro Trophy for 10 coins", (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                int n = coins - 10;
                                String i = Integer.toString(n);
                                binding.currencyVar.setText(i);
                                Log.i("coin", i);
                                val = n;
                                setCurrency();
                                setInventory(1, 1);
                                Log.i("bool", String.valueOf(item5));
                            }
                            else {finish();}});
                    }
                    else {
                        Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You do not have enough Coins to purchase this item");
                    }
                }
                else {
                    Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You've already bought this item");
                }

            }
        });

    }
    public void onClickItem2()
    {
        ImageButton btn1 = (binding.itembtn2);
        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int cost = 10;
                if(item1== false) {
                    int coins = Integer.parseInt(binding.currencyVar.getText().toString());
                    if (coins >= 10) {
                        Utils.showConfirmDialog(ShopActivity.this, "Confirm Purchase", "You are about to purchase Memory Trophy for 10 coins", (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                int n = coins - 10;
                                String i = Integer.toString(n);
                                binding.currencyVar.setText(i);
                                Log.i("coin", i);
                                val = n;
                                setCurrency();
                                setInventory(2, 1);
                                Log.i("bool", String.valueOf(item5));
                            }
                            else {finish();}});
                    }
                    else {
                        Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You do not have enough Coins to purchase this item");
                    }
                }
                else {
                    Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You've already bought this item");
                }

            }
        });

    }
    public void onClickItem3()
    {
        ImageButton btn1 = (binding.itembtn3);
        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int cost = 10;
                if(item2 == false) {
                    int coins = Integer.parseInt(binding.currencyVar.getText().toString());
                    if (coins >= 10) {
                        Utils.showConfirmDialog(ShopActivity.this, "Confirm Purchase", "You are about to purchase Sudoku Trophy for 10 coins", (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                int n = coins - 10;
                                String i = Integer.toString(n);
                                binding.currencyVar.setText(i);
                                Log.i("coin", i);
                                val = n;
                                setCurrency();
                                setInventory(3, 1);
                                Log.i("bool", String.valueOf(item5));
                            }
                            else {finish();}});
                    }
                    else {
                        Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You do not have enough Coins to purchase this item");
                    }
                }
                else {
                    Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You've already bought this item");
                }

            }
        });

    }
    public void onClickItem4()
    {
        ImageButton btn1 = (binding.itembtn4);
        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int cost = 10;
                if(item3 == false) {
                    int coins = Integer.parseInt(binding.currencyVar.getText().toString());
                    if (coins >= 10) {
                        Utils.showConfirmDialog(ShopActivity.this, "Confirm Purchase", "You are about to purchase Shoot Game Trophy for 10 coins ", (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                int n = coins - 10;
                                String i = Integer.toString(n);
                                binding.currencyVar.setText(i);
                                Log.i("coin", i);
                                val = n;
                                setCurrency();
                                setInventory(4, 1);
                                Log.i("bool", String.valueOf(item5));
                            }
                            else {finish();}});
                    }
                    else {
                        Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You do not have enough Coins to purchase this item");
                    }
                }
                else {
                    Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You've already bought this item");
                }

            }
        });


    }
    public void onClickItem5()
    {
        ImageButton btn1 = (binding.itembtn5);
        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int cost = 10;
                if(item4 == false) {
                    int coins = Integer.parseInt(binding.currencyVar.getText().toString());
                    if (coins >= 10) {
                        Utils.showConfirmDialog(ShopActivity.this, "Confirm Purchase", "You are about to purchase Heli Trophy for 10 coins", (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                int n = coins - 10;
                                String i = Integer.toString(n);
                                binding.currencyVar.setText(i);
                                Log.i("coin", i);
                                val = n;
                                setCurrency();
                                setInventory(5, 1);
                                Log.i("bool", String.valueOf(item5));
                            }
                            else {finish();}});
                    }
                    else {
                        Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You do not have enough Coins to purchase this item");
                    }
                }
                else {
                    Utils.showAlertDialog(ShopActivity.this,"Purchase Unsuccessful", "You've already bought this item");
                }

            }
        });
    }

    private void setCurrency() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference buyer = FirebaseDatabase.getInstance().getReference(Constants.USER_PATH).child(uid)
                .child(Constants.User.CURRENCY);
        buyer.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                saveVal = mutableData.getValue(Integer.class);
                int value = val ;
                mutableData.setValue(value);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                Log.d("Error", "runTransaction:onComplete:" + databaseError);
            }
        });
    }



    public void setInventory(int i ,int j){

            if(j == 1) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                DatabaseReference itemPath = FirebaseDatabase.getInstance().getReference(Constants.USER_PATH).child(uid)
                        .child(Constants.User.item1);
                if (i == 1) {
                    itemPath = FirebaseDatabase.getInstance().getReference(Constants.USER_PATH).child(uid)
                            .child(Constants.User.item1);
                    item0 = true;
                }
                if (i == 2) {
                    itemPath = FirebaseDatabase.getInstance().getReference(Constants.USER_PATH).child(uid)
                            .child(Constants.User.item2);
                    item1 = true;
                }
                if (i == 3) {
                    itemPath = FirebaseDatabase.getInstance().getReference(Constants.USER_PATH).child(uid)
                            .child(Constants.User.item3);
                    item2 = true;
                }
                if (i == 4) {
                    itemPath = FirebaseDatabase.getInstance().getReference(Constants.USER_PATH).child(uid)
                            .child(Constants.User.item4);
                    item3 = true;
                }
                if (i == 5) {
                    itemPath = FirebaseDatabase.getInstance().getReference(Constants.USER_PATH).child(uid)
                            .child(Constants.User.item5);
                    item4 = true;
                }
                if (i == 6) {
                    itemPath = FirebaseDatabase.getInstance().getReference(Constants.USER_PATH).child(uid)
                            .child(Constants.User.item6);
                    item5 = true;
                }
                if (i == 7) {
                    itemPath = FirebaseDatabase.getInstance().getReference(Constants.USER_PATH).child(uid)
                            .child(Constants.User.item7);
                    item6 = true;
                }


                itemPath.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        int value = 0;
                        Log.i("TestScore", String.valueOf(value));
                        ;
                        mutableData.setValue(1);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        Log.d("Error", "runTransaction:onComplete:" + databaseError);
                    }
                });
            }
            else {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseHelper.getInstance().getUserDao().getUser(currentUser.getUid()).observe(this, user -> {
                    if(Integer.valueOf(user.getItem1()).equals(1))
                    {
                        item0 = true;
                    }
                    if(Integer.valueOf(user.getItem2()).equals(1))
                    {
                        item1 = true;
                    }
                    if(Integer.valueOf(user.getItem3()).equals(1))
                    {
                        item2 = true;
                    }
                    if(Integer.valueOf(user.getItem4()).equals(1))
                    {
                        item3 = true;
                    }
                    if(Integer.valueOf(user.getItem5()).equals(1))
                    {
                        item4 = true;
                    }
                    if(Integer.valueOf(user.getItem6()).equals(1))
                    {
                        item5 = true;
                    }
                    if(Integer.valueOf(user.getItem7()).equals(1))
                    {
                        item6 = true;
                    }

            });
            }
    }
}
