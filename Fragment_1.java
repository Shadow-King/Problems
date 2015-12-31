public static Fragment_Contact_List newInstance(Context context) {
        Fragment_Contact_List fragment = new Fragment_Contact_List();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, context.toString());
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        mContext = context;
        return fragment;
    }

    public Fragment_Contact_List() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getUserName(getContext());
        friend_req_data();

        ContentResolver resolver = getContext().getContentResolver();
        cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phnNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            contacts_list.add(contactName);
            number_list.add(phnNumber);
            Log.d(TAG, "phn is: " + number_list);
        }
        Log.d(TAG, "Contact is: " + contacts_list);
        cursor.close();

        /*Fragment_Contact_List myListFragment = new Fragment_Contact_List();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        fragmentTransaction.show(myListFragment);
        fragmentTransaction.commit();*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment__contact__list, container, false);
        //lv = (ListView) v.findViewById(R.id.listContact);
        root = (FrameLayout) v.findViewById(R.id.root);
        tvError = (TextView) v.findViewById(R.id.tvError);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    int i;

    private void friend_req_data() {
        String reg_url = "http://roadviserrr.net/citylifeezy1/get_friend_req.php";
        f_req = new ArrayList<String>();
        adapter = new MyListAdapter();
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest putRequest = new StringRequest(Request.Method.GET, reg_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response From Server:" + response);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        friend_req = jsonObj.getJSONArray("friend_request");
                        Log.d("TAG", "CNG UP:" + friend_req);

                        for (i = 0; i < friend_req.length(); i++) {
                            JSONObject c = friend_req.getJSONObject(i);
                            Log.d(TAG, "JSON Object: " + c);
                            String phone = c.getString("phone");
                            Log.d(TAG, "area: " + phone);

                            f_req.add(phone);
                            Log.d(TAG, "Friend Req Num: " + f_req);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    for (int j = 0; j < f_req.size(); j++) {
                        String sp_num = f_req.get(j);
                        String new_num = sp_num.substring(3);
                        split_number.add(new_num);
                        Log.d(TAG, "No User " + split_number);
                        for (int k = 0; k < number_list.size(); k++) {
                            if ((f_req.get(j).equals(number_list.get(k))) || (split_number.get(j).equals(number_list.get(k)))) {
                                //final_number.add(contacts_list.get(k) + "\n" + f_req.get(j));
                                Important_contact.add(new Contact_custom(contacts_list.get(k), f_req.get(j)));
                                adapter.notifyDataSetChanged();
                                //adapter.notifyDataSetChanged();
                                /*Log.d(TAG, "New Number is: " + final_number);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.text, final_number);
                                lv.setAdapter(adapter);*/
                            } else {
                                Log.d(TAG, "No User Found");
                            }
                        }
                    }
                    //adapter.notifyDataSetChanged();
                }
                populateListView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(putRequest);
        //populateListView();
        adapter.notifyDataSetChanged();
    }
	
	public void populateListView() {
        //adapter = new MyListAdapter();
        lv = (ListView) getActivity().findViewById(R.id.listContact);
        lv.setAdapter(adapter);
        //lv.notifyAll();
        //adapter.notifyDataSetChanged();
    }

	public class MyListAdapter extends ArrayAdapter<Contact_custom> {

        //ViewHolderItem viewHolder;

        public MyListAdapter() {
            super(getActivity(), R.layout.custom_contact, Important_contact);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = inflater.inflate(R.layout.custom_contact, parent, false);

                /*viewHolder = new ViewHolderItem();
                viewHolder.bReq = (Button) itemView.findViewById(R.id.bRequest);
                viewHolder.bCan = (Button) itemView.findViewById(R.id.bCancel);
                viewHolder.bReq.setTag(position);
                viewHolder.bCan.setTag(position);*/
                bReq = (Button) itemView.findViewById(R.id.bRequest);
                /*viewHolder.bCan = (Button) itemView.findViewById(R.id.bCancel);
                viewHolder.bReq.setTag(position);
                viewHolder.bCan.setTag(position);*/

                //itemView.setTag(viewHolder);

            }
            /*else {
                //viewHolder = (ViewHolderItem) itemView.getTag();
            }*/

            Contact_custom phone_number = Important_contact.get(position);

            TextView name = (TextView) itemView.findViewById(R.id.contact_name);
            name.setText(phone_number.getName());

            phn1 = (TextView) itemView.findViewById(R.id.phone_number);
            phn1.setText(phone_number.getPhone());

            final View finalItemView = itemView;

            bReq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    names = (String) ((TextView) finalItemView.findViewById(R.id.contact_name)).getText();
                    numbers = (String) ((TextView) finalItemView.findViewById(R.id.phone_number)).getText();

                    /*if (bReq.isChecked()) {
                        request_process();
                        showConfirmation();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("toggleButton", bReq.isChecked());
                        editor.commit();
                    } else {
                        delete_process();
                        delete_req();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("toggleButton", bReq.isChecked());
                        editor.commit();
                    }*/
                    Button bReq = (Button) v;
                    if (bReq.getText().toString().equals("Send Request")) {
                        request_process();
                        showConfirmation();
                        bReq.setText("Cancel Request");
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("btnStat", "Cancel Request");
                        editor.commit();
                        notifyDataSetChanged();

                    }
                    else if (bReq.getText().toString().equals("Cancel Request")) {
                        delete_process();
                        delete_req();
                        bReq.setText("Send Request");
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("btnStat", "Send Request");
                        editor.commit();
                        notifyDataSetChanged();
                    }
                }
            });
            return itemView;
        }

    }