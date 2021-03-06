package com.kawakp.demingliu.boilerdemo.fragment;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.kawakp.demingliu.boilerdemo.R;
import com.kawakp.demingliu.boilerdemo.adapter.HistoryWarnAdapter;
import com.kawakp.demingliu.boilerdemo.base.BaseFragment;
import com.kawakp.demingliu.boilerdemo.bean.MateralBean;
import com.kawakp.demingliu.boilerdemo.bean.WarmBean;
import com.kawakp.demingliu.boilerdemo.http.OkHttpHelper;
import com.kawakp.demingliu.boilerdemo.http.SimpleCallback;
import com.kawakp.demingliu.boilerdemo.http.SpotsCallBack;
import com.kawakp.demingliu.boilerdemo.utils.IToast;
import com.kawakp.demingliu.boilerdemo.utils.NetUtils;
import com.kawakp.demingliu.boilerdemo.utils.PathUtils;
import com.kawakp.demingliu.boilerdemo.utils.PopUtils;
import com.kawakp.demingliu.boilerdemo.utils.wheelviewutlis.NumericWheelAdapter;
import com.kawakp.demingliu.boilerdemo.utils.wheelviewutlis.OnWheelScrollListener;
import com.kawakp.demingliu.boilerdemo.utils.wheelviewutlis.WheelView;
import com.kawakp.demingliu.boilerdemo.widget.decoration.DividerItemDecoration;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

/**
 * Created by deming.liu on 2017/1/13.
 */

public class AlarmHisFragment extends BaseFragment {
    @Bind(R.id.textView_startDate)
    TextView tvStartDate;
    @Bind(R.id.textView_endDate)
    TextView tvEndDate;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.materialRefreshLayout)
    MaterialRefreshLayout mRefreshLaout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView hour;
    private WheelView mins;
    private boolean flag = false;

    private PopupWindow menuWindow;
    private OkHttpHelper okHttpHelper;
    private int page = 1;
    private int pageSize = 10;
    private int status = 1;
    private int totalPage = 1;
    private static final int STATE_NOMAL = 0; //正常加载
    private static final int STATE_REFRESH = 1; //下拉刷新
    private static final int STATE_MORE = 2; //上拉加载
    private int state = STATE_NOMAL;
    private List<WarmBean> totallist = new ArrayList<WarmBean>();
    private SpotsDialog mDialog;
    private HistoryWarnAdapter adapter;
    @Override
    protected int setContentViewId() {
        return R.layout.fragment_alarmhis;
    }

    @Override
    protected void init() {
        btnSearch.setClickable(false);
        okHttpHelper = OkHttpHelper.getInstance(getActivity());
        initRefreshLayout();
        if (getActivity() != null) {
            mDialog = new SpotsDialog(getActivity(),"拼命加载中...");
            mDialog.show();
            isRequest();
        }

    }

    private void isRequest() {
        if (NetUtils.isConnected(getActivity())) {
            requestWares();
        }else {
            IToast.showToast(getActivity(),"网络连接失败，请检查网络");
        }
    }

    private  void initRefreshLayout(){
        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(page <=totalPage)
                    loadMoreData();
                else{
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void requestWares(){
        String s = PathUtils.HISTORY + "pageNum=" + page + "&pageSize=" + pageSize + "&status=" + status;
        okHttpHelper.get(s, new SimpleCallback<MateralBean<WarmBean>>(getActivity()) {

            @Override
            public void onSuccess(Response response, MateralBean<WarmBean> warmBeanMateralBean) {

                totalPage =warmBeanMateralBean.getPages();
                totallist = warmBeanMateralBean.getList();
                //Log.d("AlarmHisFragment","历史数据:"+totallist.toString());
                showWaresData(totallist);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

        });

    }
    private  void refreshData(){
        page =1;
        state=STATE_REFRESH;
        isRequest();
    }

    private void loadMoreData(){

        page = ++page;
        state = STATE_MORE;
        isRequest();

    }

    private  void showWaresData(List<WarmBean> wares){
        switch (state){
            case  STATE_NOMAL:
                mDialog.dismiss();
                if(adapter ==null) {
                    adapter = new HistoryWarnAdapter(getActivity(), wares);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
                }
                else{
                    adapter.clear();
                    adapter.addData(wares);
                }
                break;
            case STATE_REFRESH:
                adapter.clear();
                adapter.refreshData(wares);
                recyclerView.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;
            case STATE_MORE:
                adapter.loadMoreData(wares);
                recyclerView.scrollToPosition(adapter.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;
        }

    }

    @OnClick({R.id.lin_back,R.id.textView_startDate,R.id.textView_endDate,R.id.btn_search})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back:
                getActivity().finish();
                break;
            case R.id.btn_search:
                try {
                    String startTime = URLEncoder.encode(tvStartDate.getText().toString() + ":00", "utf-8");
                    String endTime = URLEncoder.encode(tvEndDate.getText().toString() + ":00", "utf-8");
                    String url = PathUtils.HISTORY + "pageNum=" + page + "&pageSize=" + 100 + "&fromDate=" + startTime + "&toDate=" + endTime + "&status=" + status;
                    Log.d("TAG", url);
                    state = STATE_NOMAL;
                    okHttpHelper.get(url, new SpotsCallBack<MateralBean<WarmBean>>(getActivity()) {
                        @Override
                        public void onSuccess(Response response, MateralBean<WarmBean> warmBeanMateralBean) {
                            totallist = warmBeanMateralBean.getList();
                            showWaresData(totallist);
                        }

                        @Override
                        public void onError(Response response, int code, Exception e) {

                        }

                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.textView_startDate:
                flag = false;
                showPopwindow(getDataPick());//弹出日期选择器
                break;
            case R.id.textView_endDate:
                flag = true;
                showPopwindow(getDataPick());//弹出日期选择器
                break;
        }
    }
    /**
     * 初始化popupWindow
     *
     * @param view
     */
    private void showPopwindow(View view) {
        menuWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        menuWindow.setFocusable(true);
        menuWindow.setOutsideTouchable(true);
        menuWindow.setBackgroundDrawable(new BitmapDrawable());
        menuWindow.setAnimationStyle(R.style.myanimation);
        menuWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                menuWindow = null;
                PopUtils.setBackgroundAlpha(1.0f, getActivity());//设置Popw消失背景为透明
            }
        });
        PopUtils.setBackgroundAlpha(0.5f, getActivity());//设置popw出现时背景透明度

    }

    /**
     * 日期选择
     *
     * @return
     */
    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_datapick, null);

        year = (WheelView) view.findViewById(R.id.year);
        year.setAdapter(new NumericWheelAdapter(1950, curYear));
        //year.setLabel("年");
        year.setCyclic(true);
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        month.setAdapter(new NumericWheelAdapter(1, 12));
        // month.setLabel("月");
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        initDay(curYear, curMonth);
        //day.setLabel("日");
        day.setCyclic(true);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        hour = (WheelView) view.findViewById(R.id.hour);
        hour.setAdapter(new NumericWheelAdapter(0, 23));
        // hour.setLabel("时");
        hour.setCyclic(true);
        mins = (WheelView) view.findViewById(R.id.mins);
        mins.setAdapter(new NumericWheelAdapter(0, 59));
        //mins.setLabel("分");
        mins.setCyclic(true);

        hour.setCurrentItem(8);
        mins.setCurrentItem(30);

        TextView bt = (TextView) view.findViewById(R.id.set);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = (year.getCurrentItem() + 1950) + "-" + (month.getCurrentItem() + 1) + "-" + (day.getCurrentItem() + 1 + " " + hour.getCurrentItem() + ":" + mins.getCurrentItem());
                if (flag) {
                    tvEndDate.setText(str);
                    tvEndDate.setTextColor(Color.parseColor("#74CB17"));
                } else {
                    tvStartDate.setText(str);
                    tvStartDate.setTextColor(Color.parseColor("#74CB17"));
                }
                if (tvStartDate.getText().length()>0&&tvEndDate.getText().length()>0){
                    btnSearch.setClickable(true);
                    btnSearch.setBackgroundResource(R.drawable.btn_select_shape);
                }
                menuWindow.dismiss();
            }
        });
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
            }
        });
        return view;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            // TODO Auto-generated method stub
            int n_year = year.getCurrentItem() + 1950;// 楠烇拷
            int n_month = month.getCurrentItem() + 1;// 閺堬拷
            initDay(n_year, n_month);
        }
    };

    private void initDay(int arg1, int arg2) {
        day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
    }

    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

}
