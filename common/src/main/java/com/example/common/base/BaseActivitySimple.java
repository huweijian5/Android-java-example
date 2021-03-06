package com.example.common.base;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.common.BuildConfig;

/**
 * BaseActivityDelegate的真正实现者
 */
public class BaseActivitySimple implements IBaseActivity, LifecycleObserver {

    private Activity mHost;

    public BaseActivitySimple(@NonNull Activity activity) {
        this.mHost = activity;
        if (mHost instanceof AppCompatActivity) {
            ((AppCompatActivity) mHost).getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        //todo 释放资源
        mHost = null;
    }


    @Override
    public void showToast(final String text) {
        this.mHost.runOnUiThread(() -> {
            Toast.makeText(mHost, text, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void showDebugToast(final String text) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        this.mHost.runOnUiThread(() -> {
            Toast.makeText(mHost, text, Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchBackPressedEvent() {

        if (mHost instanceof FragmentActivity) {
            FragmentManager fm = ((FragmentActivity) mHost).getSupportFragmentManager();
            if (fm == null) {
                return false;
            }
            int count = fm.getBackStackEntryCount();
            if (count >= 1) {
                FragmentManager.BackStackEntry entry = fm.getBackStackEntryAt(count - 1);
                Fragment it = fm.findFragmentByTag(entry.getName());
                if (it != null && it instanceof IFragmentBackPressed && it.isVisible()) {//事件分发给当前可见的实现了IFragmentBackPressed接口的fragment
                    return ((IFragmentBackPressed) it).onFragmentBackPressed();
                }
            }

        } else {//普通Activity
            android.app.FragmentManager fm = mHost.getFragmentManager();
            if (fm == null) {
                return false;
            }
            int count = fm.getBackStackEntryCount();
            if (count >= 1) {
                android.app.FragmentManager.BackStackEntry entry = fm.getBackStackEntryAt(count - 1);
                android.app.Fragment it = fm.findFragmentByTag(entry.getName());
                if (it != null && it instanceof IFragmentBackPressed && it.isVisible()) {//事件分发给当前可见的实现了IFragmentBackPressed接口的fragment
                    return ((IFragmentBackPressed) it).onFragmentBackPressed();
                }
            }
        }

        return false;
    }

    @Override
    public void gotoActivity(Class<?> cls) {
        Intent intent = new Intent(mHost, cls);
        mHost.startActivity(intent);
    }

    @Override
    public void gotoActivity(Class<?> cls, boolean isFinishCurrent) {
        Intent intent = new Intent(mHost, cls);
        mHost.startActivity(intent);
        if (isFinishCurrent) {
            mHost.finish();
        }
    }
}
