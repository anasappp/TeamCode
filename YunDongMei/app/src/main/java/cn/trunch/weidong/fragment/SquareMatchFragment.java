package cn.trunch.weidong.fragment;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.trunch.weidong.R;

public class SquareMatchFragment extends Fragment {
    private View view;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_square_match, container, false);
        context = getActivity();

        init();
        initListener();

        return view;
    }

    private void init() {

    }

    private void initListener() {

    }
}
