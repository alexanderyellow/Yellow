package com.yellow.adviceby.activities.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.yellow.adviceby.R;

public class CreateAdviceDialog extends AppCompatDialog implements View.OnClickListener {

    private Button cancelButton;
    private Button postButton;
    private EditText inputName;
    private EditText inputAdvice;
    private TextInputLayout inputLayoutName, inputLayoutAdvice;
    private AppCompatRatingBar ratingBar;

    /**
     * Constructor
     * @param context
     */
    public CreateAdviceDialog(Context context) {
        super(context, R.style.Base_Theme_AppCompat_Dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_create_advice);
        getWindow().setBackgroundDrawableResource(R.drawable.create_advice_dialog);

        cancelButton = (Button) findViewById(R.id.cancel);
        postButton = (Button) findViewById(R.id.post);

        cancelButton.setOnClickListener(this);
        postButton.setOnClickListener(this);

        inputName = (EditText) findViewById(R.id.name);
        inputAdvice = (EditText) findViewById(R.id.advice);
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputAdvice.addTextChangedListener(new MyTextWatcher(inputAdvice));

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutAdvice = (TextInputLayout) findViewById(R.id.input_layout_advice);

        ratingBar = (AppCompatRatingBar) findViewById(R.id.ratingBar);
        ratingBar.setStepSize(1);
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.YELLOW);
    //    LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
    //    stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar,
                                        float rating, boolean fromUser) {
            //    int _rating = (int) Math.ceil(rating);
            //    ratingBar.setRating(_rating);
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.post) {
            submitForm();
        } else if(view.getId() == R.id.cancel){
            dismiss();
        }
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateAdvice()) {
            return;
        }

        Toast.makeText(getContext(), "Thank You!", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private boolean validateName() {
        String email = inputName.getText().toString().trim();

        if (email.isEmpty()) {
            inputLayoutName.setError(getContext().getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAdvice() {
        String advice = inputAdvice.getText().toString().trim();
        if (advice.isEmpty()) {
            inputLayoutAdvice.setError(getContext().getString(R.string.err_msg_advice));
            requestFocus(inputAdvice);
            return false;
        } else {
            inputLayoutAdvice.setErrorEnabled(false);
        }

        return true;
    }
/*
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email);
    }
*/
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
                case R.id.name:
                    validateName();
                    break;
                case R.id.advice:
                    validateAdvice();
                    break;
            }
        }
    }

}
