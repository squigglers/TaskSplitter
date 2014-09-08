package edu.uark.csce.chores;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		EditText username = (EditText) findViewById(R.id.username);
		username.requestFocus();
		
		TextView registerView = (TextView) findViewById(R.id.registerView);
		registerView.setText(Html.fromHtml("<font color='blue'><u>Register Here</u></font>"));
		registerView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void login(View view) {
		EditText username = (EditText) findViewById(R.id.username);
		EditText password = (EditText) findViewById(R.id.password);
		
		String usernameStr = username.getText().toString();
		String passwordStr = password.getText().toString();
		
		if(!usernameStr.equals("") && !passwordStr.equals("")) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}

		//Button submit = (Button) findViewById(R.id.submit);
	}

}
