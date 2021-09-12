package com.example.minigames.gameBasic.shop;


import android.app.Application;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.minigames.R;
import com.example.minigames.base.BaseActivity;
import com.example.minigames.databinding.ActivitySettingBinding;
import com.example.minigames.firebase.FirebaseHelper;
import com.example.minigames.firebase.model.User;
import com.example.minigames.gameBasic.caroGame.view.Board_ComVsHum;
import com.example.minigames.gameBasic.helicopterGame.GamePanel;
import com.example.minigames.gameBasic.memoryGame.MemoryGameActivity;
import com.example.minigames.gameBasic.shootingGame.view.Score;
import com.example.minigames.gameBasic.sudoku.SudokuGamePlayFragment;
import com.example.minigames.ultis.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Picasso;

public class cData extends  BaseActivity<ActivitySettingBinding> {
    User user = new User();
    GamePanel gm;
    MemoryGameActivity mem;
    Board_ComVsHum caro;
    Score sh;
    SudokuGamePlayFragment sudo;

    public cData() {
    }

    public int getGm() {
        if(gm == null)
        {
            return 0;
        }
        return gm.helliScore;
    }

    public void setGm(GamePanel gm) {
        this.gm = gm;
    }

    public int getMem() {
        if(mem == null)
        {
            return 0;
        }
        return mem.memScore;
    }

    public void setMem(MemoryGameActivity mem) {
        this.mem = mem;
    }

    public int getCaro() {
        if(caro == null)
        {
            return 0;
        }
        return caro.caroScore;
    }

    public void setCaro(Board_ComVsHum caro) {
        this.caro = caro;
    }

    public int getSh() {
        if(sh == null)
        {
            return 0;
        }
        return sh.getScore();
    }

    public void setSh(Score sh) {
        this.sh = sh;
    }

    public int getSudo() {
        if(sudo == null)
        {
            return 0;
        }
        return sudo.sudoScore;
    }

    public void setSudo(SudokuGamePlayFragment sudo) {
        this.sudo = sudo;
    }

    public int currency;

    public void updateData()
    {
        currency = getCaro()+getMem()+getSudo()+getSh()+getGm();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference refe = FirebaseDatabase.getInstance().getReference(Constants.USER_PATH).child(uid)
                .child(Constants.User.CURRENCY);


        refe.runTransaction(new Transaction.Handler() {
        @Override
        public Transaction.Result doTransaction(MutableData mutableData) {
            long value = 0;
            if(mutableData.getValue(Integer.class)!= null){
            value = mutableData.getValue(Long.class);}
            Log.i("TestScore", String.valueOf(value));
            value = value + currency;
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

    @Override
    protected boolean isHaveRightMenu() {
        return false;
    }

    @Override
    protected boolean isHaveBackMenu() {
        return false;
    }

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected String getActionBarTitle() {
        return null;
    }

    @Override
    protected void subscribeUi() {

    }
}
