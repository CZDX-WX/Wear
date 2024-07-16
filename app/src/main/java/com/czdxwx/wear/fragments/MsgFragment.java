package com.czdxwx.wear.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.czdxwx.wear.R;
import com.czdxwx.wear.adapter.AlertAdapter;
import com.czdxwx.wear.adapter.EmptyViewAdapter;
import com.czdxwx.wear.databinding.FragmentMsgBinding;
import com.czdxwx.wear.entity.Alert;
import com.czdxwx.wear.pages.TabActivity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MsgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MsgFragment extends Fragment {
    private EmptyViewAdapter mAdapter = new EmptyViewAdapter();
    private AlertAdapter alertAdapter;
    private FragmentMsgBinding viewBinding;

    public static String TAG="MsgFragment";

    public static MsgFragment newInstance() {
        return new MsgFragment();
    }

    public interface MsgUpdateListener {
        void onMsgsUpdated(List<Alert> alerts);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentMsgBinding.inflate(inflater, container, false);

        return viewBinding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设备展示列表绑定


        viewBinding.rv.setAdapter(mAdapter);
        viewBinding.rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter.setStateViewLayout(requireContext(), R.layout.loading_view);
        mAdapter.setStateViewEnable(true);



        //取ViewModel
        AlertVM alertViewModel = new ViewModelProvider(requireActivity()).get(AlertVM.class);
        alertViewModel.getAlerts().observe(getViewLifecycleOwner(), alerts -> {
            if (alerts != null) {
                alertAdapter = new AlertAdapter(alerts);
                alertAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInLeft);
                alertAdapter.setOnItemClickListener((adapter, view, position) -> {


                    Intent intent = new Intent("com.czdxwx.ACTION_ALERT");
                    intent.putExtra("alert", alerts.get(position));
                    startActivity(intent);

                });

                viewBinding.rvMsg.setLayoutManager(new LinearLayoutManager(getContext()));
                viewBinding.rvMsg.setAdapter(alertAdapter);

                // 显示原始内容
                viewBinding.rvMsg.setVisibility(View.VISIBLE);
                // 隐藏RecyclerView
                viewBinding.rv.setVisibility(View.GONE);
            } else {
                //设置空页面
                mAdapter.setStateView(getEmptyDataView());
                // 隐藏原始内容
                viewBinding.rvMsg.setVisibility(View.GONE);
                // 显示 rv
                viewBinding.rv.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(), "数据为空", Toast.LENGTH_SHORT).show();
            }
        });


        //下拉刷新
        viewBinding.refreshLayout.setRefreshHeader(new ClassicsHeader(requireContext()));
        viewBinding.refreshLayout.setRefreshFooter(new ClassicsFooter(requireContext()));
        viewBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                if (getActivity() instanceof TabActivity) {
                    ((TabActivity) getActivity()).refreshAlerts();
                }
                refreshlayout.finishRefresh(2000);
            }
        });



    }


    private View getEmptyDataView() {
        View notDataView = LayoutInflater.from(getContext()).inflate(R.layout.empty_view, new FrameLayout(getContext()), false);
        return notDataView;
    }

    private View getErrorView() {
        View errorView = LayoutInflater.from(getContext()).inflate(R.layout.error_view, new FrameLayout(getContext()), false);
        return errorView;
    }
}