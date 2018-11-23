/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chungo.base.lifecyclemodel;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


/**
 * Factory methods for {@link LifecycleModelStore} class.
 * <p>
 * Created by JessYan on 21/11/2017 16:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 */
@SuppressWarnings("WeakerAccess")
public class LifecycleModelStores {

    private LifecycleModelStores() {
    }

    /**
     * Returns the {@link LifecycleModelStore} of the given activity.
     *
     * @param activity an activity whose {@code LifecycleModelStore} is requested
     * @return a {@code LifecycleModelStore}
     */
    @MainThread
    public static LifecycleModelStore of(@NonNull FragmentActivity activity) {
        return HolderFragment.holderFragmentFor(activity).getLifecycleModelStore();
    }

    /**
     * Returns the {@link LifecycleModelStore} of the given fragment.
     *
     * @param fragment a fragment whose {@code LifecycleModelStore} is requested
     * @return a {@code LifecycleModelStore}
     */
    @MainThread
    public static LifecycleModelStore of(@NonNull Fragment fragment) {
        return HolderFragment.holderFragmentFor(fragment).getLifecycleModelStore();
    }
}