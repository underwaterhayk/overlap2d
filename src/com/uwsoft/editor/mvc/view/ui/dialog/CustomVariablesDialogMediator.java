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

package com.uwsoft.editor.mvc.view.ui.dialog;

import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UIBasicItemProperties;

/**
 * Created by azakhary on 5/12/2015.
 */
public class CustomVariablesDialogMediator extends SimpleMediator<CustomVariablesDialog> {
    private static final String TAG = CustomVariablesDialogMediator.class.getCanonicalName();
    private static final String NAME = TAG;

    public CustomVariablesDialogMediator() {
        super(NAME, new CustomVariablesDialog());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                UIBasicItemProperties.CUSTOM_VARS_BUTTON_CLICKED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();

        switch (notification.getName()) {
            case UIBasicItemProperties.CUSTOM_VARS_BUTTON_CLICKED:
                viewComponent.show(uiStage);
                break;
        }
    }
}