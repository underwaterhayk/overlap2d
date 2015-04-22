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

package com.uwsoft.editor.mvc.view.ui.properties.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.dialogs.EditAnimationDialog;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemPropertiesMediator;
import com.uwsoft.editor.renderer.actor.SpriteAnimation;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by azakhary on 4/16/2015.
 */
public class UISpriteAnimationItemPropertiesMediator extends UIItemPropertiesMediator<SpriteAnimation, UISpriteAnimationItemProperties> {
    private static final String TAG = UISpriteAnimationItemPropertiesMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UISpriteAnimationItemPropertiesMediator() {
        super(NAME, new UISpriteAnimationItemProperties());
    }

    @Override
    public String[] listNotificationInterests() {
        String[] defaultNotifications = super.listNotificationInterests();
        String[] notificationInterests = new String[]{
                UISpriteAnimationItemProperties.EDIT_ANIMATIONS_CLICKED
        };

        return ArrayUtils.addAll(defaultNotifications, notificationInterests);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case UISpriteAnimationItemProperties.EDIT_ANIMATIONS_CLICKED:
                EditAnimationDialog dlg = new EditAnimationDialog(Sandbox.getInstance().getUIStage(), observableReference);
                Sandbox.getInstance().getUIStage().addActor(dlg);
                break;
            default:
                break;
        }
    }

    @Override
    protected void translateObservableDataToView(SpriteAnimation item) {
        Array<String> animations = new Array<>();
        for (String name : item.getAnimations().keySet()) {
            animations.add(name);
        }

        viewComponent.setAnimations(animations);
        viewComponent.setSelectedAnimation(item.getCurrentAnimationName());
    }

    @Override
    protected void translateViewToItemData() {
        observableReference.setAnimation(viewComponent.getSelected());
        observableReference.renew();
    }
}