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
        TOPIC_LIST
    }

    public static void createFragment(int containerId, Fragment fragment, Bundle bundle,
                                      FragmentManager fragmentManager, FragmentTag fragmentTag) {
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().add(containerId, fragment, fragmentTag.name()).commit();
    }

    public static void replaceFragment(int containerId, Fragment fragment, Bundle bundle,
                                       FragmentManager fragmentManager, FragmentTag fragmentTag) {
        Fragment myFragment = fragmentManager.findFragmentByTag(fragmentTag.name());
        if (myFragment != null && myFragment.isVisible()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            transaction.replace(containerId, fragment, fragmentTag.name());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
