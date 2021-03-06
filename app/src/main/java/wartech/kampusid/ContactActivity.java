package wartech.kampusid;

import android.net.Uri;

/**
 * Created by Hakim on 10-Feb-15.
 */
public class ContactActivity {

    private String _name,_phone,_email,_address;
    private int _id;
    Uri _image;

    public ContactActivity(int id, String name, String phone, String email, String address, Uri image)
    {
        _id = id;
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
        _image = image;
    }

    public int getId()
    {
        return _id;
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

    public Uri getImage()
    {
        return _image;
    }
}