package com.aspire.dubsmash.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by sia on 9/18/15.
 */
public class FragmentUtil {
    public static void addFragment(FragmentManager fragmentManager, int container, Fragment fragment) {
        addFragment(fragmentManager, container, fragment, null, 0, 0, 0, 0);
    }

    public static void addFragment(FragmentManager fragmentManager, int container, Fragment fragment, String tag) {
        addFragment(fragmentManager, container, fragment, tag, 0, 0, 0, 0);
    }

    public static void addFragment(FragmentManager fragmentManager, int container, Fragment fragment, int animEnter, int animExit, int animPopEnter, int animPopExit) {
        addFragment(fragmentManager, container, fragment, null, animEnter, animExit, animPopEnter, animPopExit);
    }

    public static void addFragment(FragmentManager fragmentManager, int container, Fragment fragment, String tag, int animEnter, int animExit, int animPopEnter, int animPopExit) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit);
        fragmentTransaction.add(container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void replaceFragment(FragmentManager fragmentManager, int container, Fragment fragment) {
        replaceFragment(fragmentManager, container, fragment, null, 0, 0, 0, 0);
    }

    public static void replaceFragment(FragmentManager fragmentManager, int container, Fragment fragment, String tag) {
        replaceFragment(fragmentManager, container, fragment, tag, 0, 0, 0, 0);
    }

    public static void replaceFragment(FragmentManager fragmentManager, int container, Fragment fragment, int animEnter, int animExit, int animPopEnter, int animPopExit) {
        replaceFragment(fragmentManager, container, fragment, null, animEnter, animExit, animPopEnter, animPopExit);
    }

    public static void replaceFragment(FragmentManager fragmentManager, int container, Fragment fragment, String tag, int animEnter, int animExit, int animPopEnter, int animPopExit) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit);
        fragmentTransaction.replace(container, fragment, tag);
        fragmentTransaction.commit();
    }
}