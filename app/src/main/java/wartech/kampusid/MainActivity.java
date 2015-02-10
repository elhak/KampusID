package wartech.kampusid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private EditText nameTxt, phoneTxt, emailTxt, addressTxt;
    private TabHost tabHost;
    List<ContactActivity> Contact = new ArrayList<ContactActivity>();
    private ListView kampusListView;
    private ImageView kampusImageImgView;
    Uri imageURI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTxt = (EditText) findViewById(R.id.txtName);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        addressTxt = (EditText) findViewById(R.id.txtAddress);
        kampusListView = (ListView) findViewById(R.id.listKampus);
        kampusImageImgView = (ImageView) findViewById(R.id.imgKampusView);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tabKampusList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);


        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact.add(new ContactActivity(nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(), addressTxt.getText().toString(), imageURI));
                PopulateList();
                Toast.makeText(getApplicationContext(), nameTxt.getText().toString() + " Has Been Added", Toast.LENGTH_SHORT).show();
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addBtn.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        kampusImageImgView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
            }
        });
    }

    public void onActivityResult(int reqCode, int resCode, Intent data)
    {
        if(resCode == RESULT_OK)
        {
            if(reqCode == 1)
            {
                imageURI = data.getData();
                kampusImageImgView.setImageURI(data.getData());
            }
        }
    }

    private void PopulateList()
    {
        ArrayAdapter<ContactActivity> adapter = new ContactListAdapter();
        kampusListView.setAdapter(adapter);
    }

    private class ContactListAdapter extends ArrayAdapter<ContactActivity>
    {
        public ContactListAdapter()
        {
            super(MainActivity.this, R.layout.listview_item, Contact);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            if(view == null) {
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);
            }

            ContactActivity currentContact = Contact.get(position);

            TextView name,phone,email,address;
            name = (TextView) view.findViewById(R.id.kampusName);
            name.setText(currentContact.getName());
            phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(currentContact.getPhone());
            email = (TextView) view.findViewById(R.id.emailAddress);
            email.setText(currentContact.getEmail());
            address = (TextView) view.findViewById(R.id.cAddress);
            address.setText(currentContact.getAddress());
            ImageView ivKampusImage = (ImageView) view.findViewById(R.id.ivKampusImage);
            ivKampusImage.setImageURI(currentContact.getimageURI());

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
