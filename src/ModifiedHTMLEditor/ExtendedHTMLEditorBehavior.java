/*
 * Copyright (c) 2010, 2014, Oracle and/or its affiliates. All rights reserved.
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

import javafx.scene.web.HTMLEditor;
import java.util.ArrayList;
import java.util.List;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.web.skin.HTMLEditorSkin;
import static javafx.scene.input.KeyCode.B;
import static javafx.scene.input.KeyCode.F12;
import static javafx.scene.input.KeyCode.I;
import static javafx.scene.input.KeyCode.TAB;
import static javafx.scene.input.KeyCode.U;


/**
 * HTML editor behavior.
 */
public class ExtendedHTMLEditorBehavior extends BehaviorBase<ExtendedHTMLEditor> {
    protected static final List<KeyBinding> HTML_EDITOR_BINDINGS = new ArrayList<KeyBinding>();

    static {
        HTML_EDITOR_BINDINGS.add(new KeyBinding(B, "bold").shortcut());
        HTML_EDITOR_BINDINGS.add(new KeyBinding(I, "italic").shortcut());
        HTML_EDITOR_BINDINGS.add(new KeyBinding(U, "underline").shortcut());

        HTML_EDITOR_BINDINGS.add(new KeyBinding(F12, "F12"));
        HTML_EDITOR_BINDINGS.add(new KeyBinding(TAB, "TraverseNext").ctrl());
        HTML_EDITOR_BINDINGS.add(new KeyBinding(TAB, "TraversePrevious").ctrl().shift());
    }

    public ExtendedHTMLEditorBehavior(ExtendedHTMLEditor htmlEditor) {
        super(htmlEditor, HTML_EDITOR_BINDINGS);
    }

    @Override
    protected void callAction(String name) {
        if ("bold".equals(name) || "italic".equals(name) || "underline".equals(name)) {
            ExtendedHTMLEditor editor = getControl();
            HTMLEditorSkin editorSkin = (HTMLEditorSkin)editor.getSkin();
            editorSkin.keyboardShortcuts(name);
        } else if ("F12".equals(name)) {
            getControl().getImpl_traversalEngine().selectFirst().requestFocus();
        } else {
            super.callAction(name);
        }
    }
}
