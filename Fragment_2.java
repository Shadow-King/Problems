public static Pending_request newInstance(Context context) {
        Pending_request fragment = new Pending_request();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, context.toString());
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        mContext = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getDefaults(getContext());
        get_request_data();
        /*Pending_request myListFragment = new Pending_request();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
        fragmentTransaction.show(myListFragment);
        fragmentTransaction.commit();*/
        //adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pending_request, container, false);
        tvError = (TextView) v.findViewById(R.id.tvError);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void get_request_data() {
        String req_url = "http://roadviserrr.net/citylifeezy1/get_user_request.php";
        get_user_req = new ArrayList<String>();
        check_user_req = new ArrayList<String>();
        adapter = new MyListAdapter();
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest putRequest = new StringRequest(Request.Method.GET, req_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response From Server:" + response);
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        get_req = jsonObj.getJSONArray("request_process");
                        Log.d("TAG", "Getting request:" + get_req);

                        for (int i = 0; i < get_req.length(); i++) {
                            JSONObject c = get_req.getJSONObject(i);
                            Log.d(TAG, "JSON Object: " + c);
                            String sender = c.getString("sender");
                            Log.d(TAG, "sender: " + sender);
                            String receiver = c.getString("receiver");
                            Log.d(TAG, "receiver: " + receiver);

                            get_user_req.add(sender);
                            Log.d(TAG, "Friend Req Num: " + get_user_req);

                            check_user_req.add(receiver);
                            Log.d(TAG, "Friend Receiver Num: " + check_user_req);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int j = 0; j < check_user_req.size(); j++) {
                        if (check_user_req.get(j).equals(retriveNumber)) {
                            request_contact.add(new Request_contact(get_user_req.get(j)));
                            Log.d(TAG, "No User Found");
                            adapter.notifyDataSetChanged();

                        }
                        else{
                            Log.d(TAG, "No User Found");
                        }
                    }
                    populateListView();
                }
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

    String retriveNumber;
    Intent i;

    public String getDefaults(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        retriveNumber = preferences.getString("phoneNumb", null);
        Log.d(TAG, "Fetching number is: " + retriveNumber);
        return retriveNumber;
    }

    private void populateListView() {
        //adapter = new MyListAdapter();
        lv = (ListView) getActivity().findViewById(R.id.listContact);
        lv.setAdapter(adapter);

        //lv.notifyAll();
        //adapter.notifyDataSetChanged();
    }

    Button bRequest;
    TextView phn1;
    String numbers;
    boolean isClicked = true;
    public List<Request_contact> request_contact = new ArrayList<Request_contact>();

    private class MyListAdapter extends ArrayAdapter<Request_contact> {
        public MyListAdapter() {
            super(getActivity(), R.layout.req_pro, request_contact);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = inflater.inflate(R.layout.request_contact, parent, false);

            }

            /*sp = PreferenceManager.getDefaultSharedPreferences(getContext());*/

            Request_contact phone_number = request_contact.get(position);

            TextView name = (TextView) itemView.findViewById(R.id.contact_name);
            name.setText(phone_number.getName());

            /*phn1 = (TextView) itemView.findViewById(R.id.phone_number);
            phn1.setText(phone_number.getPhone());*/

            /*bRequest = (Button) itemView.findViewById(R.id.bRequest);
            bRequest.setText("Send Request");
            final View finalItemView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isClicked) {
                                numbers = (String) ((TextView) finalItemView.findViewById(R.id.phone_number)).getText();
                                //request_process();
                                bRequest.setText("Cancel Request");
                                isClicked = false;
                            } else {
                                numbers = (String) ((TextView) finalItemView.findViewById(R.id.phone_number)).getText();
                                //delete_process();
                                bRequest.setText("Send Request");
                                isClicked = true;
                            }
                        }
                    });
                }
            });*/


            return itemView;
            //return super.getView(position, convertView, parent);
        }
    }