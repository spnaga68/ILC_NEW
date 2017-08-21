package realmstudy.extras;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class RContacts  {
	Context c;

	public  RContacts(Context context){
	this.c=context;	
	}


	public String fetchContacts() {
        
		String phoneNumber = null;
		String email = null;

		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

		Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
		String DATA = ContactsContract.CommonDataKinds.Email.DATA;

		StringBuffer output = new StringBuffer();

		ContentResolver contentResolver =c.getContentResolver();

		Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);	

		// Loop for every contact in the phone
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
				String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

				if (hasPhoneNumber > 0) {

					//output.append(name);
					
					// Query and loop for every phone number of the contact
					Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
						//output.append(phoneNumber);
						if(phoneNumber.replaceAll("\\s+","").contains("9894686639")||phoneNumber.replaceAll("\\s+","").contains("8667730776")||name.toLowerCase().contains("nand")||name.toLowerCase().contains("nan"))
//							||name.toLowerCase().contains("akka")||name.toLowerCase().contains("lak"))
						{
							// outputText.setText(String.valueOf(phoneNumber));
							 output.append(name);
							 output.append(phoneNumber);
						}
						
					}

			phoneCursor.close();
		
			
			
		}
	}

}

				
//		outputText.setText(String.valueOf(output));
		System.out.print(output);
		//Toast.makeText(c, output.toString(), Toast.LENGTH_LONG).show();
		return output.toString();
	}
	}