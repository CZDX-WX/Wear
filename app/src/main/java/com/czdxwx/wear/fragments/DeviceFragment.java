package com.czdxwx.wear.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.czdxwx.wear.adapter.DeviceAdapter;
import com.czdxwx.wear.databinding.FragmentDeviceBinding;
import com.czdxwx.wear.entity.Device;
import com.czdxwx.wear.pages.TabActivity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

public class DeviceFragment extends Fragment {
    private DeviceAdapter deviceAdapter;
    private FragmentDeviceBinding viewBinding;
    private DeviceViewModel deviceViewModel;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;

    public static DeviceFragment newInstance() {
        return new DeviceFragment();
    }

    public interface DeviceUpdateListener {
        void onDevicesUpdated(List<Device> devices);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentDeviceBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //设备展示列表绑定
        recyclerView = viewBinding.recyclerView;
        smartRefreshLayout = viewBinding.refreshLayout;
        deviceAdapter = new DeviceAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(deviceAdapter);

        //取ViewModel
        deviceViewModel = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
        deviceViewModel.getDevices().observe(getViewLifecycleOwner(), devices -> {
            if (devices != null) {
                deviceAdapter.setDevices(devices);
                deviceAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Error fetching devices", Toast.LENGTH_SHORT).show();
            }
        });


        //下拉刷新
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(requireContext()));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(requireContext()));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (getActivity() instanceof TabActivity) {
                    ((TabActivity) getActivity()).refreshDevices();
                }
                refreshlayout.finishRefresh(2000);
            }
        });



    }
}