package com.ielab.ishak.thenytimes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ielab.ishak.thenytimes.R;
import com.ielab.ishak.thenytimes.activities.ArticleActivity;
import com.ielab.ishak.thenytimes.activities.MainActivity;
import com.ielab.ishak.thenytimes.adapters.ArticleAdapter;
import com.ielab.ishak.thenytimes.callbacks.OnArticleClickCallback;
import com.ielab.ishak.thenytimes.models.Article;
import com.ielab.ishak.thenytimes.models.ArticlesResponse;
import com.ielab.ishak.thenytimes.server.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TechnologyFragment extends Fragment {
    Activity context;
    ArticlesResponse articlesResponse;
    ArticleAdapter articleAdapter;

    @BindView(R.id.technology_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.technology_recycler_view)
    RecyclerView recyclerView;

    public static TechnologyFragment newInstance() {
        return new TechnologyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity();
        if (context != null) {
            context.setTitle(R.string.nav_technology);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_technology, container, false);

        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArticles();
            }
        });

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        if (savedInstanceState != null && savedInstanceState.containsKey(MainActivity.SAVED_STATE)) {
            articlesResponse = (ArticlesResponse) savedInstanceState.getSerializable(MainActivity.SAVED_STATE);
            if (articlesResponse != null && !articlesResponse.getArticles().isEmpty()) {
                articleAdapter = new ArticleAdapter(articlesResponse.getArticles(), onArticleClickCallback);
                recyclerView.setAdapter(articleAdapter);
            }
            else {
                Snackbar.make(swipeRefreshLayout, R.string.nothing_to_display, Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            getArticles();
        }
        //getArticles();

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (articlesResponse != null)
            outState.putSerializable(MainActivity.SAVED_STATE, articlesResponse);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            getArticles();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void getArticles() {
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.apiService.getStoriesBySection(getResources().getStringArray(R.array.sections)[5], APIClient.API_KEY).enqueue(callback);
            }
        }, 1500);
    }

    OnArticleClickCallback onArticleClickCallback = new OnArticleClickCallback() {
        @Override
        public void onClick(Article article) {
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra(ArticleActivity.ARTICLE_SELECTED, article);
            context.startActivity(intent);
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    };

    Callback<ArticlesResponse> callback = new Callback<ArticlesResponse>() {
        @Override
        public void onResponse(@NonNull Call<ArticlesResponse> call, @NonNull Response<ArticlesResponse> response) {
            if (response.isSuccessful()) {
                articlesResponse = response.body();
                if (articlesResponse != null && !articlesResponse.getArticles().isEmpty()) {
                    articleAdapter = new ArticleAdapter(articlesResponse.getArticles(), onArticleClickCallback);
                    recyclerView.setAdapter(articleAdapter);
                }
                else {
                    Snackbar.make(swipeRefreshLayout, R.string.nothing_to_display, Snackbar.LENGTH_SHORT).show();
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(@NonNull Call<ArticlesResponse> call, @NonNull Throwable t) {
            Snackbar.make(swipeRefreshLayout, R.string.network_troubleshoot, Snackbar.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    };
}
