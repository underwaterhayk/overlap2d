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

package com.uwsoft.editor.mvc.view.stage;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.*;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.DropDown;
import com.uwsoft.editor.mvc.view.ui.box.UIItemsTreeBox;
import com.uwsoft.editor.gdx.ui.UILightBox;
import com.uwsoft.editor.gdx.ui.dialogs.ItemPhysicsDialog;
import com.uwsoft.editor.gdx.ui.layer.UILayerBoxOld;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.EditorResourceManager;
import com.uwsoft.editor.mvc.view.Overlap2DMenuBarMediator;
import com.uwsoft.editor.mvc.view.ui.UIMainTable;
import com.uwsoft.editor.mvc.view.ui.box.UILayerBox;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.LayerItemVO;

public class UIStage extends Stage {

    public final SceneLoader sceneLoader;
    public final Essentials essentials;
    private final Overlap2DFacade facade;
    public SandboxStage sandboxStage;
    public Group dummyTarget;
    public CompositeItem sceneUI;
    public UIMainTable uiMainTable;
    public Group contextMenuContainer;


    public DropDown mainDropDown;

    public Overlap2DMenuBarMediator menuMediator;

    public UIStage(SandboxStage sandboxStage) {
        super(new ScreenViewport());

        facade = Overlap2DFacade.getInstance();
        essentials = new Essentials();
        essentials.rm = facade.retrieveProxy(EditorResourceManager.NAME);

        SceneLoader sceneLoader = new SceneLoader(essentials);
        sceneLoader.loadScene("MainScene");

        this.sceneLoader = sceneLoader;

        this.sandboxStage = sandboxStage;

        sceneUI = sceneLoader.getSceneAsActor();

        dummyTarget = new Group();
        dummyTarget.setWidth(getWidth());
        dummyTarget.setHeight(getHeight());
        dummyTarget.setY(0);
        dummyTarget.setX(0);
        addActor(dummyTarget);

        contextMenuContainer = new Group();
        uiMainTable = new UIMainTable(this);
        menuMediator = uiMainTable.menuMediator;

        addActor(uiMainTable);
        addActor(contextMenuContainer);

        setListeners();


        mainDropDown = new DropDown(contextMenuContainer);
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }

    public Sandbox getSandbox() {
        return sandboxStage.sandbox;
    }

    public void editPhysics(String assetName) {
        ItemPhysicsDialog dlg = new ItemPhysicsDialog(this);
        addActor(dlg);
        dlg.editAsset(assetName);
    }

    public void editPhysics(IBaseItem item) {
        ItemPhysicsDialog dlg = new ItemPhysicsDialog(this);
        addActor(dlg);
        dlg.editItem(item);
    }

    public void setKeyboardFocus() {
        setKeyboardFocus(dummyTarget);
    }

    public void updateCurrentItemState() {
        // TODO: do this with notification
       // uiMainTable.multiPropertyBox.updateState();
        facade.sendNotification(Overlap2D.ITEM_DATA_UPDATED);
    }

    public void itemWasSelected(IBaseItem itm) {
        //uiMainTable.multiPropertyBox.setItem(itm);
        facade.sendNotification(Overlap2D.ITEM_SELECTED, itm);

        //uiMainTable.layerPanel.selectLayerByName(itm.getDataVO().layerName);
    }

    public void loadCurrentProject() {
       // uiMainTable.libraryPanel.initContent();
        uiMainTable.lightBox.initContent();
//        uiMainTable.itemsBox.init();
//        uiMainTable.compositePanel.initResolutionBox();

        //uiMainTable.layerPanel.initContent();

//        UIController.instance.sendNotification(NameConstants.PROJECT_OPENED, DataManager.getInstance().getCurrentProjectInfoVO());
    }

    public void reInitLibrary() {
        //uiMainTable.libraryPanel.initContent();

        uiMainTable.lightBox.initContent();
//        uiMainTable.itemsBox.init();
    }

    public void emptyClick() {
        facade.sendNotification(Overlap2D.EMPTY_SPACE_CLICKED);
    }

    public void loadScene(CompositeItemVO scene) {
        getSandbox().initSceneView(scene);
    }


    public void setListeners() {
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainDropDown.hide();
                return event.getTarget() != getRoot() && event.getTarget() != dummyTarget;
            }
        });
    }
    
    public LayerItemVO getCurrentSelectedLayer() {
        if (uiMainTable.layerPanel.currentSelectedLayerIndex == -1) return null;
        return getSandbox().sceneControl.getCurrentScene().dataVO.composite.layers.get(uiMainTable.layerPanel.currentSelectedLayerIndex);
    }


    public UIItemsTreeBox getItemsBox() {
        return uiMainTable.itemsBox;
    }

    public UILightBox getLightBox() {
        return uiMainTable.lightBox;
    }

    public UILayerBox getLayerPanel() {
        return uiMainTable.layerPanel;
    }

    @Override
    public boolean keyDown(int keyCode) {
        return super.keyDown(keyCode);
    }

}