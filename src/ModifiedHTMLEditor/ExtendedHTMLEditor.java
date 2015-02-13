/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package ModifiedHTMLEditor;

import javafx.css.StyleableProperty;
import javafx.scene.control.Control;
import javafx.print.PrinterJob;
import javafx.scene.control.Skin;


/**
 * A control that allows for users to edit text, and apply styling to this text.
 * The underlying data model is HTML, although this is not shown visually to the
 * end-user.
 * @since JavaFX 2.0
 */
public class ExtendedHTMLEditor extends Control {

    /**
     * Creates a new instance of the HTMLEditor control.
     */
    private ExtendedHTMLEditorSkin skin;

    public ExtendedHTMLEditor() {
        skin = new ExtendedHTMLEditorSkin(this);
        ((StyleableProperty)super.skinClassNameProperty()).applyStyle(null, "ModifiedHTMLEditor.ExtendedHTMLEditorSkin");
        getStyleClass().add("html-editor");
    }

    @Override protected Skin<?> createDefaultSkin() {
        return skin;
    }

    public void boldFire() {
        skin.boldFire2();
    }

    /**
     * Returns the HTML content of the editor.
     */
    public String getHtmlText() {
        return ((ExtendedHTMLEditorSkin)getSkin()).getHTMLText();
    }

    /**
     * Sets the HTML content of the editor. Note that if the contentEditable
     * property on the <body> tag of the provided HTML is not set to true, the
     * HTMLEditor will become read-only. You can ensure that the text remains
     * editable by ensuring the body appears as such: 
     * <code>
     * &lt;body contentEditable="true"&gt;
     * </code>
     *
     * @param htmlText The full HTML markup to put into the editor. This should
     *      include all normal HTML elements, starting with 
     *      <code>&lt;html&gt;</code>, and including a <code>&lt;body&gt;</code>.
     */
    public void setHtmlText(String htmlText) {
        ((ExtendedHTMLEditorSkin)getSkin()).setHTMLText(htmlText);
    }

    /**
     * Prints the content of the editor using the given printer job.
     * <p>This method does not modify the state of the job, nor does it call
     * {@link PrinterJob#endJob}, so the job may be safely reused afterwards.
     *
     * @param job printer job used for printing
     * @since JavaFX 8.0
     */
    public void print(PrinterJob job) {
        ((ExtendedHTMLEditorSkin)getSkin()).print(job);
    }
}
