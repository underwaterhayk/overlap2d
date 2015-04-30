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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.mvc.event.CheckBoxChangeListener;
import com.uwsoft.editor.mvc.event.SelectBoxChangeListener;
import com.uwsoft.editor.mvc.view.ui.properties.UIItemProperties;
import com.uwsoft.editor.renderer.data.LightVO;

/**
 * Created by azakhary on 4/28/2015.
 */
public class UILightItemProperties extends UIItemProperties {

    private VisCheckBox isStaticCheckBox;
    private VisCheckBox isXRayCheckBox;
    private NumberSelector rayCountSelector;
    private VisSelectBox<String> lightTypeSelectBox;

    private VisValidableTextField pointLightRadiusField;

    private VisValidableTextField coneInnerAngleField;
    private VisValidableTextField coneDistanceField;

    private VisTable secondaryTable;

    public UILightItemProperties() {
        super();

        Validators.FloatValidator floatValidator = new Validators.FloatValidator();

        isStaticCheckBox = new VisCheckBox(null);
        isXRayCheckBox = new VisCheckBox(null);
        rayCountSelector = new NumberSelector(null, 4, 4, 5000, 1);
        lightTypeSelectBox = new VisSelectBox<>();
        pointLightRadiusField = new VisValidableTextField(floatValidator);
        coneInnerAngleField = new VisValidableTextField(floatValidator);
        coneDistanceField = new VisValidableTextField(floatValidator);

        secondaryTable = new VisTable();

        Array<String> types = new Array<>();
        types.add("Point Light");
        types.add("Cone Light");
        lightTypeSelectBox.setItems(types);

        add(new VisLabel("Is Static: ", Align.right)).padRight(5).width(55).right();
        add(isStaticCheckBox).left();
        row().padTop(5);
        add(new VisLabel("Is XRay: ", Align.right)).padRight(5).width(55).right();
        add(isXRayCheckBox).left();
        row().padTop(5);
        add(new VisLabel("Ray Count: ", Align.right)).padRight(5).width(55).right();
        add(rayCountSelector).left();
        row().padTop(5);
        add(new VisLabel("Type: ", Align.right)).padRight(5).width(55).right();
        add(lightTypeSelectBox).left();
        row().padTop(5);

        add(secondaryTable).colspan(2); row();


        setListeners();
    }

    public void initPointFields() {
        secondaryTable.clear();

        secondaryTable.add(new VisLabel("Radius: ", Align.right)).padRight(5).width(55).right();
        secondaryTable.add(pointLightRadiusField).width(70).left();
        secondaryTable.row().padTop(5);
    }

    public void initConeFields() {
        secondaryTable.clear();

        secondaryTable.add(new VisLabel("Distance: ", Align.right)).padRight(5).width(55).right();
        secondaryTable.add(coneDistanceField).width(70).left();
        secondaryTable.row().padTop(5);
        secondaryTable.add(new VisLabel("Type: ", Align.right)).padRight(5).width(55).right();
        secondaryTable.add(coneInnerAngleField).width(70).left();
        secondaryTable.row().padTop(5);
    }

    public void setType(LightVO.LightType type) {
        if(type == LightVO.LightType.POINT) {
            lightTypeSelectBox.setSelectedIndex(0);
            initPointFields();
        } else if(type == LightVO.LightType.CONE) {
            lightTypeSelectBox.setSelectedIndex(1);
            initConeFields();
        }
    }

    public void setRayCount(int count) {
        rayCountSelector.setValue(count);
    }

    public void setStatic(boolean isStatic) {
        isStaticCheckBox.setChecked(isStatic);
    }

    public void setXRay(boolean isXRay) {
        isXRayCheckBox.setChecked(isXRay);
    }

    public void setRadius(String radius) {
        pointLightRadiusField.setText(radius);
    }

    public void setAngle(String angle) {
        coneInnerAngleField.setText(angle);
    }

    public void setDistance(String distance) {
        coneDistanceField.setText(distance);
    }

    public LightVO.LightType getType() {
        if(lightTypeSelectBox.getSelectedIndex() == 0) {
            return LightVO.LightType.POINT;
        } else {
            return LightVO.LightType.CONE;
        }
    }

    public int getRayCount() {
        return rayCountSelector.getValue();
    }

    public boolean isStatic() {
        return isStaticCheckBox.isChecked();
    }

    public boolean isXRay() {
        return isXRayCheckBox.isChecked();
    }

    public String getRadius() {
        return pointLightRadiusField.getText();
    }

    public String getAngle() {
        return coneInnerAngleField.getText();
    }

    public String getDistance() {
        return coneDistanceField.getText();
    }

    private void setListeners() {
        isStaticCheckBox.addListener(new CheckBoxChangeListener(PROPERTIES_UPDATED));
        isXRayCheckBox.addListener(new CheckBoxChangeListener(PROPERTIES_UPDATED));
        rayCountSelector.addChangeListener(new NumberSelector.NumberSelectorListener() {
            @Override
            public void changed(int number) {
                facade.sendNotification(PROPERTIES_UPDATED);
            }
        });
        lightTypeSelectBox.addListener(new SelectBoxChangeListener(PROPERTIES_UPDATED));

        lightTypeSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(lightTypeSelectBox.getSelectedIndex() == 0) {
                    initPointFields();
                } else {
                    initConeFields();
                }
            }
        });
    }
}