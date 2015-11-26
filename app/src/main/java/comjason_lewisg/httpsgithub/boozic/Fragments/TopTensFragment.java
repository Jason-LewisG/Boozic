package comjason_lewisg.httpsgithub.boozic.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import comjason_lewisg.httpsgithub.boozic.Handlers.AdapterHandler;
import comjason_lewisg.httpsgithub.boozic.MainActivity;
import comjason_lewisg.httpsgithub.boozic.Models.TopTensModel;
import comjason_lewisg.httpsgithub.boozic.R;

public class TopTensFragment extends Fragment {
    private View rootView;
    FragmentManager manager;
    MainActivity m;

    RecyclerView mRecyclerView;
    public AdapterHandler mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;

    int colorPrimary;
    int colorPrimaryDark;
    int colorAccent;
    int colorAccentDark;

    int pastVisiblesItems, visibleItemCount, totalItemCount;

    OnPass dataPasser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_toptens,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        m = (MainActivity)getActivity();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.topTen_rv);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(false);

        mAdapter = new AdapterHandler(m, askForProductList());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
                super.onScrolled(mRecyclerView, dx, dy);
                if (Math.abs(dy) > 20) {
                    if (dy > 0) {
                        ((MainActivity) getActivity()).FAB.menuButton.hide(true);
                        closeMenu();
                    } else {
                        ((MainActivity) getActivity()).FAB.menuButton.show(true);
                        closeMenu();
                    }
                }
            }
        });

        /*mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mAdapter.getCursorCheck()) {

                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount() - 2;
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        mAdapter.addWithScroll();
                    }
                }
            }
        });*/

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.ColorAccent),
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.ColorAccent2),
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.ColorAccent3),
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.ColorAccent4),
                ContextCompat.getColor(getActivity().getApplicationContext(), R.color.ColorAccent5));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                m.PLcon.callList(m.FMHandle, mAdapter, swipeRefreshLayout, m.latitude, m.longitude);
                //mAdapter.removeAll();
            }
        });
    }

    public AdapterHandler getmAdapter() {
        return mAdapter;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public interface OnPass {
        int AskForColorPrimary();
        int AskForColorPrimaryDark();
        int AskForColorAccent();
        int AskForColorAccentDark();
        void AskToHideFilterButtons();
        void AskToShowFilterButtons();
        void CloseMenu();
        List<TopTensModel> AskForProductList();
    }

    public int askColorPrimary() { return dataPasser.AskForColorPrimary(); }

    public int askColorPrimaryDark() { return dataPasser.AskForColorPrimaryDark(); }

    public int askColorAccent() { return dataPasser.AskForColorAccent(); }

    public int askColorAccentDark() { return dataPasser.AskForColorAccentDark(); }

    public void closeMenu() { dataPasser.CloseMenu(); }

    public void askHideFilterButtons() { dataPasser.AskToHideFilterButtons(); }

    public void askShowFilterButtons() { dataPasser.AskToShowFilterButtons(); }

    public List<TopTensModel> askForProductList() { return dataPasser.AskForProductList(); }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;
        if( context instanceof Activity) {
            a = (Activity) context;
            dataPasser = (OnPass) a;
        }
        askShowFilterButtons();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        askHideFilterButtons();
    }

    @Override
    public void onStart() {
        super.onStart();
        manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }
}
