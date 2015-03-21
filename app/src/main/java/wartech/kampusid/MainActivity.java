package wartech.kampusid;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    private static final int EDIT = 0, DELETE = 1;

    private EditText nameTxt, phoneTxt, emailTxt, addressTxt;
    private TabHost tabHost;
    List<ContactActivity> Contact = new ArrayList<ContactActivity>();
    private ListView kampusListView;
    private ImageView kampusImageImgView;
    Uri ImageURI = Uri.parse("android.resource://wartech.kampusid/drawable/topi.png");
    DatabaseHandler dbHandler;
    int longClickedItemIndex;
    ArrayAdapter<ContactActivity> arrayAdapter;

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
        dbHandler = new DatabaseHandler(getApplicationContext());

        registerForContextMenu(kampusListView);

        kampusListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;
                return false;
            }
        });

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
                ContactActivity contactActivity = new ContactActivity(dbHandler.getListCount(),String.valueOf(nameTxt.getText()), String.valueOf(phoneTxt.getText()), String.valueOf(emailTxt.getText()), String.valueOf(addressTxt.getText()), ImageURI);
                if(!contactExist(contactActivity)) {
                    dbHandler.Create(contactActivity);
                    Contact.add(contactActivity);
                    //Contact.add(new ContactActivity(0, nameTxt.getText().toString(), phoneTxt.getText().toString(), emailTxt.getText().toString(), addressTxt.getText().toString(), ImageURI));
                    //PopulateList();
                    arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + " Has Been Added", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + " Already Exist ", Toast.LENGTH_SHORT).show();
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addBtn.setEnabled(String.valueOf(nameTxt.getText()).toString().trim().length() > 0);
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
               startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        if (dbHandler.getListCount() != 0)
            Contact.addAll(dbHandler.getAll());

        PopulateList();
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuinfo)
    {
        super.onCreateContextMenu(menu, view, menuinfo);

        menu.setHeaderIcon(R.drawable.pencilicon);
        menu.setHeaderTitle("Option");
        menu.add(Menu.NONE, EDIT, Menu.NONE, "Edit");
        menu.add(Menu.NONE, DELETE, Menu.NONE, "Delete");

    }

    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case EDIT :
                    //TODO : Implement Editing Method
                break;

            case DELETE :
                    dbHandler.delete(Contact.get(longClickedItemIndex));
                    Contact.remove(longClickedItemIndex);
                    arrayAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private boolean contactExist(ContactActivity contact)
    {
        String name = contact.getName();
        int contactCount = Contact.size();

        for(int i = 0; i < contactCount; i++)
        {
            if(name.compareToIgnoreCase(Contact.get(i).getName()) == 0){
                return true;
            }
        }
        return false;
    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if(resCode == RESULT_OK){
            if(reqCode == 1){
                ImageURI = data.getData();
                kampusImageImgView.setImageURI(data.getData());
            }
        }
    }

    private void PopulateList()
    {
        arrayAdapter = new ContactListAdapter();
        kampusListView.setAdapter(arrayAdapter);
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
            ImageView kampusImage;
            name = (TextView) view.findViewById(R.id.kampusName);
            name.setText(currentContact.getName());
            phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(currentContact.getPhone());
            email = (TextView) view.findViewById(R.id.emailAddress);
            email.setText(currentContact.getEmail());
            address = (TextView) view.findViewById(R.id.cAddress);
            address.setText(currentContact.getAddress());
            kampusImage = (ImageView) view.findViewById(R.id.KampusImage);
            kampusImage.setImageURI(currentContact.getImage());


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
