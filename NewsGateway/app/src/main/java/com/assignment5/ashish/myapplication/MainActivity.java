package com.assignment5.ashish.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public List<NewsList> newsList = new ArrayList<>();
    public ArrayList<String> items= new ArrayList<>();
    private ArrayList<String> categoryList = new ArrayList<>();
    public List<ArticleList> articleList = new ArrayList<>();

    private ArrayList<String> items1 = new ArrayList<>();

    static final String SERVICE_DATA = "SERVICE_DATA";


    private MyPageAdapter pageAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private ViewPager pager;
    private Menu menu;
    private Boolean isRotate = false;

    private NewsReceiver newsReceiver;
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    static final String SOURCE_ID = "SOURCE_ID";
    List<Fragment> fList;
    private ArrayAdapter myAdapter;

    private int pos = -1;
    private int pagerIndex = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        myAdapter = new ArrayAdapter<>(this,
                R.layout.drawer_list_item, items);
        mDrawerList.setAdapter(myAdapter);
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                }
        );


        new AsyncSrcDownloader(MainActivity.this).executeOnExecutor(AsyncSrcDownloader.THREAD_POOL_EXECUTOR,"all");
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );
        invalidateOptionsMenu();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);
        pager.setBackground(getResources().getDrawable(R.drawable.back));

        Intent intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);

        newsReceiver = new NewsReceiver();

        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(newsReceiver, filter1);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("NEWSLIST", (Serializable) newsList);
        outState.putSerializable("CATEGORYLIST", categoryList);
        outState.putSerializable("ARTICLELIST", (Serializable) articleList);
        outState.putSerializable("ITEMS", items);
        isRotate = true;
        outState.putBoolean("STATE_FLAG",isRotate);
        outState.putInt("POSITION_FRAGMENT",pager.getCurrentItem());
        outState.putInt("POSITION",pos);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        newsList = (ArrayList<NewsList>) savedInstanceState.getSerializable("NEWSLIST");
        categoryList = (ArrayList<String>) savedInstanceState.getSerializable("CATEGORYLIST");
        articleList.removeAll(articleList);
        articleList = (ArrayList<ArticleList>) savedInstanceState.getSerializable("ARTICLELIST");
        items1 = (ArrayList<String>) savedInstanceState.getSerializable("ITEMS");
        isRotate = savedInstanceState.getBoolean("STATE_FLAG");

        pos =  savedInstanceState.getInt("POSITION");
        pagerIndex = savedInstanceState.getInt("POSITION_FRAGMENT");
        if(pos!=-1) {
            setTitle(newsList.get(pos).getName());
            reDoFragments();
            pager.setCurrentItem(pagerIndex);
        }

    }

    private List<Fragment> getFragments() {
        fList = new ArrayList<Fragment>();
        return fList;
    }

    private void selectItem(int position) {

        setTitle(items.get(position));
        int i = 0;
        for (i = 0 ; i<newsList.size();i++){
            if(newsList.get(i).getName().equals(items.get(position))){
                break;
            }
        }
        pos = i;
        String src = newsList.get(i).getId();
        Intent intent2 = new Intent();
        intent2.setAction(MyService.ACTION_MSG_TO_SERVICE);
        intent2.putExtra(SOURCE_ID, src);
        sendBroadcast(intent2);
        pager.setBackgroundColor(Color.LTGRAY);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void reDoFragments() {

        pager.setBackgroundColor(Color.LTGRAY);

        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);

        fragments.clear();
        int count = articleList.size();

        for (int i = 0; i < count; i++) {

            String title = articleList.get(i).getTitle();
            String author = articleList.get(i).getAuthor();
            String desc = articleList.get(i).getDescription();
            String imageurl = articleList.get(i).getimageUrl();
            String time = articleList.get(i).getPublishedAt();
            String url = articleList.get(i).getUrl();
            int pageno = i+1;
            fragments.add(MyFragment.newInstance(MainActivity.this, title,author,desc,imageurl,time,pageno,count,url));
            pageAdapter.notifyChangeInPosition(i);
        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        items.clear();

        for(int i=0;i<newsList.size();i++){
            String category =  newsList.get(i).getCategory();
            String name =  newsList.get(i).getName();
            if(category.equals(item.toString())){
                items.add(name);
            }else if(item.toString().equals("all")){
                items.add(name);
            }
        }
        myAdapter.notifyDataSetChanged();

        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.clear();
        for (int i = 0; i < categoryList.size(); i++) {
            menu.add(R.menu.menu, Menu.NONE, 0, categoryList.get(i));
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public void updateData(ArrayList<NewsList> cList, ArrayList<String> newCategory) {


        items.clear();
        newsList.clear();
        newsList = cList;

        if(!isRotate){
            for(int i=0;i<newsList.size();i++){
                String name =  newsList.get(i).getName();
                items.add(name);
            }
        } else {
            items.addAll(items1);
        }

        myAdapter.notifyDataSetChanged();
        categoryList.clear();
        categoryList = newCategory;
        Collections.sort(categoryList);
        myAdapter.notifyDataSetChanged();
        this.invalidateOptionsMenu();

    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(newsReceiver);
        Intent intent = new Intent(MainActivity.this, MyService.class);
        stopService(intent);
        super.onDestroy();
    }


    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
                      return baseId + position;
        }


        public void notifyChangeInPosition(int n) {

            baseId += getCount() + n;
        }
    }

    class NewsReceiver extends BroadcastReceiver implements Serializable {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACTION_NEWS_STORY)) {{
                Bundle bundle = intent.getExtras();
                articleList = (ArrayList<ArticleList>)bundle.getSerializable(MyService.SERVICE_DATA);
                reDoFragments();
            }
            }
        }
    }
}
