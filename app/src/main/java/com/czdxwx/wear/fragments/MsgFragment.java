package com.czdxwx.wear.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.czdxwx.wear.adapter.MsgAdapter;
import com.czdxwx.wear.databinding.FragmentMsgBinding;
import com.czdxwx.wear.entity.Msg;
import com.czdxwx.wear.pages.TabActivity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MsgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MsgFragment extends Fragment {

    private MsgAdapter msgAdapter;
    private FragmentMsgBinding viewBinding;

    public static String TAG="MsgFragment";

    public static MsgFragment newInstance() {
        return new MsgFragment();
    }

    public interface MsgUpdateListener {
        void onMsgsUpdated(List<Msg> devices);
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
        RecyclerView recyclerView = viewBinding.rvMsg;
        SmartRefreshLayout smartRefreshLayout = viewBinding.refreshLayout;


//        //取ViewModel
//        DeviceViewModel deviceViewModel = new ViewModelProvider(requireActivity()).get(DeviceViewModel.class);
//        deviceViewModel.getDevices().observe(getViewLifecycleOwner(), devices -> {
//            if (devices != null) {
//                deviceAdapter = new DeviceAdapter(devices);
//                deviceAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInLeft);
//                deviceAdapter.setOnItemClickListener((adapter, view, position) -> {
//                    Device item = adapter.getItems().get(position);
//                    if(item.getIsOnline()==1){
//                        Intent intent = new Intent(getContext(), MainActivity.class);
//                        startActivity(intent);
//                    }else{
//                        Toast.makeText(requireContext(), "设备不在线", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                recyclerView.setAdapter(deviceAdapter);
//            } else {
//                Toast.makeText(getContext(), "Error fetching devices", Toast.LENGTH_SHORT).show();
//            }
//        });


        //下拉刷新
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(requireContext()));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(requireContext()));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                if (getActivity() instanceof TabActivity) {
                    ((TabActivity) getActivity()).refreshDevices();
                }
                refreshlayout.finishRefresh(2000);
            }
        });



    }
}