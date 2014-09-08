package edu.uark.csce.chores;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class NormalLoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normal_login);
		
		EditText name = (EditText) findViewById(R.id.name);
		name.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.normal_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void login(View view) {
		EditText name = (EditText) findViewById(R.id.name);
		EditText code = (EditText) findViewById(R.id.accessCode);
		
		String nameStr = name.getText().toString();
		String codeStr = code.getText().toString();
		
		if(!nameStr.equals("") && !codeStr.equals("")) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}

		//Button submit = (Button) findViewById(R.id.submit);
	}
}
