/**
 *    Copyright (C) 2018 Arman Afzal <arman.afzal@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.rmanaf.impala;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.ServiceConnection;
import android.util.Log;

import com.github.rmanaf.impala.core.ControllerOptions;
import com.github.rmanaf.impala.core.IController;
import com.github.rmanaf.impala.core.IDependency;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 *  TODO: api<14 compatibility
 *  minSdkVersion is 14
 *  TODO: define process class
 *  for now one process instance is supported
 */

public class Impala {

    private static boolean mLeftStartPoint;
    private static Activity mActivity;
    private static Integer mWrapper;
    private static Class<? extends IController> mStartupController;
    private static ControllerOptions mStartupControllerOptions;

    private static ArrayList<Dependency> mDependencies = new ArrayList<>();
    private static ArrayList<IController> mControllers = new ArrayList<>();


    public static String getSimpleName(){

        return Impala.class.getSimpleName();

    }

    public static void use(Class<? extends IDependency> ...args){

        // leave method if it's not in start point

        if(mLeftStartPoint) {
            Log.e(getSimpleName() , "Unable to add dependency after run() method");
            return;
        }

        for(Class<? extends IDependency> a:args) {


            // ignore dependency if registered before

            if(dependencyExists(a.getName()))
                continue;



            // check if dependency is type of service

            if( isService(a) ){

                // TODO: register Service

                continue;

            }



            if( isIntentService(a) ){

                // TODO: register IntentService

                continue;

            }



            if(Modifier.isStatic(a.getModifiers())){

                // TODO: handle static dependency class
                // for now just ignore them

                continue;

            }



            // register dependency

            try {

                IDependency instance = (IDependency) instantiate(a);

                if(instance == null)
                {
                    Log.e(getSimpleName() , String.format("Unable to register dependency %s", a.getName()));
                    return;
                }

                register( instance.getClass().getName(), DependencyType.Object, instance );

            } catch(Exception e) {

                 e.printStackTrace();

            }

        }

    }

    private static Object instantiate(Class<?> clazz)
            throws
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {


        Constructor<?>[] ctors = clazz.getConstructors();



        if ( ctors.length != 1 ) {

            Log.e(getSimpleName() , String.format("More than one or zero constructor is defined for %s", clazz.getName()));

            return null;

        }


        Class<?>[] a = ctors[0].getParameterTypes();

        ArrayList<IDependency> params = new ArrayList<>();

        for (Class<?> dep : a) {

            Dependency d = getDependencyByName(dep.getName());


            // leave if dependency is not registered

            if (d == null){

                Log.e(getSimpleName() , String.format("Dependency %s is not registered" , dep.getName()));

                return null;

            }


            params.add((IDependency) d.getInstance());

        }

        return ctors[0].newInstance(params.toArray(new Object[a.length]));


    }

    private static void register(String name , DependencyType type , IDependency instance){

        mDependencies.add(new Dependency(name , type , instance));

    }

    private static Dependency getDependencyByName(String name){
        for (Dependency d : mDependencies) {
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null;
    }

    private static boolean dependencyExists(String name){

        return getDependencyByName(name) != null;

    }

    public static void init(Activity activity , Integer wrapper){
        mActivity = activity;
        mWrapper = wrapper;
    }

    public static void run(Class<? extends IController> controller ) {
        run(controller , (ControllerOptions) null);
    }

    public static void run(Class<? extends IController> controller , String startup) {
        ControllerOptions options = new ControllerOptions();
        options.useDefaultFormOperations()
                .setStartupPage(startup);
        run(controller , options);
    }

    public static void run(Class<? extends IController> controller , ControllerOptions options) {

        mLeftStartPoint = true;
        mStartupController = controller;
        mStartupControllerOptions = options;

        if(!allProcessesAttached())
            return;

        try {
            startup();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private static boolean isIntentService(Class<? extends IDependency> obj){

        return obj.getClass().equals(IntentService.class);

    }


    private static boolean isService(Class<? extends IDependency> obj){

        return obj.getClass().equals(Service.class);

    }



    public static boolean allProcessesAttached(){

        for(IDependency d: mDependencies){

            if(!isService(d.getClass()) && !isIntentService(d.getClass()))
                continue;

            // ProcessState state = d.getService().getProcessState();

            // if(state == ProcessState.Suspended || state == ProcessState.Destroyed)
            //    return false;

        }

        return true;

    }

    private static void startup()
            throws IllegalAccessException, InvocationTargetException, InstantiationException {


        // leave if Impala is not initialized

        if(mActivity == null || mStartupController == null){

            Log.e(getSimpleName() , "Activity or startup controller is not defined");

            return;

        }

        // Instantiate controller

        IController inst = (IController) instantiate(mStartupController);

        mControllers.add(inst.config(mActivity, mWrapper, mStartupControllerOptions));

        String startup = inst.getOptions().getStartupPage();


        // search for single method

        if (!startup.contains("|")) {
            try {
                Method m = inst.getClass().getMethod(startup);
                m.invoke(inst);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        // search for multiple methods
        String[] nominatedPages = startup.split("\\|");

        for (String page : nominatedPages) {
            try {
                Method m = inst.getClass().getMethod(page);
                m.invoke(inst);
                // leave if found
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static IController getController(Class<? extends IController> type){
        for (IController proc: mControllers) {
            if(proc.getClass().equals(type))
                return proc;
        }
        return null;
    }

    public static Integer getWrapper(){
        return mWrapper;
    }

    public static Activity getActivity(){
        return mActivity;
    }

    public static void stop(Class<? extends IDependency> ...args){

        // TODO: stop services

        /*for (Class<? extends InjectableService> svc:services) {
            Process p = getProcess(svc.getName());
            if(p != null && p.getService().getProcessState().equals(ProcessState.Started) && p.bound) {
                p.getService().stopSelf();
                getActivity().getApplicationContext().unbindService(p.getConnection());
            }
        }
        */
    }



    /* alternative of IControllerLifecycleHooks.OnInit */

    public Impala then() {
        return this;
    }

}

enum ServiceState{
    Destroyed, Alive , Suspended
}

enum DependencyType {
    Service , IntentService, Object
}

class Dependency implements IDependency {

    private DependencyType mType;
    private String mName;
    private Object mInstance;
    private Object mMetadata;

    public DependencyType getType() {
        return mType;
    }

    public Object getInstance(){
        return mInstance;
    }

    public String getName() {
        return mName;
    }

    public Object getMetadata(){
        return mMetadata;
    }

    Dependency(String name , DependencyType type, IDependency instance){
        this(name , type, instance, null);
    }

    Dependency(String name , DependencyType type, IDependency instance, Object metadata){
        mName = name;
        mType = type;
        mInstance = instance;
        mMetadata = metadata;
    }

}

class ServiceDetails {

    private ServiceState mState;
    private ServiceConnection mConnection;

}