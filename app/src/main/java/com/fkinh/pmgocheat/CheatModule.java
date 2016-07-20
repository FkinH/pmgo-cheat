package com.fkinh.pmgocheat;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Author: fkinh
 * Email: fkinh26@gmail.com
 * Date: 2016-07-19
 */
public class CheatModule implements IXposedHookLoadPackage {

    public static String TAG = "Hook";

    public static double INIT_LAT = 31.037658;

    public static double INIT_LNG = -96.891977;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("Loaded app: " + lpparam.packageName);

        if (!lpparam.packageName.equals("com.nianticlabs.pokemongo")){
            return;
        }

        findAndHookMethod("android.location.LocationManager", lpparam.classLoader, "getLastKnownLocation", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
                XposedBridge.log("location provider:" + param.args[0].toString());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
                try {
                    Location location = (Location) param.getResult();
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        XposedBridge.log("location = " + latitude + ", " + longitude);
                    } else {
                        XposedBridge.log("location not found.");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        findAndHookMethod("android.location.Location", lpparam.classLoader, "getLatitude", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
                XposedBridge.log("location provider:" + param.args[0].toString());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
                try {
                    double result = (double) param.getResult();
                    double modify = 0.5;
                    double end = result-(INIT_LAT-modify);
                    String format = String.format("%7f", end);
                    param.setResult(Double.parseDouble(format));
                    XposedBridge.log("get latitude result:" + end);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        findAndHookMethod("android.location.Location", lpparam.classLoader, "getLongitude", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
                XposedBridge.log("location provider:" + param.args[0].toString());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
                double result = (double) param.getResult();
                double modify = 0.5;
                double end = result-(INIT_LNG-modify);
                String format = String.format("%7f", end);
                param.setResult(Double.parseDouble(format));
                XposedBridge.log("get longitude result:" + end);
            }
        });

    }
}
