package com.sikhcentre.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by brinder.singh on 24/02/17.
 */

public class FragmentUtils {
    public enum FragmentTag {
        TOPIC_LIST,
        AUDIO
    }

    public static void createFragment(int containerId, Fragment fragment, Bundle bundle,
                                      FragmentManager fragmentManager, FragmentTag fragmentTag) {
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(containerId, fragment, fragmentTag.name()).commit();
    }

    public static void replaceFragment(int containerId, Fragment fragment, Bundle bundle,
                                       FragmentManager fragmentManager, FragmentTag fragmentTag) {
        Fragment myFragment = fragmentManager.findFragmentByTag(fragmentTag.name());
        if (myFragment == null || !myFragment.isVisible()) {
            myFragment = fragment;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        myFragment.setArguments(bundle);
        transaction.replace(containerId, myFragment, fragmentTag.name());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
