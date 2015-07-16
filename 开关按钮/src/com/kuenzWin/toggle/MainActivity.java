package com.kuenzWin.toggle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.kuenzWin.toggle.MyToggleButton.OnClickCallBack;

public class MainActivity extends Activity implements OnClickCallBack {

	private MyToggleButton tb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tb = (MyToggleButton) this.findViewById(R.id.tb);
		tb.setOnClickCallBack(this);
	}

	@Override
	public void onClick() {
		Log.d("¿ª¹Ø°´Å¥", tb.isChecked()+"");
	}

}
