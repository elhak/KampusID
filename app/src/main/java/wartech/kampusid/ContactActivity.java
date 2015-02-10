package wartech.kampusid;

import android.net.Uri;

/**
 * Created by Hakim on 10-Feb-15.
 */
public class ContactActivity {

    private String _name,_phone,_email,_address;
    private Uri _imageURI;

    public ContactActivity(String name, String phone, String email, String address, Uri imageURI)
    {
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
        _imageURI = imageURI;
    }

    public String getName()
    {
        return _name;
    }

    public String getPhone()
    {
        return _phone;
    }

    public String getEmail()
    {
        return _email;
    }

    public String getAddress()
    {
        return _address;
    }

    public Uri getimageURI()
    {
        return _imageURI;
    }
}