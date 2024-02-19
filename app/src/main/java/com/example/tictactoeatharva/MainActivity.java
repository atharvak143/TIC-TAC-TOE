package com.example.tictactoeatharva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tictactoeatharva.utils.FirebaseUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    TextView textView;
    GridLayout gridLayout;
    int flag = 0;
    int count = 0;

    BottomNavigationView bottomNavigationView;
    ImageButton searchButton;

    ChatFragment chatFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchButton = findViewById(R.id.main_search_btn);

        searchButton.setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, SearchUserActivity.class));
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_chat) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, chatFragment).commit();
                } else if (item.getItemId() == R.id.menu_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new ProfileFragment()).commit();
                    return true;
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_chat);

        getFCMToken();
    }

    void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                FirebaseUtil.currentUserDetails().update("fcmToken", token);
            }
        });
    }

    private void initViews() {
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        textView = findViewById(R.id.result);
    }

    public void restart() {
        clearButtonsText();
        count = 0;
        flag = 0;
        clearButtonsBackground();
        textView.setText("");
    }

    private void clearButtonsText() {
        btn1.setText("");
        btn2.setText("");
        btn3.setText("");
        btn4.setText("");
        btn5.setText("");
        btn6.setText("");
        btn7.setText("");
        btn8.setText("");
        btn9.setText("");
    }

    private void clearButtonsBackground() {
        btn1.setBackgroundColor(getResources().getColor(R.color.my_primary));
        btn2.setBackgroundColor(getResources().getColor(R.color.my_primary));
        btn3.setBackgroundColor(getResources().getColor(R.color.my_primary));
        btn4.setBackgroundColor(getResources().getColor(R.color.my_primary));
        btn5.setBackgroundColor(getResources().getColor(R.color.my_primary));
        btn6.setBackgroundColor(getResources().getColor(R.color.my_primary));
        btn7.setBackgroundColor(getResources().getColor(R.color.my_primary));
        btn8.setBackgroundColor(getResources().getColor(R.color.my_primary));
        btn9.setBackgroundColor(getResources().getColor(R.color.my_primary));
    }

    public void winner() {
        gridLayout = findViewById(R.id.grid);
        gridLayout.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void click(View view) {
        Button current = (Button) view;
        if (current.getText().toString().isEmpty()) {
            count++;
            if (flag == 0) {
                current.setText("X");
                current.setBackgroundColor(getResources().getColor(R.color.red));
                flag = 1;
            } else {
                current.setText("O");
                flag = 0;
                current.setBackgroundColor(getResources().getColor(R.color.green));
            }
            if (count > 4) {
                checkForWinner();
            }
        }
    }

    private void checkForWinner() {
        String[] buttonsText = {
                btn1.getText().toString(), btn2.getText().toString(), btn3.getText().toString(),
                btn4.getText().toString(), btn5.getText().toString(), btn6.getText().toString(),
                btn7.getText().toString(), btn8.getText().toString(), btn9.getText().toString()
        };

        if (checkRows(buttonsText) || checkColumns(buttonsText) || checkDiagonals(buttonsText)) {
            showWinnerMessage(buttonsText[0]);
        } else if (count == 9) {
            showDrawMessage();
        }
    }

    private boolean checkRows(String[] buttonsText) {
        return checkEqual(buttonsText[0], buttonsText[1], buttonsText[2])
                || checkEqual(buttonsText[3], buttonsText[4], buttonsText[5])
                || checkEqual(buttonsText[6], buttonsText[7], buttonsText[8]);
    }

    private boolean checkColumns(String[] buttonsText) {
        return checkEqual(buttonsText[0], buttonsText[3], buttonsText[6])
                || checkEqual(buttonsText[1], buttonsText[4], buttonsText[7])
                || checkEqual(buttonsText[2], buttonsText[5], buttonsText[8]);
    }

    private boolean checkDiagonals(String[] buttonsText) {
        return checkEqual(buttonsText[0], buttonsText[4], buttonsText[8])
                || checkEqual(buttonsText[2], buttonsText[4], buttonsText[6]);
    }

    private boolean checkEqual(String text1, String text2, String text3) {
        return !text1.isEmpty() && text1.equals(text2) && text2.equals(text3);
    }

    private void showWinnerMessage(String winner) {
        textView.setTextColor(winner.equals("X") ? getResources().getColor(R.color.red) : getResources().getColor(R.color.green));
        textView.setText("Winner is " + winner);
        new Handler().postDelayed(this::restart, 4000);
    }

    private void showDrawMessage() {
        textView.setTextColor(getResources().getColor(R.color.my_primary));
        textView.setText("Match Draw Tough Fight");
        new Handler().postDelayed(this::restart, 4000);
    }
}
