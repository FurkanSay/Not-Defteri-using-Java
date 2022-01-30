import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;public class JFontChooser extends JComponent {

    /** The list of possible font sizes. */
    private static final Integer[] SIZES =
            {8, 9, 10, 11, 12, 13, 14, 16, 18, 20, 24, 26, 28, 32, 36, 40, 48, 56, 64, 72};

    /** The list of possible fonts. */
    private static final String[] FONTS = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getAvailableFontFamilyNames();


    private FontSelectionModel selectionModel;

    private JList fontList;

    private JList sizeList;

    private JCheckBox boldCheckBox;

    private JCheckBox italicCheckBox;

    private JLabel previewLabel;

    /** The preview text, if null the font name will be the preview text. */
    private String previewText;

    /** Listener used to update the font of the selection model. */
    private SelectionUpdater selectionUpdater = new SelectionUpdater();

    /** Listener used to update the font in the components. This should be registered
     * with the selection model. */
    private LabelUpdater labelUpdater = new LabelUpdater();

    /** True if the components are being updated and no event should be generated. */
    private boolean updatingComponents = false;

    /** Listener class used to update the font in the components. This should be registered
      * with the selection model. */
    private class LabelUpdater implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            updateComponents();
        }

    }

    /** Listener class used to update the font of the preview label. */
    private class SelectionUpdater implements ChangeListener, ListSelectionListener {

        public void stateChanged(ChangeEvent e) {
            if (!updatingComponents) {
                setFont(buildFont());
            }
        }

        public void valueChanged(ListSelectionEvent e) {
            if (!updatingComponents) {
                setFont(buildFont());
            }
        }
    }
    

public Font showDialog(Component component, String title) {

    FontTracker ok = new FontTracker(this);
    JDialog dialog = createDialog(component, title, true, ok, null);
    dialog.addWindowListener(new FontChooserDialog.Closer());
    dialog.addComponentListener(new FontChooserDialog.DisposeOnClose());

    dialog.setVisible(true); // blocks until user brings dialog down...

    return ok.getFont();
}

public JDialog createDialog(Component c, String title, boolean modal,
        ActionListener okListener, ActionListener cancelListener) {

        return new FontChooserDialog(c, title, modal, this,
                okListener, cancelListener);
    }
public JFontChooser() {
    this(new DefaultFontSelectionModel());
}
public JFontChooser(Font initialFont) {
    this(new DefaultFontSelectionModel(initialFont));
}
    

public JFontChooser(FontSelectionModel model) {
    selectionModel = model;
    init(model.getSelectedFont());
    selectionModel.addChangeListener(labelUpdater);
}

private void init(Font font) {
    setLayout(new GridBagLayout());

    Insets ins = new Insets(2, 2, 2, 2);

    fontList = new JList(FONTS);
    fontList.setVisibleRowCount(10);
    fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    add(new JScrollPane(fontList), new GridBagConstraints(0, 0, 1, 1, 2, 2,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            ins, 0, 0));

    sizeList = new JList(SIZES);
    ((JLabel)sizeList.getCellRenderer()).setHorizontalAlignment(JLabel.RIGHT);
    sizeList.setVisibleRowCount(10);
    sizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    add(new JScrollPane(sizeList), new GridBagConstraints(1, 0, 1, 1, 1, 2,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            ins, 0, 0));

    boldCheckBox = new JCheckBox("Bold");
    add(boldCheckBox, new GridBagConstraints(0, 1, 2, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            ins, 0, 0));

    italicCheckBox = new JCheckBox("Italic");
    add(italicCheckBox, new GridBagConstraints(0, 2, 2, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            ins, 0, 0));

    previewLabel = new JLabel("");
    previewLabel.setHorizontalAlignment(JLabel.CENTER);
    previewLabel.setVerticalAlignment(JLabel.CENTER);
    add(new JScrollPane(previewLabel), new GridBagConstraints(0, 3, 2, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            ins, 0, 0));

    setFont(font == null ? previewLabel.getFont() : font);

    fontList.addListSelectionListener(selectionUpdater);
    sizeList.addListSelectionListener(selectionUpdater);
    boldCheckBox.addChangeListener(selectionUpdater);
    italicCheckBox.addChangeListener(selectionUpdater);
}

private Font buildFont() {
//  Font labelFont = previewLabel.getFont();

  String fontName = (String)fontList.getSelectedValue();
  if (fontName == null) {
      return null;
//      fontName = labelFont.getName();
  }
  Integer sizeInt = (Integer)sizeList.getSelectedValue();
  if (sizeInt == null) {
//      size = labelFont.getSize();
      return null;
  }
  
  return new Font(fontName,
          (italicCheckBox.isSelected() ? Font.ITALIC : Font.PLAIN)
          | (boldCheckBox.isSelected() ? Font.BOLD : Font.PLAIN),
          sizeInt);
}

private void updateComponents() {
    updatingComponents = true;

    Font font = getFont();

    fontList.setSelectedValue(font.getName(), true);
    sizeList.setSelectedValue(font.getSize(), true);
    boldCheckBox.setSelected(font.isBold());
    italicCheckBox.setSelected(font.isItalic());

    if (previewText == null) {
        previewLabel.setText(font.getName());
    }

    // set the font and fire a property change
    Font oldValue = previewLabel.getFont();
    previewLabel.setFont(font);
    firePropertyChange("font", oldValue, font);

    updatingComponents = false;
}

public FontSelectionModel getSelectionModel() {
    return selectionModel;
}

public void setSelectionModel(FontSelectionModel newModel ) {
    FontSelectionModel oldModel = selectionModel;
    selectionModel = newModel;
    oldModel.removeChangeListener(labelUpdater);
    newModel.addChangeListener(labelUpdater);
    firePropertyChange("selectionModel", oldModel, newModel);
}

public Font getFont() {
    return selectionModel.getSelectedFont();
}
public void setFont(Font font) {
    selectionModel.setSelectedFont(font);
}
public String getPreviewText() {
    return previewText;
}
public void setPreviewText(String previewText) {
    this.previewText = previewText;
    previewLabel.setText("");
    updateComponents();
}

}
class FontChooserDialog extends JDialog {
    private Font initialFont;
    private JFontChooser chooserPane;

    public FontChooserDialog(Component c, String title, boolean modal,
              JFontChooser chooserPane,
              ActionListener okListener, ActionListener cancelListener) {
        super(JOptionPane.getFrameForComponent(c), title, modal);
        //setResizable(false);

        String okString = UIManager.getString("ColorChooser.okText");
        String cancelString = UIManager.getString("ColorChooser.cancelText");
        String resetString = UIManager.getString("ColorChooser.resetText");

        /*
         * Create Lower button panel
         */
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton(okString);
        getRootPane().setDefaultButton(okButton);
        okButton.setActionCommand("OK");
        if (okListener != null) {
            okButton.addActionListener(okListener);
        }
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPane.add(okButton);

        JButton cancelButton = new JButton(cancelString);

        // The following few lines are used to register esc to close the dialog
        Action cancelKeyAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // todo make it in 1.3
//                ActionListener[] listeners
//                        = ((AbstractButton) e.getSource()).getActionListeners();
//                for (int i = 0; i < listeners.length; i++) {
//                    listeners[i].actionPerformed(e);
//                }
            }
        };
        KeyStroke cancelKeyStroke = KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE);
        InputMap inputMap = cancelButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = cancelButton.getActionMap();
        if (inputMap != null && actionMap != null) {
            inputMap.put(cancelKeyStroke, "cancel");
            actionMap.put("cancel", cancelKeyAction);
        }
        // end esc handling

        cancelButton.setActionCommand("cancel");
        if (cancelListener != null) {
            cancelButton.addActionListener(cancelListener);
        }
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPane.add(cancelButton);

        JButton resetButton = new JButton(resetString);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        int mnemonic = UIManager.getInt("ColorChooser.resetMnemonic");
        if (mnemonic != -1) {
            resetButton.setMnemonic(mnemonic);
        }
        buttonPane.add(resetButton);


        // initialiase the content pane
        this.chooserPane = chooserPane;

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(chooserPane, BorderLayout.CENTER);

        contentPane.add(buttonPane, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(c);
    }
    public void setVisible(boolean visible) {
        if (visible)
            initialFont = chooserPane.getFont();
        super.setVisible(visible);
    }

    public void reset() {
        chooserPane.setFont(initialFont);
    }

    static class Closer extends WindowAdapter implements Serializable {
        public void windowClosing(WindowEvent e) {
            Window w = e.getWindow();
            w.setVisible(false);
        }
    }

    static class DisposeOnClose extends ComponentAdapter implements Serializable {
        public void componentHidden(ComponentEvent e) {
            Window w = (Window) e.getComponent();
            w.dispose();
        }
    }

}

class FontTracker implements ActionListener, Serializable {
    JFontChooser chooser;
    Font color;

    public FontTracker(JFontChooser c) {
        chooser = c;
    }

    public void actionPerformed(ActionEvent e) {
        color = chooser.getFont();
    }

    public Font getFont() {
        return color;
    }
}

class DefaultFontSelectionModel implements FontSelectionModel {

    /** The default selected font. */
    private static final Font DEFAULT_INITIAL_FONT = new Font("Dialog", Font.PLAIN, 12);
    
    /** The selected font. */
    private Font selectedFont;

    /** The change listeners notified by a change in this model. */
    private EventListenerList listeners = new EventListenerList();

    /**
     * Creates a <code>DefaultFontSelectionModel</code> with the
     * current font set to <code>Dialog, 12</code>.  This is
     * the default constructor.
     */
    public DefaultFontSelectionModel() {
        this(DEFAULT_INITIAL_FONT);
    }
    public DefaultFontSelectionModel(Font selectedFont) {
        if (selectedFont == null) {
            selectedFont = DEFAULT_INITIAL_FONT;
        }
        this.selectedFont = selectedFont;
    }

    public Font getSelectedFont() {
        return selectedFont;
    }

    public void setSelectedFont(Font selectedFont) {
        if (selectedFont != null) {
            this.selectedFont = selectedFont;
            fireChangeListeners();
        }
    }

    public void addChangeListener(ChangeListener listener) {
        listeners.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(ChangeListener.class, listener);
    }

    /** Fires the listeners registered with this model. */
    protected void fireChangeListeners() {
        ChangeEvent ev = new ChangeEvent(this);
        Object[] l = listeners.getListeners(ChangeListener.class);
        for (Object listener : l) {
            ((ChangeListener) listener).stateChanged(ev);
        }
    }
}
interface FontSelectionModel {
    /**
     * Returns the selected <code>Font</code> which should be
     * non-<code>null</code>.
     *
     * @return  the selected <code>Font</code>
     * @see     #setSelectedFont
     */
    Font getSelectedFont();

    /**
     * Sets the selected font to <code>font</code>.
     * Note that setting the font to <code>null</code>
     * is undefined and may have unpredictable results.
     * This method fires a state changed event if it sets the
     * current font to a new non-<code>null</code> font.
     *
     * @param font the new <code>Font</code>
     * @see   #getSelectedFont
     * @see   #addChangeListener
     */
    void setSelectedFont(Font font);

    /**
     * Adds <code>listener</code> as a listener to changes in the model.
     * @param listener the <code>ChangeListener</code> to be added
     */
    void addChangeListener(ChangeListener listener);

    /**
     * Removes <code>listener</code> as a listener to changes in the model.
     * @param listener the <code>ChangeListener</code> to be removed
     */
    void removeChangeListener(ChangeListener listener);
}
//////////////////////////////