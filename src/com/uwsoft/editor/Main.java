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

package com.uwsoft.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.jglfw.JglfwApplication;
import com.badlogic.gdx.backends.jglfw.JglfwApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.utils.AppConfig;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main {

    public Main() {
        Rectangle maximumWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        double width = maximumWindowBounds.getWidth();
        double height = maximumWindowBounds.getHeight();
        Overlap2D overlap2D = new Overlap2D();
        if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Overlap2D");
            JglfwApplicationConfiguration config = new JglfwApplicationConfiguration();
            config.width = (int) (width);
            config.height = (int) (height - height * .04);
            config.backgroundFPS = 0;
            config.title = "Overlap2D - Public Alpha v" + AppConfig.getInstance().version;
            new JglfwApplication(overlap2D, config);
        } else {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.title = "Overlap2D - Public Alpha v" + AppConfig.getInstance().version;
            config.width = (int) (width);
            config.height = (int) (height - height * .04);
            config.backgroundFPS = 0;
            LwjglFrame mainFrame = new LwjglFrame(overlap2D, config);
            mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }

    }

    public static void main(String[] argv) throws Exception {
        generateBitmapFonts();
        generateTextureAtlas();
        new Main();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }

    public static void generateBitmapFonts() {
        FileHandle fileHandle = new FileHandle(new File("../assets/freetypefonts/Roboto-Medium.ttf"));
        generateFontWriteFiles("Roboto-Medium", fileHandle, 32, 1280, 1280);
    }

    public static void generateTextureAtlas() {
        String input = "../art/fonts";
        String output = "style";
        String packFileName = "uiskin";
        TexturePacker.Settings settings =  new TexturePacker.Settings();
        settings.flattenPaths = true;
        TexturePacker.processIfModified(input, output, packFileName);
    }
    //------------------------------------------------
    private static void generateFontWriteFiles(String fontName, FileHandle fontFile, int fontSize, int pageWidth, int pageHeight) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        PixmapPacker packer = new PixmapPacker(pageWidth, pageHeight, Pixmap.Format.RGBA8888, 2, false);
        FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.packer = packer;
        parameters.size = 32;
        FreeTypeFontGenerator.FreeTypeBitmapFontData fontData = generator.generateData(parameters);
        Array<PixmapPacker.Page> pages = packer.getPages();
        TextureRegion[] texRegions = new TextureRegion[pages.size];
        for (int i=0; i<pages.size; i++) {
            PixmapPacker.Page p = pages.get(i);
            Texture tex = new Texture(new PixmapTextureData(p.getPixmap(), p.getPixmap().getFormat(), false, false, true)) {
                @Override
                public void dispose () {
                    super.dispose();
                    getTextureData().consumePixmap().dispose();
                }
            };
            tex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            texRegions[i] = new TextureRegion(tex);
        }
        BitmapFont bitmapFont = generator.generateFont(parameters);
        saveFontToFile(bitmapFont, fontSize, fontName, packer);
        generator.dispose();
        packer.dispose();
       // return bitmapFont;
    }

    private static boolean saveFontToFile(BitmapFont font, int fontSize, String fontName, PixmapPacker packer) {
        FileHandle fontFile = getFontFile(fontName + ".fnt"); // .fnt path
        FileHandle pixmapDir = getFontFile(fontName); // png dir path
        BitmapFontWriter.setOutputFormat(BitmapFontWriter.OutputFormat.Text);

        String[] pageRefs = BitmapFontWriter.writePixmaps(packer.getPages(), pixmapDir, fontName);
        System.out.println(String.format("Saving font [%s]: fontfile: %s, pixmapDir: %s\n", fontName, fontFile, pixmapDir));
        // here we must add the png dir to the page refs
        for (int i = 0; i < pageRefs.length; i++) {
            pageRefs[i] =  pageRefs[i];
            //Tools.log.debug("\tpageRef: " + pageRefs[i]);
        }
        BitmapFontWriter.writeFont(font.getData(), pageRefs, fontFile, new BitmapFontWriter.FontInfo(fontName, fontSize), 1, 1);
        return true;
    }

    private static FileHandle getFontFile(String filename) {
        return Gdx.files.local("art/textures" + filename);
    }
}
