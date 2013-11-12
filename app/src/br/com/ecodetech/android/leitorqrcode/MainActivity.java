package br.com.ecodetech.android.leitorqrcode;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

	private final String TAG = "LEITOR_QR_CODE";
	private static final String ZXING_MARKET = "market://search?q=pname:com.google.zxing.client.android";
	private static final String ZXING_DIRECT = "https://zxing.googlecode.com/files/BarcodeScanner4.5.apk";

	private Button btLer;
	private Button btAdd;
	private ListView lvProdutos;
	private EditText edNome;
	private ArrayAdapter<String> adapter;
	private List<String> produtos;

	private void carregar() {
		produtos = new ArrayList<String>();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i(TAG, "onCreate()");
		int layout = android.R.layout.simple_list_item_1;
		if (produtos == null) {
			carregar();
		}
		adapter = new ArrayAdapter<String>(this, layout, produtos);
		lvProdutos = (ListView) findViewById(R.id.lvProdutos);
		lvProdutos.setAdapter(adapter);

		edNome = (EditText) findViewById(R.id.edNome);

		btAdd = (Button) findViewById(R.id.btAdd);
		btAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				produtos.add(edNome.getText().toString());
				adapter.notifyDataSetChanged();
				edNome.setText("");
			}
		});

		btLer = (Button) findViewById(R.id.btLer);
		btLer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				chamaLeitor();
			}
		});

	}

	private void chamaLeitor() {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		// intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		// intent.putExtra("SCAN_FORMATS",
		// "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");

		try {
			startActivityForResult(intent, 0);

		} catch (ActivityNotFoundException e) {
			mostrarMensagem();
		}
	}

	private void mostrarMensagem() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Instalar barcode scanner?");
		builder.setMessage("Para escanear QR code você precisa instalar o ZXing barcode scanner.");
		builder.setIcon(android.R.drawable.ic_dialog_alert);

		builder.setPositiveButton("Instalar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(ZXING_MARKET));
						try {
							startActivity(intent);
						} catch (ActivityNotFoundException e) {
							intent = new Intent(Intent.ACTION_VIEW, Uri
									.parse(ZXING_DIRECT));
							startActivity(intent);
						}
					}
				});
		builder.setNegativeButton("Cancelar", null);

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		Log.i(TAG, "onActivityResult()");
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			String qrcode = intent.getStringExtra("SCAN_RESULT");
			produtos.add(qrcode);
			adapter.notifyDataSetChanged();
			chamaLeitor();
			// TextView label = (TextView) findViewById(R.id.label);
			// label.setText(qrcode);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onCreate()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onCreate()");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
