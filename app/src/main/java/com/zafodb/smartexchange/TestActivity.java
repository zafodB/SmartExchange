package com.zafodb.smartexchange;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity {

    TextView outputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button myButton = findViewById(R.id.button);
        outputText = findViewById(R.id.outputText);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appendToTextBox(String.valueOf(Web3jwrapper.deployReadyContractTest(getApplicationContext())));
            }
        });

        Button createWalletBtn = findViewById(R.id.create_wallet_btn);

        createWalletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                appendToTextBox(Web3jwrapper.invokeDieselPriceUpdate(getApplicationContext()));


//                Web3jwrapper.createWallet(getApplicationContext());

//                try {
//
//                    File myFile = new File(getFilesDir() + "/diesel.bin");
//                    myFile.createNewFile();
//
//                    BufferedWriter writer = new BufferedWriter(new FileWriter(myFile, true));
//
//                    writer.append("");
//
//                    writer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }
        });

        Button newFilterButton = findViewById(R.id.button2);

        newFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appendToTextBox(Web3jwrapper.createNewFilter());
            }
        });
    }




    private void appendToTextBox(String textToAppend){
        String text = outputText.getText().toString() + "\n" + textToAppend;
//
        outputText.setText(text);
    }
}
