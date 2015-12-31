<---- Adapter---->

tabHost = (MaterialTabHost) v.findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        //viewPager.setOffscreenPageLimit(4);
        adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
       // viewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //viewPager.setOffscreenPageLimit(4);
            }

            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
                viewPager.setOffscreenPageLimit(3);
                //adapter.notifyDataSetChanged();
                //viewPager.setOffscreenPageLimit(4);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for(int k =0;k<adapter.getCount();k++){
            tabHost.addTab(
                    tabHost.newTab().setText(adapter.getPageTitle(k)).setTabListener(new MaterialTabListener() {
                        @Override
                        public void onTabSelected(MaterialTab tab) {
                            viewPager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabReselected(MaterialTab tab) {

                        }

                        @Override
                        public void onTabUnselected(MaterialTab tab) {

                        }
                    }));
        }

@Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        //adapter.notifyDataSetChanged();
        //viewPager.setOffscreenPageLimit(4);
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

	private class MyPagerAdapter extends FragmentStatePagerAdapter {

        String[] tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            //MyFragment myFragment = MyFragment.getInstance(position);
            Fragment fragment = null;
            switch (position) {
                case Map_View:
                    fragment = Fragment_Map_View.newInstance(mContext);
                    break;
                    //return new Fragment_Map_View();
                case Contact_list:
                    fragment = Fragment_Contact_List.newInstance(mContext);
                    //fragment = Pending_request.newInstance("pen1","pen2");
                    //adapter.notifyDataSetChanged();
                    break;

                    //return new Fragment_Contact_List();
                case Settings_list:
                    fragment = Pending_request.newInstance(mContext);
                    //fragment = Settings_frag.newInstance(mContext);
                    break;
                    //return new Settings_frag();
                case Pending_Request:
                    //fragment = Pending_request.newInstance(mContext);
                    fragment = Settings_frag.newInstance(mContext);
                    //adapter.notifyDataSetChanged();
                    break;
                    //return new Pending_request();

            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
            //return super.getItemPosition(object);
        }
    }
<-----Adapter---->