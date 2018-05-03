package hu.ait.android.itemrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
        final ImageView ivCartIcon = (ImageView) findViewById(R.id.ivCartIcon);

        final Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.cart_rotate);

        ivCartIcon.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    finish();
                    Snackbar.make(findViewById(R.id.layoutContent),"WWelcome to the shopping list app baby!",Snackbar.LENGTH_LONG).show();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));


                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }