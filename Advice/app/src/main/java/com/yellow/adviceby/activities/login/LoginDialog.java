package com.yellow.adviceby.activities.login;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;

import com.yellow.adviceby.R;

public class LoginDialog extends AppCompatDialog implements View.OnClickListener {

    private Button cancelButton;
    private Button signinButton;

    public LoginDialog(Context context) {
        super(context, R.style.Base_Theme_AppCompat_Dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setTitle("Sign in");
        setContentView(R.layout.activity_login_dialog);
        getWindow().setBackgroundDrawableResource(R.drawable.login_dialog);
      //  getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        cancelButton = (Button) findViewById(R.id.cancel);
        signinButton = (Button) findViewById(R.id.sigin);

        cancelButton.setOnClickListener(this);
        signinButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

}
