/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.mvc.view.ui.box.resourcespanel;

import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UIParticleEffectsTabMediator extends SimpleMediator<UIParticleEffectsTab> {

    private static final String TAG = UIParticleEffectsTabMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIParticleEffectsTabMediator() {
        super(NAME, new UIParticleEffectsTab());
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2D.PROJECT_OPENED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        switch (notification.getName()) {
            case Overlap2D.PROJECT_OPENED:

                break;
            default:
                break;
        }
    }
}