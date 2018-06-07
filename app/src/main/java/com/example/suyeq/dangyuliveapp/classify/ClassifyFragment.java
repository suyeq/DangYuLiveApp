package com.example.suyeq.dangyuliveapp.classify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.suyeq.dangyuliveapp.R;
import com.example.suyeq.dangyuliveapp.livelist.GetRoomListRequest;
import com.example.suyeq.dangyuliveapp.model.RoomInfo;

import java.util.List;

public class ClassifyFragment extends Fragment {

    private List<RoomInfo> list;
    private RecyclerAdapter recyclerAdapter;
    public static Fragment newInstance() {
        ClassifyFragment fragment = new ClassifyFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter=new RecyclerAdapter();
        getList();
        recyclerView.setAdapter(recyclerAdapter);
        return rootView;
    }

    public void getList(){
        GetRoomListRequest request=new GetRoomListRequest();
        int index=0;
        request.getRoomListRequest(0);
        request.setOnResultListener(new GetRoomListRequest.OnResultListener<List<RoomInfo>>() {
            @Override
            public void onFail() {
                Toast.makeText(getContext(),"加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<RoomInfo> l) {
                recyclerAdapter.addRoominfos(l);
                Toast.makeText(getContext(),"加载完成", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
