package com.yellow.adviceby.activities.login;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yellow.adviceby.R;

public class LoginDialog extends AppCompatDialog implements View.OnClickListener {

    private Button cancelButton;
    private Button signinButton;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;

    /**
     * Constructor
     * @param context
     */
    public LoginDialog(Context context) {
        super(context, R.style.Base_Theme_AppCompat_Dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setTitle("Sign in");
        setContentView(R.layout.activity_login_dialog);
        getWindow().setBackgroundDrawableResource(R.drawable.login_dialog);
      //  getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        cancelButton = (Button) findViewById(R.id.cancel);
        signinButton = (Button) findViewById(R.id.signin);

        cancelButton.setOnClickListener(this);
        signinButton.setOnClickListener(this);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signin) {
            submitForm();
        } else {
            dismiss();
        }
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        Toast.makeText(getContext(), "Thank You!", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getContext().getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getContext().getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }
        }
    }

}
