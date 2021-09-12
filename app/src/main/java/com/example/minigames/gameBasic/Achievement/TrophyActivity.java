package com.example.minigames.gameBasic.Achievement;

import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.example.minigames.R;
import com.example.minigames.base.BaseActivity;
import com.example.minigames.databinding.ActivityTrophyBinding;
import com.example.minigames.firebase.FirebaseHelper;
import com.example.minigames.gameBasic.shop.ShopActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TrophyActivity extends BaseActivity<ActivityTrophyBinding> {
    private AnimationDrawable aniChar;
    private ImageView charImg;
    private Integer i=0;
    public boolean item1=false; /* In the database it's ID number is 4 */
    public boolean item2=false; /* In the database it's ID number is 6 */
    public boolean item3=false; /* In the database it's ID number is 15 */
    public boolean item4=false; /* In the database it's ID number is 23 */
    public boolean item5=false; /* In the database it's ID number is 47 */
    public boolean item6=false; /* In the database it's ID number is 100 */
    public boolean item7=false; /* In the database it's ID number is 203 */
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
        return R.layout.activity_trophy;
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.title_activity_trophy);
    }

    @Override

    protected void subscribeUi()
    {
        getAnimation(0);
    }

    private void setAnimationfox()
    {
        Log.i("printed item","entered loop");
        if(item6==true)
        {
            Log.i("printed item","entered 2nd loop");

            charImg = binding.charImg;
            charImg.setBackgroundResource(R.drawable.foxanimation);
            aniChar = (AnimationDrawable) charImg.getBackground();
            aniChar.start();
        }
    }

    private void setAnimationSkeleton() {
        charImg = binding.char2;
        charImg.setBackgroundResource(R.drawable.skeletonanimation);
        aniChar = (AnimationDrawable) charImg.getBackground();
        aniChar.start();
    }


    private void getAnimation(int i)
    {

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseHelper.getInstance().getUserDao().getUser(currentUser.getUid()).observe(this, user -> {

                if(Integer.valueOf(user.getItem1()).equals(1))
                {
                    item1 = true;
                    binding.trophy1.setBackgroundResource(R.drawable.caro_trophy);
                }
                if(Integer.valueOf(user.getItem2()).equals(1))
                {
                    item2 = true;
                    binding.trophy2.setBackgroundResource(R.drawable.memory_trophy);

                }
                if(Integer.valueOf(user.getItem3()).equals(1))
                {
                    item3 = true;
                    binding.trophy3.setBackgroundResource(R.drawable.sudoku_trophy);

                }
                if(Integer.valueOf(user.getItem4()).equals(1))
                {
                    item4 = true;
                    binding.trophy4.setBackgroundResource(R.drawable.shoot_trophy);

                }
                if(Integer.valueOf(user.getItem5()).equals(1))
                {
                    item5 = true;
                    binding.trophy5.setBackgroundResource(R.drawable.heli_trophy);

                }
                if(Integer.valueOf(user.getItem6()).equals(1))
                {
                    item6 = true;
                    setAnimationfox();

                }
                if(Integer.valueOf(user.getItem7()).equals(1))
                {
                    item7 = true;
                    setAnimationSkeleton();

                }

                if(Integer.valueOf(user.getCurrency()).equals(null))
                {
                    binding.currencyVar.setText("0");
                }
                else
                {
                  //  Log.i("printed item",String.valueOf(user.getItem6()));
                    binding.currencyVar.setText(String.valueOf(user.getCurrency()));
                }
            });

    }

}
